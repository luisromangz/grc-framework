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
    public static final String TABLE_CELL_SEPARATOR = "||";
    public static final String TABLE_CELL_SEPARATOR_REGEX = "\\|\\|";
    @FieldProperties(label = "Tipo de repetición", type = FieldType.SELECTION,
    possibleValues = {"true", "false"}, possibleValueLabels = {"Tabla", "Lista"})
    private boolean isTable;
    @FieldProperties(label = "Mostrar encabezados de la tabla", type = FieldType.BOOLEAN,
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable", newValue = "false")
    })
    private boolean showTableHeaders = true;
    @FieldProperties(label = "Encabezados de la tabla", required = false, widgetStyle = "width:98%",
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable"),
        @FieldDeactivationCondition(equals = "false", triggerField = "showTableHeaders")
    })
    private String tableHeader = "";
    @FieldProperties(label = "Anchuras de las columnas", required = false, widgetStyle = "width:98%",
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable")})
    private String columnSizes = "";
    @FieldProperties(label = "Lista ordenada", type = FieldType.BOOLEAN, deactivationConditions = {
        @FieldDeactivationCondition(equals = "'true'", newValue = "false", triggerField = "isTable")
    })
    private boolean isOrderedList;
    @FieldProperties(label = "Formato del elemento", widgetStyle = "width:98%")
    private String elementFormat;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
        String result = this.isOrderedList?"<ol>":"<ul>";

        for (Map<T, String> elementReplacements : replacements) {
            String elementString = this.fillTemplateAux(elementFormat, elementReplacements);
            result += String.format("<li>%s</li>", elementString);
        }

        result += this.isOrderedList?"</ol>":"</ul>";

        return result;
    }

    private String fillTableTemplates(List<Map<T, String>> replacements) {

        if (Strings.isNullOrEmpty(columnSizes)) {
            throw new IllegalStateException("Can't be a table and not having column sizes.");
        }

        List<String> sizes = new ArrayList<String>(Arrays.asList(this.getColumnSizes().split(
                ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX)));

        String result = "<table cellspacing=\"0\">";

        if (this.showTableHeaders) {

            // We use the first row to get the header's replacements.
            String header = fillTemplateAux(this.getTableHeader(), replacements.get(0));

            String[] splitHeader = header.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            result += "<thead><tr>";

            for (int i = 0; i < splitHeader.length; i++) {
                result += String.format("<th>%s</th>", splitHeader[i]);
            }

            result += "</tr></thead>";

           
        }

        result += "<tbody>";


        // We walk the replacements for the table rows.
        for (Map<T, String> elementReplacements : replacements) {

            String elementString = this.fillTemplateAux(elementFormat, elementReplacements);

            String[] columnElements = elementString.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

             while(sizes.size()< columnElements.length) {
                sizes.add("auto");
            }

            for (int i = 0; i < columnElements.length; i++) {
                columnElements[i] = String.format(
                        "<td style=\"width:%s\">%s</td>",
                        sizes.get(i),
                        columnElements[i]);
            }

            result += String.format("<tr>%s</tr>", Strings.join(columnElements, ""));
        }

        result += "</tbody></table>";

        return result;

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
    // </editor-fold>
}
