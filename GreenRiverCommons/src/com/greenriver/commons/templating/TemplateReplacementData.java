package com.greenriver.commons.templating;

/**
 *
 * @author luis
 */
public class TemplateReplacementData implements TemplateReplacement {

    private String placeHolder;
    private String description;

    private static final String PLACEHOLDER_MARK = "%";

    public TemplateReplacementData (String placeHolder, String description){
        this.placeHolder = placeHolder;
        this.description = description;
    }

    @Override
    public String getPlaceholder() {
        return placeHolder;
    }



    @Override
    public String getDecoratedPlaceholder() {
        return String.format("%s%s%s",
                PLACEHOLDER_MARK,
                this.placeHolder.toUpperCase(),
                PLACEHOLDER_MARK);
    }

    @Override
    public String getDescription() {
        return description;
    }

}
