
package com.greenriver.commons.mvc.pageTasks;

import com.greenriver.commons.mvc.configuration.FormsConfiguration;
import com.greenriver.commons.mvc.configuration.PageConfiguration;
import com.greenriver.commons.mvc.configuration.PropertiesViewConfiguration;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
import java.util.List;
import java.util.Map;

/**
 * Instances of this class hold the information about a task that will be
 * included, for example, in a web page, in regards to its own properties
 * and requeriments, as for example he JavaScript files it needs, the
 * DWR services that uses, etc.
 * @author luis
 */
public class PageTask  implements FormsConfiguration, HeaderConfiguration,
        Comparable<PageTask>, PropertiesViewConfiguration {

    // The page configuration needed by the task.
    private PageConfiguration pageConfiguration;

    // The roles that are required to show the task.
    private String[] allowedRoles = {"ROLE_USER"};

    // The name of the main JSP file.
    private String mainJspFileName;

    // The name's task;
    private String taskName;

    // The JSP file used to render the contents of the tasks contextual toolbar.
    private String toolbarJspFileName;

    // Tells if the task is the start one, which provides access to the other
    // tasks.
    private boolean isStartTask;

    // The image that is shown as icon for the task in the taskSelector.
    private String imageFileName;

    // A function name that must be called when showing a task to re initialize
    // it.
    private String taskResetCallback;

    public PageTask() {
        pageConfiguration = new PageConfiguration();
        taskResetCallback = "false";
    }

    public void addFormEntity(String id,String entityName) {
        pageConfiguration.addFormEntity(id, entityName);
    }

    public Map<String,String> getFormEntities() {
        return pageConfiguration.getFormEntities();
    }

    public void setFormEntities(Map<String,String> formEntities) {
        pageConfiguration.setFormEntities(formEntities);
    }

    public void addCssFile(String cssFilename) {
        pageConfiguration.addCssFile(cssFilename);
    }

    public void addDwrService(String name) {
        pageConfiguration.addDwrService(name);
    }

    public void addDojoBundle(String bundleName) {
        pageConfiguration.addDojoBundle(bundleName);
    }

    public void addDojoModule(String dojoModule) {
        pageConfiguration.addDojoModule(dojoModule);
    }

    public void addJavaScriptFile(String jsFilename) {
        pageConfiguration.addJavaScriptFile(jsFilename);
    }

    public void addOnLoadScript(String code) {
        pageConfiguration.addOnLoadScript(code);
    }

    public void addScript(String script) {
        pageConfiguration.addScript(script);
    }

    public List<String> getCssFiles() {
        return pageConfiguration.getCssFiles();
    }

    public List<String> getDojoModules() {
        return pageConfiguration.getDojoModules();
    }

    public List<String> getDojoBundles() {
        return pageConfiguration.getDojoBundles();
    }

    public List<String> getDwrServices() {
        return pageConfiguration.getDwrServices();
    }

    public List<String> getJavaScriptFiles() {
        return pageConfiguration.getJavaScriptFiles();
    }

    public List<String> getOnLoadScripts() {
        return pageConfiguration.getOnLoadScripts();
    }

    public List<String> getScripts() {
        return pageConfiguration.getScripts();
    }

    public String getTitle() {
        return pageConfiguration.getTitle();
    }

    public void setTitle(String title) {
        pageConfiguration.setTitle(title);
    }

    public void setCssFiles(List<String> cssFiles) {
        pageConfiguration.setCssFiles(cssFiles);
    }

    public void setDwrServices(List<String> dwrServices) {
        pageConfiguration.setDwrServices(dwrServices);
    }

    public void setDojoBundles(List<String> dojoBundles) {
        pageConfiguration.setDojoBundles(dojoBundles);
    }

    public void setDojoModules(List<String> dojoModules) {
        pageConfiguration.setDojoModules(dojoModules);

    }

    public void setJavaScriptFiles(List<String> javascriptFiles) {
        pageConfiguration.setJavaScriptFiles(javascriptFiles);
    }

    public void setOnLoadScripts(List<String> onLoadScripts) {
        pageConfiguration.setOnLoadScripts(onLoadScripts);
    }

    public void setScripts(List<String> scripts) {
        pageConfiguration.setScripts(scripts);
    }

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
     * Defines an order for PageTasks based on the startTask attribute.
     * @param o Another PageTask to compare.
     * @return 0 if both tasks have the same startTask value; -1 if this page
     * task object is the start task and the other isn't, and 1 if this page task
     * is not the start one, and the other is.
     */
    public int compareTo(PageTask o) {
        if(this.isStartTask == o.isStartTask) {
            return 0;
        } else if(this.isStartTask) {
            return -1;
        } else {
            return 1;
        }

    }

    /**
     * @return the startTask
     */
    public boolean getIsStartTask() {
        return isStartTask;
    }

    /**
     * @param startTask the startTask to set
     */
    public void setIsStartTask(boolean startTask) {
        this.isStartTask = startTask;
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
     * @return the taskResetCallback
     */
    public String getTaskResetCallback() {
        return taskResetCallback;
    }

    /**
     * @param taskResetCallback the taskResetCallback to set
     */
    public void setTaskResetCallback(String taskResetCallback) {
        this.taskResetCallback = taskResetCallback;
    }

    public void addPropertiesView(String id, Object configuration) {
	pageConfiguration.addPropertiesView(id, configuration);
    }

    public void setPropertiesView(Map<String, Object> configuration) {
	pageConfiguration.setPropertiesView(configuration);
    }

    public Map<String, Object> getPropertiesView() {
	return pageConfiguration.getPropertiesView();
    }

    public void addDojoBundles(List<String> dojoBundles) {
        pageConfiguration.addDojoBundles(dojoBundles);
    }

    public void addDojoModules(List<String> dojoModules) {
         pageConfiguration.addDojoModules(dojoModules);
    }

}
