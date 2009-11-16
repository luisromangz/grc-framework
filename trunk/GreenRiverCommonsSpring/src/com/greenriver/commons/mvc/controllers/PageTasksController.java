package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.mvc.configuration.PageTasksConfiguration;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.pageTasks.PageTask;
import com.greenriver.commons.mvc.pageTasks.PageTaskManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a controller meant to be configured through the Spring
 * dependency inyection system, receiving descriptions of the tasks that the
 * page must undertake.
 * 
 * @author luis
 */
public class PageTasksController extends ConfigurablePageController implements PageTasksConfiguration {

    // The TaskManager object holding the tasks.
    private PageTaskManager pageTaskManager;

    public PageTasksController() {
        super();
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * Gets the <c>PageTaskManager</c> instance used by the the controller.
     *
     * @return the used pageTaskManager.
     */
    public PageTaskManager getPageTaskManager() {
        return pageTaskManager;
    }

    /**
     * Sets the
     * @param pageTaskManager the pageTaskManager to set
     */
    public void setPageTaskManager(PageTaskManager pageTaskManager) {
        this.pageTaskManager = pageTaskManager;
    }
// </editor-fold>

    @Override
    public void customHandleRequest(HttpServletRequest request,
            HttpServletResponse response, ModelAndView modelAndView) throws Exception {

        HeaderConfigurer headerConfigurer = this.getHeaderConfigurer();
        List<PageTask> allowedTasks = new ArrayList<PageTask>();

        // Configuration at the header level and the form level is done here,
        // but inclussion of the required jsp templates and other things must
        // be done in a page-task-oriented jsp file defined in the app.

        // We add all the included elements in the header configurer.
        for (PageTask pageTask : getPageTaskManager().getTasks()) {
            // We need to check if the current user has permissions to see the
            // task, if not we will not configure it. If allowed is added to
            // the list of tasks
            if (!isAllowedTaskForUser(pageTask)) {
                continue;
            } else {
                allowedTasks.add(pageTask);
            }

            // We have to add to the previously configured properties,
            // that had been configured in the page level, not the task level
            // as we are doing it now.
            configurePageProperties(pageTask, modelAndView);
        }

        // We add the tasks info to the ModelAndView object so we can access
        // it in the view so the tasks and toolbar can be rendered.
        // Be careful
        modelAndView.addObject("pageTasksInfo", allowedTasks);
    }

    /**
     * Gets if a task is allowed for the current user.
     * @param pageTask
     * @return if the current user is allowed or not.
     */
    private boolean isAllowedTaskForUser(PageTask pageTask) {
        User user = getUserSessionInfo().getCurrentUser();

        boolean result = user.hasAnyRole(pageTask.getAllowedRoles());
        return result;
    }

    private void configurePageProperties(
            PageTask pageTask,
            ModelAndView mav) throws ClassNotFoundException {

        String taskName = pageTask.getTaskName();

        // The properties that are files need to have their path relative
        // to the task's name, and inside a "js" folder.
        getPageConfiguration().getJavaScriptFiles().addAll(addPathPrefixToFileNames(
                "tasks/"+taskName,
                pageTask.getJavaScriptFiles()));

        getPageConfiguration().getCssFiles().addAll(
                addPathPrefixToFileNames(taskName, pageTask.getCssFiles()));

        getPageConfiguration().getDojoBundles().addAll(pageTask.getDojoBundles());
        getPageConfiguration().getDojoModules().addAll(pageTask.getDojoModules());
        getPageConfiguration().getDwrServices().addAll(pageTask.getDwrServices());
        getPageConfiguration().getOnLoadScripts().addAll(pageTask.getOnLoadScripts());
        getPageConfiguration().getScripts().addAll(pageTask.getScripts());

        //Forms ids are prefixed with the task name
        configureFormEntities(pageTask.getFormEntities(), mav,
                pageTask.getTaskName() + "_");

        configurePropertiesView(pageTask.getPropertiesView(), mav,
                pageTask.getTaskName() + "_");
    }

  


}
