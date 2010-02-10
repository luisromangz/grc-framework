
package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public interface Subtemplate<T extends TemplateReplacement, R,K> extends Copieable<Subtemplate> {
    R fillTemplate(K source);
    List<T> getTemplateReplacements();

    SubtemplatedReplacement getSubtemplatedReplacement();
}
