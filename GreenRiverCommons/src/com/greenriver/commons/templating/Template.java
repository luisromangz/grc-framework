package com.greenriver.commons.templating;

import java.util.List;
import java.util.Map;

/**
 *
 * @author luis
 */
public interface Template<T extends TemplateReplacement, R> {
    R fillTemplate(Map<T,String> replacements);

    List<T> getTemplateReplacements();
}
