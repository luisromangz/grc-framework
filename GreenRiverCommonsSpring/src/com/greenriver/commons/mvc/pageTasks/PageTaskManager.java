
package com.greenriver.commons.mvc.pageTasks;

import com.greenriver.commons.collections.SortedArrayList;
import java.util.List;
/**
 *
 * @author luis
 */
public class PageTaskManager {

    // The tasks to be managed, which will be ordered by the criteria defined
    // for the PageTaks instances.
    private SortedArrayList<PageTask> tasks;

    public PageTaskManager () {
        tasks = new SortedArrayList<PageTask>();
    }

    /**
     * @return the tasks
     */
    public // The tasks to be managed.
    List<PageTask> getTasks() {
        return tasks;
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<PageTask> tasks) {
        this.tasks = new SortedArrayList<PageTask>();
        this.tasks.addAll(tasks);
    }

    public void addTask(PageTask task) {
        this.tasks.add(task);
    }


}
