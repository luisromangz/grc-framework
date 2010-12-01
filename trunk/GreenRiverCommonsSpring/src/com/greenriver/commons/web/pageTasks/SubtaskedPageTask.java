package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.web.configuration.PageTasksConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a PageTask that will be formed by several subtasks.
 * @author luisro
 */
public class SubtaskedPageTask 
    extends DojoHandledPageTask
    implements PageTasksConfig {

    List<PageTask> pageTasks;

    public SubtaskedPageTask() {
        super();

        pageTasks = new ArrayList<PageTask>();
    }

    @Override
    public String getContainerFile() {
        return "subtaskedPageTask.jsp";
    }
    

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    @Override
    public List<PageTask> getPageTasks() {
        return pageTasks;
    }

    @Override
    public void setPageTasks(List<PageTask> pageTasks) {
        this.pageTasks = pageTasks;
    }
    // </editor-fold>

}
