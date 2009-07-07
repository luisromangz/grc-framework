
package com.greenriver.commons.pageTasks;

import com.greenriver.commons.mvc.controllers.FormsConfiguration;
import com.greenriver.commons.mvc.controllers.PageConfiguration;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
import java.util.List;

/**
 * Instances of this class hold the information about a task that will be
 * included, for example, in a web page, in regards to its own properties
 * and requeriments, as for example he JavaScript files it needs, the
 * DWR services that uses, etc.
 * @author luis
 */
public class PageTaskInfo implements FormsConfiguration, HeaderConfiguration{

    // The page configuration needed by the task.
    private PageConfiguration pageConfiguration;

    // The roles that are required to show the task.
    private String[] allowedRoles = {"ROLE_USER"};

    // The name of the main JSP file.
    private String mainJSPFileName;

    // The name's task;
    private String taskName;

    public PageTaskInfo() {
        pageConfiguration = new PageConfiguration();
    }

    public void addFormEntity(String entityName) {
        pageConfiguration.addFormEntity(entityName);
    }

    public List<String> getFormEntities() {
        return pageConfiguration.getFormEntities();
    }

    public void setFormEntities(List<String> formEntities) {
        pageConfiguration.setFormEntities(formEntities);
    }

    public void addCssFile(String cssFilename) {
        pageConfiguration.addCssFile(cssFilename);
    }

    public void addDWRService(String name) {
        pageConfiguration.addDWRService(name);
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

    public List<String> getDWRServices() {
        return pageConfiguration.getDWRServices();
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

    public void setDWRServices(List<String> dwrServices) {
        pageConfiguration.setDWRServices(dwrServices);
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
     * @return the mainJSPFileName
     */
    public String getMainJSPFileName() {
        return mainJSPFileName;
    }

    /**
     * @param mainJSPFileName the mainJSPFileName to set
     */
    public void setMainJSPFileName(String mainJSPFileName) {
        this.mainJSPFileName = mainJSPFileName;
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

}
