/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.web.controllers.plugins;

import com.greenriver.commons.web.configuration.PageConfig;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author luis
 */
public interface ControllerPlugin {
    public void doWork(HttpServletRequest request, PageConfig configuration);
}
