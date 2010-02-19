/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
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
@EntityFieldsProperties(appendSuperClassFields=true)
public abstract class TextRepeaterSubtemplate
        extends RepeaterSubtemplate<TemplateReplacement, Collection<?>>implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @FieldProperties(label="Texto que se repite",type=FieldType.RICHTEXT)
    private String body;

    @FieldProperties(label="Añadir nueva línea tras el texto", type=FieldType.BOOLEAN)
    private boolean newLineAfterText = true;
  
    @Override
    protected String fillTemplatesInternal(List<Map<TemplateReplacement, String>> replacements) {
        String result="";

        for(Map<TemplateReplacement,String> elementReplacement: replacements) {
            String elementResult = body;

            for(TemplateReplacement replacement : elementReplacement.keySet()) {
                elementResult = elementResult.replace(
                        replacement.getDecoratedPlaceholder(),
                        elementReplacement.get(replacement));
            }

            result+=elementResult;

            if(newLineAfterText) {
                result+="<br>";
            }
        }

        return result;
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
        super.copyTo(copyTarget);

        ((TextRepeaterSubtemplate)copyTarget).body = this.body;
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
    // </editor-fold>
}
