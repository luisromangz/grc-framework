package com.greenriver.commons.templating;

import com.greenriver.commons.Strings;
import java.util.Arrays;
import java.util.List;
import org.apache.bcel.generic.INSTANCEOF;

/**
 *
 * @author luis
 */
public class TemplatingUtils {

    public static boolean isSubtemplatedReplacement(TemplateReplacement replacement) {
        return (replacement instanceof SubtemplatedReplacement);
    }


    public static String formatTemplateReplacement(TemplateReplacement replacement, String value) {
        if(value==null) {
            return "No definido";
        }

        if(Strings.isNullOrEmpty(value)) {
            // We dont' return the empty string so, e.g. an empty cell doesn't
            // desappear.
            return " ";
        }

        String element="span";
        if(isSubtemplatedReplacement(replacement)
                && ((SubtemplatedReplacement)replacement).getSubtemplateField()!=null){
            element="div";
        }

        return String.format("<%s class=\"%s\">%s</span>",
                element,
                replacement.getPlaceholder(),
                value);
    }

}
