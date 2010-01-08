/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

/**
 *
 * @author luis
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PrintingTemplate<T extends TemplateReplacement>
        implements Serializable, Template<T, PrintableDocument>{

    @FieldProperties(label = "Cuerpo del documento", type = FieldType.RICHTEXT)
    @Column(length=2048)
    private String body;

    @Column(length=2048)
    private String cssStyles;
    @OneToOne(cascade=CascadeType.ALL)
    private PageConfiguration pageConfiguration;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public PrintableDocument fillTemplate(Map<T, String> replacements) {
        String documentBody = new String(body);
        for (T replacement : replacements.keySet()) {
            String replacementValue = replacements.get(replacement);
            if(replacementValue ==null){
                replacementValue="";
            }

            documentBody = documentBody.replace(
                    replacement.getPlaceholder(),
                    replacementValue);
        }

        PrintableDocument document = new PrintableDocument();
        document.setBody(documentBody);
        document.setCssStyles(this.getCssStyles());
        document.setPageConfiguration(this.getPageConfiguration());

        return document;
    }

    @Override
    public void copyTo(Template copyTarget) {
       PrintingTemplate targetTemplate = (PrintingTemplate) copyTarget;
       targetTemplate.setBody(this.getBody());
       targetTemplate.setCssStyles(cssStyles);
       this.getPageConfiguration().copyTo(targetTemplate.getPageConfiguration());
    }

    

    // <editor-fold defaultstate="collapsed" desc="Auto generated stuff">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrintingTemplate)) {
            return false;
        }
        PrintingTemplate other = (PrintingTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.greenriver.commons.templating.PrintingTemplate[id=" + id + "]";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the pageConfiguration
     */
    public PageConfiguration getPageConfiguration() {
        return pageConfiguration;
    }

    /**
     * @param pageConfiguration the pageConfiguration to set
     */
    public void setPageConfiguration(PageConfiguration pageConfiguration) {
        this.pageConfiguration = pageConfiguration;
    }

    /**
     * @return the cssStyles
     */
    public String getCssStyles() {
        return cssStyles;
    }

    /**
     * @param cssStyles the cssStyles to set
     */
    public void setCssStyles(String cssStyles) {
        this.cssStyles = cssStyles;
    }

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
    // </editor-fold>
}
