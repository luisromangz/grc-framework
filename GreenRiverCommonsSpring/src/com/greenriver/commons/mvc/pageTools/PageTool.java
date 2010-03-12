package com.greenriver.commons.mvc.pageTools;

import com.greenriver.commons.mvc.configuration.FormsConfiguration;
import com.greenriver.commons.mvc.configuration.PageConfiguration;
import com.greenriver.commons.mvc.configuration.PropertiesViewConfiguration;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class' instances contain information about tools meant to be used
 * system wide.
 * @author luis
 */
public class PageTool implements FormsConfiguration,
        PropertiesViewConfiguration, HeaderConfiguration {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The tool's name.
     */
    private String name;
    /**
     * The JSP files used as dialogs in the application.
     */
    private List<String> dialogJspFiles;
    /**
     * The JSP files used as panes in the config area of the app.
     */
    private List<String> setupPaneJspFiles;
    private PageConfiguration pageConfiguration;


    private boolean loadedOnPageLoad = true;
    // </editor-fold>

    public PageTool() {
        name = "Unnamed tool";
        dialogJspFiles = new ArrayList<String>();
        setupPaneJspFiles = new ArrayList<String>();
        pageConfiguration = new PageConfiguration();
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass() != PageTool.class) {
            return false;
        }

        String oName = ((PageTool)o).getName();
        return oName.equals(this.name);
    }

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dialogJspFiles
     */
    public List<String> getDialogJspFiles() {
        return dialogJspFiles;
    }

    /**
     * @param dialogJspFiles the dialogJspFiles to set
     */
    public void setDialogJspFiles(List<String> dialogJspFiles) {
        this.dialogJspFiles = dialogJspFiles;
    }

    /**
     * @return the setupPaneJspFiles
     */
    public List<String> getSetupPaneJspFiles() {
        return setupPaneJspFiles;
    }

    /**
     * @param setupPaneJspFiles the setupPaneJspFiles to set
     */
    public void setSetupPaneJspFiles(List<String> setupPaneJspFiles) {
        this.setupPaneJspFiles = setupPaneJspFiles;
    }

    public List<String> getJavaScriptFiles() {
        return this.pageConfiguration.getJavaScriptFiles();
    }

    /**
     * @param javaScriptFiles the javaScriptFiles to set
     */
    public void setJavaScriptFiles(List<String> javaScriptFiles) {
        this.pageConfiguration.setJavaScriptFiles(javaScriptFiles);
    }

    public void addFormEntity(String id, String entityName) {
        this.pageConfiguration.addFormEntity(id, entityName);
    }

    public Map<String, String> getFormEntities() {
        return pageConfiguration.getFormEntities();
    }

    public void setFormEntities(Map<String, String> formEntities) {
        pageConfiguration.setFormEntities(formEntities);
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

    public void setOnLoadScripts(List<String> onLoadScripts) {
        pageConfiguration.setOnLoadScripts(onLoadScripts);
    }

    public void setScripts(List<String> scripts) {
        pageConfiguration.setScripts(scripts);
    }

    public void addDojoBundles(List<String> dojoBundles) {
        pageConfiguration.addDojoBundles(dojoBundles);
    }

    public void addDojoModules(List<String> dojoModules) {
        pageConfiguration.addDojoModules(dojoModules);
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


