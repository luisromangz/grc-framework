package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public interface Template<T extends TemplateReplacement, R,K> extends Copieable<Template> {
    R fillTemplate(K source, Map<T,String> externalReplacements);

    List<T> getTemplateReplacements();
}
