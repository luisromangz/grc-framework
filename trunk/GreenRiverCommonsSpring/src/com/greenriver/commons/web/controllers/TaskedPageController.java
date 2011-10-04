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

        this.setDojoControllerModule("grc.webs.TaskedPageController");

        pageTaskManager = new PageTaskManager();
    }

    @Override
    protected void customConfiguration(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView modelAndView) throws Exception {

        List<PageTask> allowedTasks = new ArrayList<PageTask>();
        super.customConfiguration(request, response, configuration, modelAndView);
        // We add all the included elements in the header configurer.
        User user = getUserSessionInfo().getCurrentUser();
        for (PageTask pageTask : this.getPageTasks()) {
            // We need to check if the current user has permissions to see the
            // task, if not we will not configure it. If allowed is added to
            // the list of tasks
            pageTask.initialize();
            if (pageTask.isAllowedForUser(user)) {
                allowedTasks.add(pageTask);
                // We have to add to the previously configured properties,
                // that had been configured in the page level, not the task level
                // as we are doing it now.
                configurePageTask(pageTask, configuration, modelAndView);
            }


        }

        modelAndView.addObject("pageTasksInfo", allowedTasks);
    }

    private void configurePageTask(
            PageTask pageTask,
            PageConfig configuration,
            ModelAndView mav) throws ClassNotFoundException {

        pageTask.configurePage(configuration, mav);

        // We add global functions intended for loading this.
        configuration.addOnLoadScript(String.format(
                "window['%s_onLoad']=function(){",
                pageTask.getTaskName()));
        configuration.getOnLoadScripts().addAll(pageTask.getOnLoadScripts());

        // We configure forms and grids because they can add on load methods.
        // ids are prefixed with the task name
        configureForms(pageTask.getForms(), mav, configuration, pageTask.getTaskName() + "_");
        configureGrids(pageTask.getGrids(), mav, configuration, pageTask.getTaskName() + "_");

        configuration.addOnLoadScript(String.format(
                "} // End of %s.onLoad function",
                pageTask.getTaskName()));
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
