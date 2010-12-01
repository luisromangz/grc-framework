package com.greenriver.commons.web.configuration;

import com.greenriver.commons.web.pageTasks.PageTask;
import java.util.List;

/**
 * This interface defines methods used to set and access page tasks where neccessary.
 * @author luis
 */
public interface PageTasksConfig {

    List<PageTask> getPageTasks();
    void setPageTasks(List<PageTask> pageTasks);

}
