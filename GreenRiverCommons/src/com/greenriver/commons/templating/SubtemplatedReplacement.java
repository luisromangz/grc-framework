
package com.greenriver.commons.templating;

/**
 * This interface extends TemplateReplacement by adding operations required
 * when the template elements might represent subtemplates that have to
 * be edited.
 *
 * @author luis
 */
public interface SubtemplatedReplacement extends TemplateReplacement{
    String getEditionClientCallback();
    String getSubtemplateField();
}

