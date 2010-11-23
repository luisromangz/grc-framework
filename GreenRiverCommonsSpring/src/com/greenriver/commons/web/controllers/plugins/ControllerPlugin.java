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
