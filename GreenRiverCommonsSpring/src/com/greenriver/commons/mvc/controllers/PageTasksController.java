
package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.pageTasks.PageTask;
import com.greenriver.commons.pageTasks.PageTaskManager;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * @author luis
 */
public class PageTasksController extends ConfigurablePageController{

    // The TaskManager object holding the tasks.
    private PageTaskManager pageTaskManager;
   

    public PageTasksController() {
        super();
    }

    @Override
    protected void customHandleRequest (HttpServletRequest request,
        HttpServletResponse response, ModelAndView modelAndView) throws Exception{

        HeaderConfigurer headerConfigurer = this.getHeaderConfigurer();

        // Configuration at the header level and the form level is done here,
        // but inclussion of the required jsp templates and other things must
        // be done in a page-task-oriented jsp file defined in the app.

        // We add all the included elements in the header configurer.
        for(PageTask pageTask : getPageTaskManager().getTasks()) {
           // We have to add to the previously configured properties,
           // that had been configured in the page level, not the task level
           // as we are doing it now.
           configurePageProperties(pageTask, headerConfigurer,modelAndView);
        }

        // The configuration was done in the call to super.handleRequestInternal,
        // but must be redone now as we have to add all the things we have included
        // for the tasks.
        headerConfigurer.configure(modelAndView);

        // We add the tasks info to the ModelAndView object so we can access
        // it in the view so the tasks and toolbar can be rendered.
        modelAndView.addObject("pageTasksInfo", getPageTaskManager().getTasks());
    }

    private void configurePageProperties(PageTask pageTask,
            HeaderConfigurer headerConfigurer,
            ModelAndView mav) throws ClassNotFoundException {
       String taskName = pageTask.getTaskName();

       // The properties that are files need to have their path relative
       // to the task's name.
       headerConfigurer.getJavaScriptFiles().addAll(
               addTaskNameToFileNames(taskName,pageTask.getJavaScriptFiles()));

       headerConfigurer.getCssFiles().addAll(
               addTaskNameToFileNames(taskName, pageTask.getCssFiles()));

       headerConfigurer.getDojoBundles().addAll(
               addTaskNameToFileNames(taskName, pageTask.getDojoBundles()));

       headerConfigurer.getDojoModules().addAll(pageTask.getDojoModules());
       headerConfigurer.getDwrServices().addAll(pageTask.getDwrServices());
       headerConfigurer.getOnLoadScripts().addAll(pageTask.getOnLoadScripts());
       headerConfigurer.getScripts().addAll(pageTask.getScripts());

        // We add the forms defined in the task.
       for(String entityName : pageTask.getFormEntities()) {
           // Form id creation: e.g. taskname-user-editform,
           // being taskName the task's name, and User the entity's.

           Class entityClass = Class.forName(entityName);

           getFormBuilder().addForm(
                   String.format("%s-%s-EditForm",
                        pageTask.getTaskName(),
                        entityClass.getSimpleName()).toLowerCase(),
                   mav);
           getFormBuilder().addFieldsFromModel(entityClass);
       }
    }

    private List<String> addTaskNameToFileNames(String taskName, 
            List<String> filenames) {

        ArrayList<String> taskedFileNames = new ArrayList<String>();

        for(String fileName : filenames) {
            taskedFileNames.add(String.format("%s/%s", taskName, fileName));
        }

        return taskedFileNames;
    }

    /**
     * @return the pageTaskManager
     */
    public PageTaskManager getPageTaskManager() {
        return pageTaskManager;
    }

    /**
     * @param pageTaskManager the pageTaskManager to set
     */
    public void setPageTaskManager(PageTaskManager pageTaskManager) {
        this.pageTaskManager = pageTaskManager;
    }
}
