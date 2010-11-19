

package com.greenriver.commons.web.controllers;

import com.greenriver.commons.Strings;
import com.greenriver.commons.mvc.pageTasks.PageTask;
import com.greenriver.commons.mvc.pageTasks.PageTaskManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 *A controller used to load each task
 * @author luis
 */
public class PageTaskController extends ConfigurablePageController{

    private PageTaskManager taskManager;

    @Override
    public void customHandleRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) throws Exception {       
        

        String taskName = request.getParameter("taskName");
        if(Strings.isNullOrEmpty(taskName)) {
            mav.addObject("errorMessage","El nombre de la tarea no puede ser vacío.");
            return;
        }

        PageTask pageTask = taskManager.getTasks(taskName);
        if(pageTask==null){
            mav.addObject("errorMessage","La tarea indicada no esta registrada en el gestor.");
            return;
        }

        this.configureForms(pageTask.getForms(), mav, taskName+"_");
        this.configurePropertiesView(pageTask.getPropertiesView(), mav, taskName+"_");

        mav.addObject("usedByTaskController",true);

        mav.addObject("pageTaskInfo", pageTask);
    }

    /**
     * @return the taskManager
     */
    public PageTaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * @param taskManager the taskManager to set
     */
    public void setTaskManager(PageTaskManager taskManager) {
        this.taskManager = taskManager;
    }


}
