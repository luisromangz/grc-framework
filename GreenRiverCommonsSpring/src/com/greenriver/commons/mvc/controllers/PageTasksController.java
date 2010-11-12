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
    @Override
    public PageTaskManager getPageTaskManager() {
        return pageTaskManager;
    }

    /**
     * Sets the
     * @param pageTaskManager the pageTaskManager to set
     */
    @Override
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

        if (getPageTaskManager().getStartTask() == null) {
            throw new IllegalArgumentException("A start task definition is required.");
        }
        configurePageProperties(getPageTaskManager().getStartTask(), modelAndView);
        modelAndView.addObject("startTaskInfo", getPageTaskManager().getStartTask());

        // We add all the included elements in the header configurer.
        User user = getUserSessionInfo().getCurrentUser();
        for (PageTask pageTask : getPageTaskManager().getTasks()) {
            // We need to check if the current user has permissions to see the
            // task, if not we will not configure it. If allowed is added to
            // the list of tasks
            if (pageTask.isAllowedForUser(user)) {
                allowedTasks.add(pageTask);
            } else {
                continue;
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

    private void configurePageProperties(
            PageTask pageTask,
            ModelAndView mav) throws ClassNotFoundException {

        String taskName = pageTask.getTaskName();

        // The properties that are files need to have their path relative
        // to the task's name, and inside a "js" folder.
        getPageConfiguration().getJavaScriptFiles().addAll(addPathPrefixToFileNames(
                "tasks/" + taskName,
                pageTask.getJavaScriptFiles()));

        getPageConfiguration().getCssFiles().addAll(
                addPathPrefixToFileNames(taskName, pageTask.getCssFiles()));

        getPageConfiguration().getDojoBundles().addAll(pageTask.getDojoBundles());
        getPageConfiguration().getDojoModules().addAll(pageTask.getDojoModules());

        getPageConfiguration().getDwrServices().addAll(pageTask.getDwrServices());


        if (pageTask.isLoadedOnPageLoad()) {
            getPageConfiguration().getOnLoadScripts().addAll(pageTask.getOnLoadScripts());
            //Forms ids are prefixed with the task name
            configureFormEntities(pageTask.getFormEntities(), mav,
                    pageTask.getTaskName() + "_");
        } else {
            // If the load of the task is on demand, we add global functions intended for loading this.
            getPageConfiguration().addOnLoadScript(String.format(
                    "window['%s_onLoad']=function(){",
                    pageTask.getTaskName()));
            getPageConfiguration().getOnLoadScripts().addAll(pageTask.getOnLoadScripts());
            //Forms ids are prefixed with the task name
            configureFormEntities(pageTask.getFormEntities(), mav,
                    pageTask.getTaskName() + "_");
            getPageConfiguration().addOnLoadScript(String.format(
                    "} // End of %s.onLoad function",
                    pageTask.getTaskName()));
        }

        getPageConfiguration().getScripts().addAll(pageTask.getScripts());


        configurePropertiesView(pageTask.getPropertiesView(), mav,
                pageTask.getTaskName() + "_");
    }
}
