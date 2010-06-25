
package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import java.util.List;

/**
 * Interface to implement by those templates that are placed inside another
 * templates.
 * @param <T> Type of the replacement that must extends TemplateReplacement
 * @param <R> Type used to return the template with all the replacements made.
 * For example this could be an string with all the replacements done.
 * @param <K> Type of the source of data used to fill the template replacing
 * placeholders (the replacements defined) with concrete data.
 * @author luis
 */
public interface Subtemplate<T extends TemplateReplacement, R,K> extends Copieable<Subtemplate> {
    /**
     * Fills the template from the source and returns it
     * @param source Source data used to fill the template
     * @return
     */
    R fillTemplate(K source);

    /**
     * Gets all the replacements defined in the template
     * @return
     */
    T[] getTemplateReplacements();

    /**
     * Gets the replacement this subtemplate will replace in the parent template.
     * @return
     */
    SubtemplatedReplacement getSubtemplatedReplacement();
}
