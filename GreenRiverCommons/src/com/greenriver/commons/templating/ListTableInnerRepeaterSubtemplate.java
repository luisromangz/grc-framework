package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
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
import javax.persistence.OneToOne;

/**
 *
 * @author luis
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(length = 255)
public abstract class ListTableInnerRepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
         extends RepeaterSubtemplate<T, K>
        implements Serializable, Subtemplateable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @WidgetProps(label = "Formato del elemento",type=FieldType.LONGTEXT)
    private String elementFormat = "";
    @OneToOne
    // This must be set in the setter method for this template in the parent template.
    private ListTableRepeaterSubtemplate parentTemplate;

    @Override
    protected String fillTemplatesInternal(List<Map<T, String>> replacements) {
        if (getParentTemplate() == null) {
            throw new IllegalStateException("Parent template for a ListTableInnerRepeaterTemplate hasn't been set.");
        }

        List<String> elementStrings = Lists.apply(replacements, new ElementFormatter(this.elementFormat));

        String glue = "; ";

        if (getParentTemplate().getIsTable()) {
            glue = ListTableRepeaterSubtemplate.TABLE_CELL_SEPARATOR;
        }

        return Strings.join(elementStrings, glue);
    }

    @Override
    public void copyTo(Subtemplate copyTarget) {
        super.copyTo(copyTarget);

        ((ListTableInnerRepeaterSubtemplate) copyTarget).elementFormat = elementFormat;


    }

    public boolean isTable() {
        if(this.parentTemplate ==null) {
            return true;
        }
        return this.getParentTemplate().getIsTable();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * @return the parentTemplate
     */
    public ListTableRepeaterSubtemplate getParentTemplate() {
        return parentTemplate;
    }

    /**
     * @param parentTemplate the parentTemplate to set
     */
    public void setParentTemplate(ListTableRepeaterSubtemplate parentTemplate) {
        this.parentTemplate = parentTemplate;
    }

    // </editor-fold>
    private class ElementFormatter implements Applicable<Map<T, String>, String> {

        private String elementFormat;

        public ElementFormatter(String elementFormat) {
            this.elementFormat = elementFormat;
        }

        @Override
        public String apply(Map<T, String> element) {
            String result = elementFormat;

            for (T replacement : element.keySet()) {
                result = result.replace(
                        replacement.getDecoratedPlaceholder(),
                        TemplatingUtils.formatTemplateReplacement(replacement,element.get(replacement)));
            }

            return result;
        }
    }
}
