
package com.greenriver.commons.templating;

/**
 *
 * @author luis
 */
public class SubtemplatedReplacementData
        extends TemplateReplacementData
        implements SubtemplatedReplacement {
        
    private Class subtemplateBaseClass;
    private String subtemplateField;

    public SubtemplatedReplacementData(
            String placeholder,
            String description,
            Class subtemplateBaseClass,
            String subtemplateField) {
        
        super(placeholder,description);
        this.subtemplateBaseClass=subtemplateBaseClass;
        this.subtemplateField = subtemplateField;
    }

    @Override
    public String getSubtemplateField() {
        return subtemplateField;
    }

    @Override
    public String getSubtemplateBaseClassName() {
        if(this.subtemplateBaseClass==null) {
            return "No definido";
        }
        return this.subtemplateBaseClass.getSimpleName();
    }

}
