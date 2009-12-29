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
public abstract class RepeaterSubtemplate<T extends TemplateReplacement> 
        implements Template<T,String>,Serializable {

    public static final String TABLE_CELL_SEPARATOR="||";

    @FieldProperties(label="Tipo de repetición", type=FieldType.SELECTION,
        possibleValues={"true","false"}, possibleValueLabels={"Tabla","Lista"})
    private boolean isTable;

    @FieldProperties(label="Encabezados de la tabla", required=false, widgetWidth="98%",
    deactivationConditions={@FieldDeactivationCondition(equals="'false'",newValue="''",triggerField="isTable")})
    private String tableHeader;

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

    public String fillTemplates(List<Map<T,String>> replacements){
        String result = "<ul>";
        
        if(isTable) {
            result="<table><thead><tr><th>";
            String[] split = tableHeader.split(RepeaterSubtemplate.TABLE_CELL_SEPARATOR);

            result +=Strings.join(Arrays.asList(split), "</th><th>");

            result+="</th></tr></thead><tbody>";
        }

        for(Map<T,String> elementReplacement : replacements) {
            result+=String.format(isTable?"<tr><td>${0}</td></tr>":"<li>${0}</li>",
                    this.fillTemplate(elementReplacement).replace(
                        RepeaterSubtemplate.TABLE_CELL_SEPARATOR,
                        isTable?"</td><td>":""));
        }

        result+=isTable?"</tbody>":"</ul>";

        return result;

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
    // </editor-fold>

}
