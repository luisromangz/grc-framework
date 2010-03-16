
package com.greenriver.commons.templating;

import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
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
@EntityFieldsProperties(appendSuperClassFields=true)
public abstract class TextRepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
        extends RepeaterSubtemplate<T, K>
        implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @FieldProperties(label="Texto que se repite",type=FieldType.RICHTEXT)
    @Column(length=10240)
    private String body;

     @FieldProperties(label="Página nueva tras el texto", type=FieldType.BOOLEAN)
    private boolean newPageAfterText = false;

    @FieldProperties(label="Añadir nueva línea tras el texto", type=FieldType.BOOLEAN,
    deactivationConditions={
        @FieldDeactivationCondition(triggerField="newPageAfterText",equals="'on'")
    })
    private boolean newLineAfterText = true;
  
    @Override
    protected String fillTemplatesInternal(List<Map<T, String>> replacements) {
        String result="";

        for(Map<T,String> elementReplacements: replacements) {
            String elementResult = body;

            for(T replacement : elementReplacements.keySet()) {
                elementResult = elementResult.replace(
                        replacement.getDecoratedPlaceholder(),TemplatingUtils.formatTemplateReplacement(
                        replacement,
                        elementReplacements.get(replacement)));
            }

            result+=elementResult;

            if(newPageAfterText){             
                result = "<div style=\"page-break-after:always\">"+result+"</div>";
            } else if(newLineAfterText) {
                result+="<br>";
            }
        }

        return result;
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
        super.copyTo(copyTarget);

        TextRepeaterSubtemplate templateTarget = ((TextRepeaterSubtemplate)copyTarget);
        templateTarget.body = this.body;
        templateTarget.newLineAfterText = this.newLineAfterText;
        templateTarget.newPageAfterText= this.newPageAfterText;


    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the newLineAfterText
     */
    public boolean getNewLineAfterText() {
        return newLineAfterText;
    }

    /**
     * @param newLineAfterText the newLineAfterText to set
     */
    public void setNewLineAfterText(boolean newLineAfterText) {
        this.newLineAfterText = newLineAfterText;
    }

    /**
     * @return the newPageAfterText
     */
    public boolean getNewPageAfterText() {
        return newPageAfterText;
    }

    /**
     * @param newPageAfterText the newPageAfterText to set
     */
    public void setNewPageAfterText(boolean newPageAfterText) {
        this.newPageAfterText = newPageAfterText;
    }
    // </editor-fold>
}
