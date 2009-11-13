/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.controllers.plugins;

import com.greenriver.commons.mvc.configuration.PageConfiguration;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luis
 */
public interface ControllerPlugin {
    public void doWork(HttpServletRequest request, PageConfiguration configuration);
}
