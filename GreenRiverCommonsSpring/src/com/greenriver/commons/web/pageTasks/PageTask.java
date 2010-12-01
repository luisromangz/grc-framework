package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.configuration.PageToolsConfig;

/**
 * Instances of this class hold the information about a task that will be
 * included, for example, in a web page, in regards to its own properties
 * and requeriments, as for example he JavaScript files it needs, the
 * DWR services that uses, etc.
 * @author luis
 */
public class PageTask
       extends PageConfig
       implements PageToolsConfig, Comparable<PageTask> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    // The roles that are required to show the task.
    private String[] allowedRoles = {"ROLE_USER"};
    // The name of the main JSP file.
    private String mainJspFileName="task.jsp";
    // The name's task;
    private String taskName;
    // The JSP file used to render the contents of the tasks contextual toolbar.
    private String toolbarJspFileName="toolbar.jsp";
    // The image that is shown as icon for the task in the taskSelector.
    private String imageFileName;
   
    private boolean loadedOnPageLoad = false;
    // </editor-fold>

    public PageTask() {
      
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != PageTask.class) {
            return false;
        }

        PageTask oTask = (PageTask) o;
        if (getTaskName() == null || oTask.getTaskName() == null) {
            return false;
        }

        return getTaskName().equals(oTask.getTaskName());
    }

    public boolean isAllowedForUser(User user) {
        boolean allowed = user.hasAnyRole(this.getAllowedRoles());
        return allowed;
    }

    /**
     * Defines an order for PageTasks based on the startTask attribute.
     * @param o Another PageTask to compare.
     * @return 0 if both tasks have the same startTask value; -1 if this page
     * task object is the start task and the other isn't, and 1 if this page task
     * is not the start one, and the other is.
     */
    @Override
    public int compareTo(PageTask o) {
        return this.getTaskName().compareTo(o.getTaskName());
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the allowedRoles
     */
    public String[] getAllowedRoles() {
        return allowedRoles;
    }

    /**
     * @param allowedRoles the allowedRoles to set
     */
    public void setAllowedRoles(String[] allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    /**
     * @return the mainJspFileName
     */
    public String getMainJspFileName() {
        return mainJspFileName;
    }

    /**
     * @param mainJspFileName the mainJspFileName to set
     */
    public void setMainJspFileName(String mainJspFileName) {
        this.mainJspFileName = mainJspFileName;
    }

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return the toolbarJspFileName
     */
    public String getToolbarJspFileName() {
        return toolbarJspFileName;
    }

    /**
     * @param toolbarJspFileName the toolbarJspFileName to set
     */
    public void setToolbarJspFileName(String toolbarJspFileName) {
        this.toolbarJspFileName = toolbarJspFileName;
    }

    /**
     * @return the imageFileName
     */
    public String getImageFileName() {
        return imageFileName;
    }

    /**
     * @param imageFileName the imageFileName to set
     */
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }    

    /**
     * @return the loadedOnPageLoad
     */
    public boolean isLoadedOnPageLoad() {
        return loadedOnPageLoad;
    }

    /**
     * @param loadedOnPageLoad the loadedOnPageLoad to set
     */
    public void setLoadedOnPageLoad(boolean loadedOnPageLoad) {
        this.loadedOnPageLoad = loadedOnPageLoad;
    }

    // </editor-fold>
}
