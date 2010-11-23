/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.web.configuration;

import com.greenriver.commons.web.pageTasks.PageTaskManager;

/**
 *
 * @author luis
 */
public interface PageTasksConfiguration {

    /**
     * Gets the <c>PageTaskManager</c> instance used by the the controller.
     *
     * @return the used pageTaskManager.
     */
    PageTaskManager getPageTaskManager();

    /**
     * Sets the
     * @param pageTaskManager the pageTaskManager to set
     */
    void setPageTaskManager(PageTaskManager pageTaskManager);

}
