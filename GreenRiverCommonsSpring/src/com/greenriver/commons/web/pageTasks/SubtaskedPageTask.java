package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.web.configuration.PageTasksContainer;
import java.util.List;

/**
 * This class represents a PageTask that will be formed by several subtasks.
 * @author luisro
 */
public class SubtaskedPageTask 
    extends DojoHandledPageTask
    implements PageTasksContainer {

    PageTaskManager taskManager;

    public SubtaskedPageTask() {
        super();

        taskManager = new PageTaskManager();
        
        this.setMainJspFileName("../../subtaskedPageTask.jsp");
        this.setDojoControllerModule("grc.web.tasks.SubtaskedPageTaskController");
    }
    

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    @Override
    public List<PageTask> getPageTasks() {
        return taskManager.getTasks();
    }

    @Override
    public void setPageTasks(List<PageTask> pageTasks) {
       taskManager.setTasks(pageTasks);
    }

    @Override
    public PageTask getTask(String taskName) {
       return taskManager.getTask(taskName);
    }
    // </editor-fold>

}
