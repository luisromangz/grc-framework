
package com.greenriver.commons.mvc.pageTasks;

import com.greenriver.commons.Strings;
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
        for(PageTask task : tasks) {
            addTask(task);
        }
    }

    public void addTask(PageTask task) {
        if(Strings.isNullOrEmpty(task.getTaskName())){
            throw new IllegalArgumentException("Page task has no name");
        }

        if(tasks.contains(task)) {
            throw new IllegalArgumentException(
                    "Duplicate task name "+task.getTaskName());
        }

        this.tasks.add(task);
    }


}
