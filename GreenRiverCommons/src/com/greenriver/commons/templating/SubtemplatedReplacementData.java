
package com.greenriver.commons.templating;

/**
 *
 * @author luis
 */
public class SubtemplatedReplacementData
        extends TemplateReplacementData
        implements SubtemplatedReplacement {
        
    private String editionClientCallback;
    private String subtemplateField;

    public SubtemplatedReplacementData(
            String placeholder,
            String description,
            String editionClientCallback,
            String subtemplateField) {
        
        super(placeholder,description);
        this.editionClientCallback=editionClientCallback;
        this.subtemplateField = subtemplateField;
    }

    @Override
    public String getEditionClientCallback() {
        return editionClientCallback;
    }

    @Override
    public String getSubtemplateField() {
        return subtemplateField;
    }

}