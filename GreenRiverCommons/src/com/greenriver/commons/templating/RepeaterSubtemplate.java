/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class RepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
        implements Template<T,String,K>,Serializable {

    public static final String TABLE_CELL_SEPARATOR="||";
    public static final String TABLE_CELL_SEPARATOR_REGEX="\\|\\|";

    @FieldProperties(label="Tipo de repetición", type=FieldType.SELECTION,
        possibleValues={"true","false"}, possibleValueLabels={"Tabla","Lista"})
    private boolean isTable;

    @FieldProperties(label="Encabezados de la tabla", required=false, widgetWidth="98%",
    deactivationConditions={@FieldDeactivationCondition(equals="'false'",triggerField="isTable")})
    private String tableHeader;

    @FieldProperties(label="Anchuras de las columnas", required=false, widgetWidth="98%",
    deactivationConditions={@FieldDeactivationCondition(equals="'false'",triggerField="isTable")})    
    private String columnSizes;

    @FieldProperties(label="Lista ordenada", type=FieldType.BOOLEAN, deactivationConditions={
        @FieldDeactivationCondition(equals="'true'",newValue="false",triggerField="isTable")
    })
    private boolean isOrderedList;

    @FieldProperties(label="Formato del elemento", widgetWidth="98%")
    private String elementFormat;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    protected String fillTemplatesAux(List<Map<T,String>> replacements){
        String result = "<ul>";
        if(isTable) {
            String[] splitHeader = tableHeader.split(RepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);

            result="<table cellspacing=\"0\"><thead><tr>";

            String[] sizes = new String[]{};
            if(!Strings.isNullOrEmpty(columnSizes)) {
                sizes = this.columnSizes.split(RepeaterSubtemplate.TABLE_CELL_SEPARATOR_REGEX);
            }

            for(int i=0; i< splitHeader.length; i++) {
                String columnSize ="auto";
                if(i< sizes.length) {
                    columnSize = sizes[i];
                }

                result+=String.format("<th style=\"width:%s\">%s</th>", columnSize,splitHeader[i]);
            }

            result+="</tr></thead><tbody>";
        }

        for(Map<T,String> elementReplacements : replacements) {
            String elementString = this.fillTemplateAux(elementReplacements);
            if(isTable) {
                elementString = elementString.replace(RepeaterSubtemplate.TABLE_CELL_SEPARATOR,"</td><td>");
            }

            result+=String.format(
                    isTable?"<tr><td>%s</td></tr>":"<li>%s</li>",
                    elementString);
        }

        result+=isTable?"</tbody></table>":"</ul>";

        return result;

    }

    private String fillTemplateAux(Map<T,String> replacements){
        String result = new String(this.elementFormat);
        for(T replacement : replacements.keySet()) {
            String replacementValue = replacements.get(replacement);
            if(replacementValue==null){
                replacementValue="";
            }

            result = result.replace(replacement.getPlaceholder(), replacementValue);
        }

        return result;
    }

    @Override
    public void copyTo(Template copyTarget) {
        RepeaterSubtemplate targetTemplate = (RepeaterSubtemplate) copyTarget;
        targetTemplate.setColumnSizes(columnSizes);
        targetTemplate.setElementFormat(elementFormat);
        targetTemplate.setIsOrderedList(isOrderedList);
        targetTemplate.setIsTable(isTable);
        targetTemplate.setTableHeader(tableHeader);
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
        return tableHeader;
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
        return elementFormat;
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
        return columnSizes;
    }

    /**
     * @param columnSizes the columnSizes to set
     */
    public void setColumnSizes(String columnSizes) {
        this.columnSizes = columnSizes;
    }
    // </editor-fold>

}
