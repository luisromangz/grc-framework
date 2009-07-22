package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.data.model.User;
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
public class PageTasksController extends ConfigurablePageController {

    // The TaskManager object holding the tasks.
    private PageTaskManager pageTaskManager;

    public PageTasksController() {
	super();
    }

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
	    configurePageProperties(pageTask, headerConfigurer, modelAndView);
	}

	// The configuration was done in the call to super.handleRequestInternal,
	// but must be redone now as we have to add all the things we have included
	// for the tasks.
	headerConfigurer.configure(modelAndView);

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

    private void configurePageProperties(PageTask pageTask,
	    HeaderConfigurer headerConfigurer,
	    ModelAndView mav) throws ClassNotFoundException {

	String taskName = pageTask.getTaskName();

	// The properties that are files need to have their path relative
	// to the task's name.
	headerConfigurer.getJavaScriptFiles().addAll(
		addTaskNameToFileNames(taskName, pageTask.getJavaScriptFiles()));

	headerConfigurer.getCssFiles().addAll(
		addTaskNameToFileNames(taskName, pageTask.getCssFiles()));

	headerConfigurer.getDojoBundles().addAll(
		addTaskNameToFileNames(taskName, pageTask.getDojoBundles()));

	headerConfigurer.getDojoModules().addAll(pageTask.getDojoModules());
	headerConfigurer.getDwrServices().addAll(pageTask.getDwrServices());
	headerConfigurer.getOnLoadScripts().addAll(pageTask.getOnLoadScripts());
	headerConfigurer.getScripts().addAll(pageTask.getScripts());

//	// We add the forms defined in the task.
//	for (String formId : pageTask.getFormEntities().keySet()) {
//	    // Form id creation: e.g. taskName-id.
//	    String entityClassName = pageTask.getFormEntities().get(formId);
//	    Class entityClass = Class.forName(entityClassName);
//
//	    getFormBuilder().addForm(
//		    String.format("%s_%s", pageTask.getTaskName(), formId),
//		    mav);
//	    getFormBuilder().addFieldsFromModel(entityClass);
//	}

	//Forms ids are prefixed with the task name
	configureFormEntities(pageTask.getFormEntities(), mav,
		pageTask.getTaskName() + "_");

	configurePropertiesView(pageTask.getPropertiesView(), mav,
		pageTask.getTaskName() + "_");
    }

    private List<String> addTaskNameToFileNames(String taskName,
	    List<String> filenames) {

	ArrayList<String> taskedFileNames = new ArrayList<String>();

	for (String fileName : filenames) {
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
