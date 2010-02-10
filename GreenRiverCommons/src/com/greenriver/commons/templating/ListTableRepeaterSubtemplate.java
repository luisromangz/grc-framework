package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
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
public abstract class ListTableRepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
            implements Subtemplate<T, String, K>, Serializable {

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
    private String tableHeader;
    @FieldProperties(label = "Anchuras de las columnas", required = false, widgetStyle = "width:98%",
    deactivationConditions = {
        @FieldDeactivationCondition(equals = "'false'", triggerField = "isTable")})
    private String columnSizes;
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
    public final String fillTemplate(K source) {

        List<Map<T, String>> replacements = this.createReplacements(source);

        return this.fillTemplatesAux(this.getSubtemplatedReplacement().getPlaceholder(), replacements);
    }

    protected abstract List<Map<T, String>> createReplacements(K source);

    private String fillTemplatesAux(String placeholder, List<Map<T, String>> replacements) {

        if (isTable) {
            return fillTableTemplates(placeholder, replacements);
        } else {
            return fillListTemplates(placeholder, replacements);
        }

    }

    private String fillListTemplates(String placeholder, List<Map<T, String>> replacements) {
        String result = "<ul class=\"" + placeholder + "\">";

        for (Map<T, String> elementReplacements : replacements) {
            String elementString = this.fillTemplateAux(elementReplacements);
            result += String.format("<li>%s</li>", elementString);
        }

        result += "</ul>";

        return result;
    }

    private String fillTableTemplates(String placeholder, List<Map<T, String>> replacements) {

        if (Strings.isNullOrEmpty(columnSizes)) {
            throw new IllegalStateException("Can't be a table and not having column sizes.");
        }

        String[] sizes = this.getColumnSizes().split(
                ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

        String result = "<table class=\"" + placeholder + "\" cellspacing=\"0\">";

        String[] splitHeader = getTableHeader().split(
                ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);
        if (this.showTableHeaders) {

            result+="<thead><tr>";

            for (int i = 0; i < splitHeader.length; i++) {
                result += String.format("<th>%s</th>", splitHeader[i]);
            }

            result+="</tr></thead>";
        }

        result += "<tbody>";


        // We walk the replacements for the table rows.
        for (Map<T, String> elementReplacements : replacements) {

            String elementString = this.fillTemplateAux(elementReplacements);

            String[] columnElements =elementString.split(
                    ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            for (int i = 0; i < columnElements.length; i++) {
                columnElements[i] = String.format(
                        "<td style=\"width:%s\">%s</td>",
                        sizes[i],
                        columnElements[i]);
            }

            result += String.format("<tr>%s</tr>",Strings.join(columnElements,""));
        }

        result += "</tbody></table>";

        return result;

    }

    private String fillTemplateAux(Map<T, String> replacements) {
        String result = new String(this.getElementFormat());
        for (T replacement : replacements.keySet()) {
            String replacementValue = replacements.get(replacement);
            if (replacementValue == null) {
                replacementValue = "Reemplazo no sustituido";
            }

            result = result.replace(replacement.getDecoratedPlaceholder(), replacementValue);
        }

        return result;
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
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
        return tableHeader+" ";
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
        return elementFormat+" ";
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
        return columnSizes+" ";
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
