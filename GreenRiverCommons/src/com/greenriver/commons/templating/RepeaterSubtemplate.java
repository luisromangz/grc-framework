package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.WidgetProps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.MappedSuperclass;

/**
 * This class implements Subtemplate in a way suitable to create
 * subtemplates aplicable to collections of elements.
 *
 * @param <T> Type of the class that provides the replacements and that extends
 * template replacement.
 * @param <K> Type of the collection of elements which this subtemplate renders
 * @author luis
 */
@MappedSuperclass
public abstract class RepeaterSubtemplate<T extends TemplateReplacement, K>
            implements Subtemplate<T, String, Collection<K>>, Serializable {

    @WidgetProps(label = "Mensaje a mostrar si no hay elementos", widgetStyle = "width:89%", required = false)
    private String noElementsMessage;

    @Override
    public String fillTemplate(Collection<K> source) {       


        List<Map<T, String>> replacements = this.createReplacements(source);

        if (replacements.isEmpty()) {
            if (!Strings.isNullOrEmpty(noElementsMessage)) {
                return noElementsMessage;
            } else {
                return "";
            }
        }

        return this.fillTemplatesInternal(replacements);
    }
    
    
    /**
     * Renders the subtemplate puting in place the replacements. This method must be able to
     * use the result of <b>List&lt;Map&lt;T, String&gt;&gt; createReplacements(K source)</b>.
     * @param replacements
     * @return
     */
    protected abstract String fillTemplatesInternal(List<Map<T, String>> replacements);

    /**
     * For each item of data in the source element this method must return a
     * map between the replacement's enumeration constants and the value that
     * will be used to replace them. This way we will have a set of replacements
     * for each item of the source parameter.
     * @param source K Source of data to fill the value of the replacements for
     * each item.
     * @return A list of maps for replacements. One map per item to draw.
     */
    private List<Map<T, String>> createReplacements(Collection<K> source) {
        List<Map<T,String>> replacements= new ArrayList<Map<T,String>>();
        
        for(K item :source) {
            Map<T,String> map = new HashMap<T,String>();
            createItemReplacements(map, item);
           replacements.add(map);
        }
        
        return replacements;
                
    }
    
    protected abstract void createItemReplacements(Map<T,String> replacements, K item);

    @Override
    public void copyTo(Subtemplate copyTarget) {
        ((RepeaterSubtemplate) copyTarget).noElementsMessage = this.noElementsMessage;
    }

    /**
     * Gets the message to be shown if the collection of elements to show is
     * empty.
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
