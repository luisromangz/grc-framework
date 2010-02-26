package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luis
 */
public class TemplatingUtils {

    public static boolean isSubtemplatedReplacement(TemplateReplacement replacement) {
        List<Class<?>> interfaces = Arrays.asList(replacement.getClass().getInterfaces());
        return interfaces.contains(SubtemplatedReplacement.class)
                && ((SubtemplatedReplacement)replacement).getSubtemplateField()!=null;
    }


    public static String formatTemplateReplacement(TemplateReplacement replacement, String value) {
        if(value==null) {
            return "No definido";
        }

        if(Strings.isNullOrEmpty(value)) {
            return "";
        }

        return String.format("<span class=\"%s\">%s</span>",
                replacement.getPlaceholder(),
                value);
    }

}
