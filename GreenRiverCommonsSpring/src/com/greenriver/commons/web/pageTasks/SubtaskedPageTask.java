package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.web.configuration.PageTasksContainer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, String> getGrids() {
        Map<String, String> grids = new HashMap<String, String>();
        Map<String, String> ownGrids = super.getGrids();

        grids.putAll(ownGrids);


        for (PageTask t : this.getPageTasks()) {
            addAndPrefixKeys(t.getTaskName(), t.getGrids(), grids);
        }

        return grids;
    }

    @Override
    public Map<String, String> getForms() {
        Map<String, String> forms = new HashMap<String, String>();
        Map<String, String> ownForms = super.getForms();

        forms.putAll(ownForms);


        for (PageTask t : this.getPageTasks()) {
            addAndPrefixKeys(t.getTaskName(), t.getForms(), forms);
        }

        return forms;
    }

    @Override
    public Map<String, String> getPropsViews() {
        Map<String, String> propViews = new HashMap<String, String>();
        Map<String, String> ownPropsViews = super.getGrids();

        propViews.putAll(ownPropsViews);


        for (PageTask t : this.getPageTasks()) {
            addAndPrefixKeys(t.getTaskName(), t.getPropsViews(), propViews);
        }

        return propViews;
    }
    

    private void addAndPrefixKeys(String prefix, Map<String, String> source, Map<String, String> result) {
        for (String key : source.keySet()) {
            result.put(prefix + "_" + key, source.get(key));
        }
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
