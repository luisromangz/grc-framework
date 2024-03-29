package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.SortedArrayList;
import com.greenriver.commons.data.fieldProperties.FieldType;
import com.greenriver.commons.data.fieldProperties.WidgetAction;
import com.greenriver.commons.data.fieldProperties.WidgetActions;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;


/**
 * This subtemplate base class allows creation of tables or lists from
 * a collection of elements.
 *
 * It allows setting format for the elements of the list's elements/columns,
 * and in case the output is selected to be a table, also the table's headers,
 * columns widths. Also, an order for the columns can be defined.
 *
 * @author luis
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
public abstract class ListTableRepeaterSubtemplate<T extends TemplateReplacement, K>
        extends RepeaterSubtemplate<T, K>
        implements Subtemplateable {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public static final String TABLE_CELL_SEPARATOR = "||";
    public static final String TABLE_CELL_SEPARATOR_REGEX = "\\|\\|";
    public static final String COLUMN_SORTING_SEPARATOR = ";";
    @WidgetProps(label = "Tipo de repetición", type = FieldType.SELECTION, externalValues=false,
    possibleValues = {"true", "false"}, possibleValueLabels = {"Tabla", "Lista"})
    private boolean isTable = true;
    @WidgetProps(label = "Lista ordenada", type = FieldType.CHECKBOX, editable = false)
    @WidgetAction(triggerValue = "'true'", newValue = "false", triggerField = "isTable", deactivate = true)
    private boolean isOrderedList;
    @WidgetProps(label = "Mostrar encabezados de la tabla", type = FieldType.CHECKBOX)
    @WidgetAction(triggerValue = "'false'", triggerField = "isTable", newValue = "false")  
    private boolean showTableHeaders = true;
    @WidgetProps(label = "Encabezados de la tabla", type = FieldType.LONGTEXT, required = false, widgetStyle = "width:98%")
    @WidgetActions({
        @WidgetAction(triggerValue = "'false'", triggerField = "isTable", deactivate = true),
        @WidgetAction(triggerValue = "false", triggerField = "showTableHeaders", deactivate = true)
    })
    private String tableHeader = "";
    @WidgetProps(label = "Anchuras de las columnas", type = FieldType.LONGTEXT, required = false, widgetStyle = "width:98%")
    @WidgetAction(triggerValue = "'false'", triggerField = "isTable", deactivate = true)
    private String columnSizes = "";
    @WidgetProps(label = "Columnas por las que se ordena", customRegExp = "\\d+(;\\d+)*", required = false)
    @WidgetAction(triggerValue = "'false'", triggerField = "isTable", deactivate = true)
    private String orderByColumns = "";
    @WidgetProps(label = "Formato del elemento", type = FieldType.LONGTEXT, widgetStyle = "width:98%")
    private String elementFormat;
    @WidgetProps(label = "Estilo del elemento", type = FieldType.SELECTION, externalValues=false,
    possibleValueLabels = {"Normal", "Negrita", "Cursiva", "Negrita y cursiva"},
    possibleValues = {" ", "font-weight:bold", "font-style:italic", "font-weight:bold;font-style:italic"})
    private String fontStyle = " ";
    @WidgetProps(label = "Tamaño de la fuente", type = FieldType.NUMBER, minValue = 4, unit = "pt")
    private int fontSize = 9;
    @WidgetProps(label = "Alineación del texto en la celda", type = FieldType.SELECTION,
    externalValues=false,possibleValueLabels = {"Izquierda", "Centro", "Derecha"},
    possibleValues = {"left", "center", "right"})
    @WidgetAction(triggerField = "isTable", triggerValue = "'false'", newValue = "'left'", deactivate = true)
    private String textAlign = "center";
    @WidgetProps(label = "Bordes", type = FieldType.SELECTION,
    externalValues=false,possibleValueLabels = {"Todos", "Horizontales", "Verticales"},
    possibleValues = {"border-top,border-bottom,border-left,border-right", "border-top,border-bottom", "border-left,border-right"})
    @WidgetAction(triggerField = "isTable", triggerValue = "'false'", deactivate = true)
    private String borders = "border-top,border-bottom,border-left,border-right";
    @WidgetProps(label = "Filas vacías a añadir al final", type = FieldType.NUMBER, minValue = 0)
    @WidgetAction(triggerField = "isTable", triggerValue = "'false'", deactivate = true)
    private int emptyRowsAppended = 0;
    // </editor-fold>
    
    @Override
    protected String fillTemplatesInternal(List<Map<T, String>> replacements) {


        if (isTable) {
            return fillTableTemplates(replacements);
        } else {
            return fillListTemplates(replacements);
        }

    }

    private String fillListTemplates(List<Map<T, String>> replacements) {
        String result = this.isOrderedList ? "<ol>" : "<ul>";

        String elementStyle = elementStyle();
        for (Map<T, String> elementReplacements : replacements) {
            String elementString = this.fillTemplateAux(elementFormat, elementReplacements);
            result += String.format("<li style=\"%s\">%s</li>", elementStyle, elementString);
        }

        result += this.isOrderedList ? "</ol>" : "</ul>";

        return result;
    }

    private String fillTableTemplates(List<Map<T, String>> replacements) {

        if (Strings.isNullOrEmpty(columnSizes)) {
            throw new IllegalStateException("Can't be a table and not having column sizes.");
        }

        List<String> sizes = new ArrayList<String>(Arrays.asList(
                this.getColumnSizes().trim().split(
                ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX)));

        String result = "<table cellspacing=\"0\">";

        String elementStyle = elementStyle();
        if (this.showTableHeaders) {
            // We use the first row to get the header's replacements.
            String header = fillTemplateAux(this.getTableHeader(), replacements.get(0));

            String[] splitHeader = header.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            result += "<thead><tr>";

            for (int i = 0; i < splitHeader.length; i++) {
                result += String.format("<th style=\"%s\">%s</th>",
                        elementStyle, splitHeader[i]);
            }

            result += "</tr></thead>";
        }

        List<TableRow> tableRows = null;
        if (Strings.isNullOrEmpty(this.getOrderByColumns())) {
            tableRows = new ArrayList<TableRow>();
        } else {
            try {
                // We have to sort the table rows before creating the table.
                tableRows = new SortedArrayList<TableRow>(new TableRowComparator(this.getOrderByColumns()));
            } catch (ParseException ex) {
                // This shouldnt happen, as the order string was validated before saving.
                Logger.getLogger(ListTableRepeaterSubtemplate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // We walk the replacements for the table rows.
        int rowIndex=0;
        for (Map<T, String> elementReplacements : replacements) {

            String elementString = this.fillTemplateAux(elementFormat, elementReplacements);

            String[] columnElements = elementString.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            while (sizes.size() < columnElements.length) {
                sizes.add("auto");
            }

            TableRow row = new TableRow(rowIndex);
            for (int i = 0; i < columnElements.length; i++) {
                String size = sizes.get(i);
                row.addCell(columnElements[i], size, elementStyle);
            }

            tableRows.add(row);
            rowIndex++;
        }

        for (int i = 0; i < emptyRowsAppended; i++) {
            // Then we add blanki lines.
            TableRow emptyRow = new TableRow(rowIndex);
            for (int j = 0; j < sizes.size(); j++) {
                emptyRow.addCell("<span style=\"color:white\">empty</span>", sizes.get(j), elementStyle);
            }

            tableRows.add(emptyRow);
            rowIndex++;
        }


        result += "<tbody>";

        for (TableRow row : tableRows) {
            result += row.getRow(tableRows.size());
        }

        result += "</tbody></table>";

        return result;

    }

    private String elementStyle() {
        String computedBorders = "";
        String alignment = "left";
        if (isTable) {
            for (String border : this.borders.split(",")) {
                computedBorders += border + ":0.5mm solid black;";
            }

            alignment = textAlign;
        }

        return String.format("font-size:%spt;%stext-align:%s;%s",
                this.fontSize,
                computedBorders,
                alignment,
                this.fontStyle);
    }

    private String fillTemplateAux(String formatString, Map<T, String> replacements) {
        // We copy the format string so replacements doesn't modify it.
        String result = new String(formatString);
        for (T replacement : replacements.keySet()) {
            String replacementValue = TemplatingUtils.formatTemplateReplacement(
                    replacement,
                    replacements.get(replacement));


            result = result.replace(replacement.getDecoratedPlaceholder(), replacementValue);
        }

        return result;
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
        super.copyTo(copyTarget);

        ListTableRepeaterSubtemplate targetTemplate = (ListTableRepeaterSubtemplate) copyTarget;
        targetTemplate.setColumnSizes(columnSizes);
        targetTemplate.setElementFormat(elementFormat);
        targetTemplate.setIsOrderedList(isOrderedList);
        targetTemplate.setIsTable(isTable);
        targetTemplate.setTableHeader(tableHeader);
        targetTemplate.setShowTableHeaders(showTableHeaders);
        targetTemplate.setTextAlign(textAlign);
        targetTemplate.setBorders(borders);
        targetTemplate.setFontSize(fontSize);
        targetTemplate.setFontStyle(fontStyle);
        targetTemplate.setOrderByColumns(orderByColumns);
        targetTemplate.setEmptyRowsAppended(emptyRowsAppended);
    }
    

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the isTable
     */
    public boolean getIsTable() {
        return isTable;
    }

    /**
     * @param isTable the isTable to set
     */
    public void setIsTable(boolean isTable) {
        this.isTable = isTable;
    }

    /**
     * @return the tableHeader
     */
    public String getTableHeader() {
        return tableHeader + " ";
    }

    /**
     * @param tableHeader the tableHeader to set
     */
    public void setTableHeader(String tableHeader) {
        this.tableHeader = tableHeader;
    }

    /**
     * @return the elementFormat
     */
    public String getElementFormat() {
        return elementFormat + " ";
    }

    /**
     * @param elementFormat the elementFormat to set
     */
    public void setElementFormat(String elementFormat) {
        this.elementFormat = elementFormat;
    }

    /**
     * @return the orderedList
     */
    public boolean getIsOrderedList() {
        return isOrderedList;
    }

    /**
     * @param orderedList the orderedList to set
     */
    public void setIsOrderedList(boolean orderedList) {
        this.isOrderedList = orderedList;
    }

    /**
     * @return the columnSizes
     */
    public String getColumnSizes() {
        return columnSizes + " ";
    }

    /**
     * @param columnSizes the columnSizes to set
     */
    public void setColumnSizes(String columnSizes) {
        this.columnSizes = columnSizes;
    }

    /**
     * @return the showTableHeaders
     */
    public boolean getShowTableHeaders() {
        return showTableHeaders;
    }

    /**
     * @param showTableHeaders the showTableHeaders to set
     */
    public void setShowTableHeaders(boolean showTableHeaders) {
        this.showTableHeaders = showTableHeaders;
    }

    /**
     * @return the textAlign
     */
    public String getTextAlign() {
        return textAlign;
    }

    /**
     * @param textAlign the textAlign to set
     */
    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * @return the style
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * @param style the style to set
     */
    public void setFontStyle(String style) {
        this.fontStyle = style;
    }

    /**
     * @return the borders
     */
    public String getBorders() {
        return borders;
    }

    /**
     * @param borders the borders to set
     */
    public void setBorders(String borders) {
        this.borders = borders;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the orderByColumns
     */
    public String getOrderByColumns() {
        return orderByColumns;
    }

    /**
     * @param orderByColumns the orderByColumns to set
     */
    public void setOrderByColumns(String orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    /**
     * @return the numberOfEmptyRowsAppended
     */
    public int getEmptyRowsAppended() {
        return emptyRowsAppended;
    }

    /**
     * @param numberOfEmptyRowsAppended the numberOfEmptyRowsAppended to set
     */
    public void setEmptyRowsAppended(int numberOfEmptyRowsAppended) {
        this.emptyRowsAppended = numberOfEmptyRowsAppended;








    }
    // </editor-fold>
}
class TableRowComparator implements Comparator<TableRow> {

    private int[] columnIndexes;
    private NumberFormat numberParser;
    private DateFormat dateParser;

    public TableRowComparator(String indexesString) throws ParseException {


        String[] indexStrings = indexesString.split(
                ListTableRepeaterSubtemplate.COLUMN_SORTING_SEPARATOR);

        columnIndexes = new int[indexStrings.length];

        numberParser = NumberFormat.getNumberInstance();
        dateParser = DateFormat.getInstance();

        for (int i = 0; i < indexStrings.length; i++) {
            columnIndexes[i] = (int) ((Long) numberParser.parse(indexStrings[i]) - 1);
        }
    }

    @Override
    public int compare(TableRow o1, TableRow o2) {
        for (int columnIndex : columnIndexes) {
            String content1 = Strings.stripTags(o1.getCellContent(columnIndex));
            String content2 = Strings.stripTags(o2.getCellContent(columnIndex));

            // By default, we would compare the contents as strings.
            Comparable c1 = content1;
            Comparable c2 = content2;

            // We try to convert the cell contents to numbers.
            boolean numbers = true;
            try {
                c1 = (Comparable) numberParser.parse(content1);
                c2 = (Comparable) numberParser.parse(content2);
            } catch (ParseException ex) {
                // The contents werent numbers.
                numbers = false;
                c1 = content1;
                c2 = content2;
            }


            boolean dates = true;
            if (!numbers) {
                try {
                    // We try to get dates.
                    c1 = dateParser.parse(content1);
                    c2 = dateParser.parse(content2);

                } catch (ParseException ex) {
                    // Contents weren't dates.
                    dates = false;
                    c1 = content1;
                    c2 = content2;
                }
            }

            if (!numbers && !dates) {
                if (Strings.isNullOrEmpty(content1) || content1.contains("empty")) {
                    return 1;
                } else if (Strings.isNullOrEmpty(content2) || content2.contains("empty")) {
                    return -1;
                }
            }


            int result = c1.compareTo(c2);
            if (result != 0) {
                // We return the first column comparation that isnt 0;
                return result;
            }
        }

        // If we are here, all comparations were equal.
        return 0;
    }
}

class TableRow {

    List<String> cellStyles;
    List<String> cellContents;
    
    int index;
    int total;
    
    public TableRow(int index) {
        cellContents = new ArrayList<String>();
        cellStyles = new ArrayList<String>();
        
        this.index = index;      
    }

    public void addCell(String content, String width, String style) {
        cellContents.add(content);
        cellStyles.add(String.format("width:%s;%s", width, style));
    }

    public String getCellContent(int cellIndex) {
        return cellContents.get(cellIndex);
    }

    public String getCellStyles(int cellIndex) {
        return cellStyles.get(cellIndex);
    }

    public String getRow(int totalRows) {
        
        String rowClass="odd";
        if(index%2==0) {
            rowClass="even";
        }
        
        if(index ==0) {
            rowClass+=" firstRow";
        } else if (index == totalRows-1){
            rowClass+= " lastRow";
        }
        
        String row = "<tr class=\""+ rowClass +"\">";

        for (int i = 0; i < cellContents.size(); i++) {
            row += String.format("<td style=\"%s\">%s</td>",
                    cellStyles.get(i),
                    cellContents.get(i));
        }

        return row + "</tr>";
    }
}
