

package com.greenriver.commons.web.controllers;

import com.greenriver.commons.Strings;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.configuration.PageTasksContainer;
import com.greenriver.commons.web.pageTasks.PageTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *A controller used to load each task
 * @author luis
 */
public class PageTaskController
    extends ConfigurablePageController{

    private PageTasksContainer tasksContainer;

    @Override
    public void customConfiguration(
            HttpServletRequest request,
            HttpServletResponse response,
            PageConfig configuration,
            ModelAndView mav) throws Exception {
        super.customConfiguration(request, response, configuration, mav);

        String taskName = request.getParameter("taskName");
        if(Strings.isNullOrEmpty(taskName)) {
            mav.addObject("errorMessage","El nombre de la tarea no puede ser vacío.");
            return;
        }

        PageTask pageTask = tasksContainer.getTask(taskName);
        if(pageTask==null){
            mav.addObject("errorMessage","La tarea indicada no esta registrada en el gestor.");
            return;
        }
        
        if(!pageTask.isAllowedForUser(this.getUserSessionInfo().getCurrentUser())) {
            throw new IllegalAccessException("User is not authorized for the task "+pageTask.getTaskName());
        }

        this.configureForms(pageTask.getForms(), mav, configuration, taskName+"_");
        this.configurePropertiesView(pageTask.getPropsViews(), mav, taskName+"_");
        this.configureGrids(pageTask.getGrids(), mav, configuration, taskName+"_");

        mav.addObject("usedByTaskController",true);

        mav.addObject("taskInfo", pageTask);
        mav.addObject("taskName", taskName);
    }

    
    /**
     * @return the tasksContainer
     */
    public PageTasksContainer getTasksContainer() {
        return tasksContainer;
    }

    /**
     * @param tasksContainer the tasksContainer to set
     */
    public void setTasksContainer(PageTasksContainer tasksContainer) {
        this.tasksContainer = tasksContainer;
    }



}
