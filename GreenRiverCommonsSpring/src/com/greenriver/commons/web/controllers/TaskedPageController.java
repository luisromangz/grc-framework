package com.greenriver.commons.web.controllers;

import com.greenriver.commons.web.configuration.PageTasksContainer;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.pageTasks.PageTask;
import com.greenriver.commons.web.pageTasks.PageTaskManager;
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
public class TaskedPageController
        extends DojoHandledPageController
        implements PageTasksContainer {

    // The TaskManager object holding the tasks.
    private PageTaskManager pageTaskManager;

    public TaskedPageController() {
        super();

        this.setDojoControllerModule("grc.controllers.TaskedPageController");

        pageTaskManager = new PageTaskManager();
    }

    @Override
    public void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView modelAndView) throws Exception {
        super.customHandleRequest(request, response, configuration, modelAndView);

        List<PageTask> allowedTasks = new ArrayList<PageTask>();

        // Configuration at the header level and the form level is done here,
        // but inclussion of the required jsp templates and other things must
        // be done in a page-task-oriented jsp file defined in the app.


        // We add all the included elements in the header configurer.
        User user = getUserSessionInfo().getCurrentUser();
        for (PageTask pageTask : this.getPageTasks()) {
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
            configurePageTask(pageTask, configuration, modelAndView);
        }

        // We add the tasks info to the ModelAndView object so we can access
        // it in the view so the tasks and toolbar can be rendered.
        // Be careful
        modelAndView.addObject("pageTasksInfo", allowedTasks);

    }

    private void configurePageTask(
            PageTask pageTask,
            PageConfig configuration,
            ModelAndView mav) throws ClassNotFoundException {

        pageTask.configurePage(configuration, mav);

        // If the load of the task is on demand, we add global functions intended for loading this.
        configuration.addOnLoadScript(String.format(
                "window['%s_onLoad']=function(){",
                pageTask.getTaskName()));
        configuration.getOnLoadScripts().addAll(pageTask.getOnLoadScripts());

        //Forms ids are prefixed with the task name
        configureForms(pageTask.getForms(), mav,pageTask.getTaskName() + "_");

        configuration.addOnLoadScript(String.format(
                "} // End of %s.onLoad function",
                pageTask.getTaskName()));


        configurePropertiesView(pageTask.getPropertiesView(), mav,
                pageTask.getTaskName() + "_");

    }

    @Override
    public List<PageTask> getPageTasks() {
        return pageTaskManager.getTasks();
    }

    @Override
    public void setPageTasks(List<PageTask> pageTasks) {
        pageTaskManager.setTasks(pageTasks);
    }

    @Override
    public PageTask getTask(String taskName) {
        return pageTaskManager.getTask(taskName);
    }
}
