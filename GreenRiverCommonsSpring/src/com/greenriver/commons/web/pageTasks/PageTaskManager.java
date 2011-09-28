
package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.SortedArrayList;
import java.util.ArrayList;
import java.util.List;
/**
 * Holds the tags and allows retrieval by task name.
 * @author luis
 */
public class PageTaskManager {

    // The tasks to be managed, which will be ordered by the criteria defined
    // for the PageTaks instances.
    private List<PageTask> tasks;
    private boolean sortTasks=false;

    public PageTaskManager () {
        tasks = new ArrayList<PageTask>();
    }

    /**
     * @return the tasks
     */
    public List<PageTask> getTasks() {
        if(getSortTasks()){
            return new SortedArrayList<PageTask>(tasks);
        } else {
            return tasks;
        }        
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<PageTask> tasks) {
        this.tasks=tasks;
    }


    public PageTask getTask(String taskName) {
        for(PageTask t : tasks) {
            if(t.getTaskName().equals(taskName)) {
                return t;
            }
        }
        
        throw new IndexOutOfBoundsException("Task with name '"+taskName+"' not found!");
    }


    public void addTask(PageTask task) {
        if(Strings.isNullOrEmpty(task.getTaskName())){
            throw new IllegalArgumentException("Page task has no name");
        }

        if(tasks.contains(task)) {
            // PageTask's equals compares by name.
            throw new IllegalArgumentException(
                    "Duplicate task name "+task.getTaskName());
        }

        this.tasks.add(task);
    }

    /**
     * @return the sortTasks
     */
    public boolean getSortTasks() {
        return sortTasks;
    }

    /**
     * @param sortTasks the sortTasks to set
     */
    public void setSortTasks(boolean sortTasks) {
        this.sortTasks = sortTasks;
    }

}
