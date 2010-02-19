package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.MappedSuperclass;

/**
 * This class implements Subtemplate in a way suitable to create
 * subtemplates aplicable to collections of elements.
 *
 * @author luis
 */
@MappedSuperclass
public abstract class RepeaterSubtemplate<T extends TemplateReplacement, K extends Collection<?>>
          implements Subtemplate<T, String, K>, Serializable {

    @FieldProperties(label = "Mensaje a mostrar si no hay elementos", widgetStyle = "width:89%")
    private String noElementsMessage;

    @Override
    public String fillTemplate(K source) {
        if (source.isEmpty() && !Strings.isNullOrEmpty(noElementsMessage)) {
            return noElementsMessage;
        }


        List<Map<T, String>> replacements = this.createReplacements(source);

        return this.fillTemplatesInternal(replacements);
    }

    protected abstract String fillTemplatesInternal(List<Map<T, String>> replacements);

    protected abstract List<Map<T, String>> createReplacements(K source);

    @Override
    public void copyTo(Subtemplate copyTarget) {
        ((RepeaterSubtemplate)copyTarget).noElementsMessage=this.noElementsMessage;
    }

    

    /**
     * @return the noElementsMessage
     */
    public String getNoElementsMessage() {
        return noElementsMessage;
    }

    /**
     * @param noElementsMessage the noElementsMessage to set
     */
    public void setNoElementsMessage(String noElementsMessage) {
        this.noElementsMessage = noElementsMessage;
    }
}
