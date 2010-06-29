package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author luis
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
@EntityFieldsProperties(appendSuperClassFields = true)
public abstract class ListTableRepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
                extends RepeaterSubtemplate<T, K> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public static final String TABLE_CELL_SEPARATOR = "||";
    public static final String TABLE_CELL_SEPARATOR_REGEX = "\\|\\|";
    @FieldProperties(label = "Tipo de repetición", type = FieldType.SELECTION,
    possibleValues = {"true", "false"}, possibleValueLabels = {"Tabla", "Lista"})
    private boolean isTable = true;
    @FieldProperties(label = "Lista ordenada", type = FieldType.BOOLEAN, editable = false, deactivationConditions = {
        @FieldDeactivationCondition(equals = "'true'", newValue = "false", triggerField = "isTable")
    })
    private boolean isOrderedList;
    @FieldProperties(label = "Mostrar encabezados de la tabla", type = FieldType.BOOLEAN,
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable", newValue = "false")
    })
    private boolean showTableHeaders = true;
    @FieldProperties(label = "Encabezados de la tabla", type = FieldType.LONGTEXT, required = false, widgetStyle = "width:98%",
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable"),
        @FieldDeactivationCondition(equals = "false", triggerField = "showTableHeaders")
    })
    private String tableHeader = "";
    @FieldProperties(label = "Anchuras de las columnas", type = FieldType.LONGTEXT, required = false, widgetStyle = "width:98%",
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable")})
    private String columnSizes = "";
    @FieldProperties(label = "Formato del elemento", type = FieldType.LONGTEXT, widgetStyle = "width:98%")
    private String elementFormat;
    @FieldProperties(label = "Estilo del elemento", type = FieldType.SELECTION,
    possibleValueLabels = {"Normal", "Negrita", "Cursiva", "Negrita y cursiva"},
    possibleValues = {" ", "font-weight:bold", "font-style:italic", "font-weight:bold;font-style:italic"})
    private String fontStyle = " ";
    @FieldProperties(label = "Tamaño de la fuente", type = FieldType.NUMBER, minValue = 4, unit = "pt")
    private int fontSize = 9;
    @FieldProperties(label = "Alineación del texto en la celda", type = FieldType.SELECTION,
    possibleValueLabels = {"Izquierda", "Centro", "Derecha"},
    possibleValues = {"left", "center", "right"},
    deactivationConditions = {
        @FieldDeactivationCondition(triggerField = "isTable", equals = "'false'")})
    private String textAlign = "center";
    @FieldProperties(label = "Bordes", type = FieldType.SELECTION,
    possibleValueLabels = {"Todos", "Horizontales", "Verticales"},
    possibleValues = {"border-top,border-bottom,border-left,border-right", "border-top,border-bottom", "border-left,border-right"},
    deactivationConditions = {
        @FieldDeactivationCondition(triggerField = "isTable", equals = "'false'")})
    private String borders = "border-top,border-bottom,border-left,border-right";
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

        List<String> sizes = new ArrayList<String>(Arrays.asList(this.getColumnSizes().split(
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
                result += String.format("<th style=\"%s\">%s</th>", elementStyle, splitHeader[i]);
            }

            result += "</tr></thead>";
        }

        result += "<tbody>";




        // We walk the replacements for the table rows.
        for (Map<T, String> elementReplacements : replacements) {

            String elementString = this.fillTemplateAux(elementFormat, elementReplacements);

            String[] columnElements = elementString.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            while (sizes.size() < columnElements.length) {
                sizes.add("auto");
            }

            for (int i = 0; i < columnElements.length; i++) {
                columnElements[i] = String.format(
                        "<td style=\"width:%s;%s\">%s</td>",
                        sizes.get(i),
                        elementStyle,
                        columnElements[i]);
            }

            result += String.format("<tr>%s</tr>", Strings.join(columnElements, ""));
        }

        result += "</tbody></table>";

        return result;

    }

    private String elementStyle() {
        String computedBorders = "";
        if (isTable) {
            for (String border : this.borders.split(",")) {
                computedBorders += border + ":0.5mm solid black;";
            }
        }

        return String.format("font-size:%spt;%s;text-align:%s;%s",
                this.fontSize,
                computedBorders,
                this.textAlign,
                this.fontStyle);
    }

    private String fillTemplateAux(String formatString, Map<T, String> replacements) {
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
    // </editor-fold>
}
