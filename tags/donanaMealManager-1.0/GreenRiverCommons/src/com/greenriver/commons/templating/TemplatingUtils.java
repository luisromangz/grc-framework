package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;

/**
 *
 * @author luis
 */
public class TemplatingUtils {

    public static boolean isSubtemplatedReplacement(TemplateReplacement replacement) {
        return (replacement instanceof SubtemplatedReplacement);
    }

    public static String formatTemplateReplacement(TemplateReplacement replacement, String value) {
        if (value == null) {
            return "No definido";
        }

        value = value.trim();

        if (Strings.isNullOrEmpty(value)) {
            // We dont' return the empty string so, e.g. an empty cell doesn't
            // desappear.
            return "<!--Not empty-->";
        }

        if (isSubtemplatedReplacement(replacement)
                && ((SubtemplatedReplacement) replacement).getSubtemplateField() != null
                && !((SubtemplatedReplacement) replacement).getSubtemplateBaseClassName().equals(ListTableRepeaterSubtemplate.class.getSimpleName())
                && !((SubtemplatedReplacement) replacement).getSubtemplateBaseClassName().equals(ListTableInnerRepeaterSubtemplate.class.getSimpleName())) {
            String element = "div";

            return String.format("<%s class=\"%s\">%s</%s>",
                    "div",
                    replacement.getPlaceholder(),
                    value, element);
        } else {
            return value;
        }


    }
}
