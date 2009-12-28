/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

import java.util.Map;

/**
 *
 * @author luis
 */
public interface Template<T extends TemplateReplacement, R> {
    R fillTemplate(Map<T,String> replacements);
}
