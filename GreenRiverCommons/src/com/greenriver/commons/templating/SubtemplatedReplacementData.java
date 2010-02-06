
package com.greenriver.commons.templating;

/**
 *
 * @author luis
 */
public class SubtemplatedReplacementData
        extends TemplateReplacementData
        implements SubtemplatedReplacement {
        
    private String subtemplateBaseClass;
    private String subtemplateField;

    public SubtemplatedReplacementData(
            String placeholder,
            String description,
            String subtemplateBaseClass,
            String subtemplateField) {
        
        super(placeholder,description);
        this.subtemplateBaseClass=subtemplateBaseClass;
        this.subtemplateField = subtemplateField;
    }

    @Override
    public String getSubtemplateBaseClass() {
        return subtemplateBaseClass;
    }

    @Override
    public String getSubtemplateField() {
        return subtemplateField;
    }

}
