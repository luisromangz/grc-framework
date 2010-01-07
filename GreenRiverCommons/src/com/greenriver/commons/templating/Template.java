package com.greenriver.commons.templating;

import com.greenriver.commons.Copieable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public interface Template<T extends TemplateReplacement, R> extends Copieable<Template> {
    R fillTemplate(Map<T,String> replacements);

    List<T> getTemplateReplacements();
}
