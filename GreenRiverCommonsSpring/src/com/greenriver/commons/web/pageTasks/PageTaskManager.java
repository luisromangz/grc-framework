
package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.SortedArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Holds the tags and allows retrieval by task name.
 * @author luis
 */
public class PageTaskManager {

    // The tasks to be managed, which will be ordered by the criteria defined
    // for the PageTaks instances.
    private HashMap<String, PageTask> tasks;

    public PageTaskManager () {
        tasks = new HashMap<String, PageTask>();
    }

    /**
     * @return the tasks
     */
    public List<PageTask> getTasks() {
        return new SortedArrayList<PageTask>(tasks.values());
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<PageTask> tasks) {
        this.tasks.clear();
        for(PageTask task : tasks) {
            addTask(task);
        }
    }


    public PageTask getTask(String taskName) {
        return this.tasks.get(taskName);
    }


    public void addTask(PageTask task) {
        if(Strings.isNullOrEmpty(task.getTaskName())){
            throw new IllegalArgumentException("Page task has no name");
        }

        if(tasks.containsKey(task.getTaskName())) {
            throw new IllegalArgumentException(
                    "Duplicate task name "+task.getTaskName());
        }

        this.tasks.put(task.getTaskName(),task);
    }   

}
