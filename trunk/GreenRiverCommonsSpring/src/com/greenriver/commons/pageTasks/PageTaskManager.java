
package com.greenriver.commons.pageTasks;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class PageTaskManager {

    // The tasks to be managed.
    private List<PageTaskInfo> tasks;

    public PageTaskManager () {
        tasks = new ArrayList<PageTaskInfo>();
    }

    /**
     * @return the tasks
     */
    public // The tasks to be managed.
    List<PageTaskInfo> getTasks() {
        return tasks;
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<PageTaskInfo> tasks) {
        this.tasks = tasks;
    }

    public void addTask(PageTaskInfo task) {
        this.tasks.add(task);
    }


}
