package com.greenriver.commons.templating;

import com.greenriver.commons.data.fieldProperties.WidgetProps;
import com.greenriver.commons.data.fieldProperties.FieldType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
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
@DiscriminatorColumn(length = 255)
public abstract class PrintingTemplate<T extends TemplateReplacement, K>
        implements Serializable, Template<T, PrintableDocument, K>, Subtemplateable {

    public static final String PAGE_BREAK = "<div style=\"page-break-after:always\"><!-- Non empty --></div>";
    @WidgetProps(label = "Cuerpo del documento", type = FieldType.RICHTEXT)
    @Column(length = 10240)
    private String body;
    @Column(length = 10240)
    private String cssStyles;
    @OneToOne(cascade = CascadeType.ALL)
    private PageConfiguration pageConfiguration;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public final PrintableDocument fillTemplate(K source, Map<T, String> externalReplacements) {
        return this.fillTemplate("", source, externalReplacements);
    }

    public final PrintableDocument fillTemplate(
            String documentTitle, K source,
            Map<T, String> externalReplacements) {

        Map<T, String> replacements = new HashMap<T, String>();

        this.createReplacements(replacements, source);

        if (externalReplacements != null) {
            replacements.putAll(externalReplacements);
        }

        PrintableDocument document = new PrintableDocument(documentTitle);
        this.fillTemplateAux(document, replacements);
        return document;
    }

    protected abstract void createReplacements(Map<T, String> replacements, K source);

    private void fillTemplateAux(
            PrintableDocument document,
            Map<T, String> replacements) {
        String documentBody = new String(body);

        String styles = this.getCssStyles();
        for (T replacement : replacements.keySet()) {
            String replacementValue = TemplatingUtils.formatTemplateReplacement(
                    replacement,
                    replacements.get(replacement));

            documentBody = documentBody.replace(
                    replacement.getDecoratedPlaceholder(),
                    replacementValue);


            styles = styles.replaceAll(replacement.getDecoratedPlaceholder(), replacementValue);
        }

        document.setBody(documentBody);




        document.setCssStyles(styles);
        document.setPageConfiguration(getPageConfiguration().fillTemplate(replacements));
    }

    @Override
    public void copyTo(Template copyTarget) {
        PrintingTemplate targetTemplate = (PrintingTemplate) copyTarget;
        targetTemplate.setBody(this.getBody());
        targetTemplate.setCssStyles(cssStyles);
        this.getPageConfiguration().copyTo(targetTemplate.getPageConfiguration());

        copySubtemplatesTo(copyTarget);
    }

    protected abstract void copySubtemplatesTo(Template copyTarget);

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
