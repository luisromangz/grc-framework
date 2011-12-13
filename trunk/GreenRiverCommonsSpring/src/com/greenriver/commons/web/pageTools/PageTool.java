package com.greenriver.commons.web.pageTools;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.configuration.FormsContainer;
import com.greenriver.commons.web.configuration.GridsContainer;
import com.greenriver.commons.web.configuration.PageConfig;
import com.greenriver.commons.web.configuration.PropsViewsContainer;
import com.greenriver.commons.web.helpers.header.HeaderConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class' instances contain information about tools meant to be used
 * system wide.
 * @author luis
 */
public class PageTool
        implements FormsContainer, PropsViewsContainer, GridsContainer, HeaderConfig {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private String[] allowedRoles = {"ROLE_USER"};
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
    private PageConfig pageConfiguration;
    private boolean initialized = false;
    // </editor-fold>

    public PageTool() {
        name = "Unnamed tool";
        dialogJspFiles = new ArrayList<String>();
        setupPaneJspFiles = new ArrayList<String>();
        pageConfiguration = new PageConfig();
    }
    
    public boolean isAllowedForUser(User user) {
        boolean allowed = user.hasAnyRole(this.getAllowedRoles());
        return allowed;
    }
    
    public final void initialize() {
        this.initialized = true;
        this.internalInitialize();
    }
    
    protected void internalInitialize() {
        
    }
    
    public final boolean isInitialized() {
        return initialized;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != PageTool.class) {
            return false;
        }
        
        String oName = ((PageTool) o).getName();
        return oName.equals(this.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
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
    
    @Override
    public List<String> getJavaScriptFiles() {
        return this.pageConfiguration.getJavaScriptFiles();
    }

    /**
     * @param javaScriptFiles the javaScriptFiles to set
     */
    @Override
    public void setJavaScriptFiles(List<String> javaScriptFiles) {
        this.pageConfiguration.setJavaScriptFiles(javaScriptFiles);
    }
    
    @Override
    public void addForm(String id, String className) {
        this.pageConfiguration.addForm(id, className);
    }
    
    @Override
    public Map<String, String> getForms() {
        return pageConfiguration.getForms();
    }
    
    @Override
    public void setForms(Map<String, String> forms) {
        pageConfiguration.setForms(forms);
    }
    
    @Override
    public void addPropsView(String id, String className) {
        pageConfiguration.addPropsView(id, className);
    }
    
    @Override
    public void setPropsViews(Map<String, String> className) {
        pageConfiguration.setPropsViews(className);
    }
    
    @Override
    public Map<String, String> getPropsViews() {
        return pageConfiguration.getPropsViews();
    }
    
    @Override
    public void addCssFile(String cssFilename) {
        pageConfiguration.addCssFile(cssFilename);
    }
    
    @Override
    public void addDwrService(String name) {
        pageConfiguration.addDwrService(name);
    }
    
    @Override
    public void addDojoBundle(String bundleName) {
        pageConfiguration.addDojoBundle(bundleName);
    }
    
    @Override
    public void addDojoModule(String dojoModule) {
        pageConfiguration.addDojoModule(dojoModule);
    }
    
    @Override
    public void addJavaScriptFile(String jsFilename) {
        pageConfiguration.addJavaScriptFile(jsFilename);
    }
    
    @Override
    public void addOnLoadScript(String code) {
        pageConfiguration.addOnLoadScript(code);
    }
    
    @Override
    public void addScript(String script) {
        pageConfiguration.addScript(script);
    }
    
    @Override
    public List<String> getCssFiles() {
        return pageConfiguration.getCssFiles();
    }
    
    @Override
    public List<String> getDojoModules() {
        return pageConfiguration.getDojoModules();
    }
    
    @Override
    public List<String> getDojoBundles() {
        return pageConfiguration.getDojoBundles();
    }
    
    @Override
    public List<String> getDwrServices() {
        return pageConfiguration.getDwrServices();
    }
    
    @Override
    public List<String> getOnLoadScripts() {
        return pageConfiguration.getOnLoadScripts();
    }
    
    @Override
    public List<String> getScripts() {
        return pageConfiguration.getScripts();
    }
    
    @Override
    public String getTitle() {
        return pageConfiguration.getTitle();
    }
    
    @Override
    public void setTitle(String title) {
        pageConfiguration.setTitle(title);
    }
    
    @Override
    public void setCssFiles(List<String> cssFiles) {
        pageConfiguration.setCssFiles(cssFiles);
    }
    
    @Override
    public void setDwrServices(List<String> dwrServices) {
        pageConfiguration.setDwrServices(dwrServices);
    }
    
    @Override
    public void setDojoBundles(List<String> dojoBundles) {
        pageConfiguration.setDojoBundles(dojoBundles);
    }
    
    @Override
    public void setDojoModules(List<String> dojoModules) {
        pageConfiguration.setDojoModules(dojoModules);
    }
    
    @Override
    public void setOnLoadScripts(List<String> onLoadScripts) {
        pageConfiguration.setOnLoadScripts(onLoadScripts);
    }
    
    @Override
    public void setScripts(List<String> scripts) {
        pageConfiguration.setScripts(scripts);
    }
    
    @Override
    public void addDojoBundles(List<String> dojoBundles) {
        pageConfiguration.addDojoBundles(dojoBundles);
    }
    
    @Override
    public void addDojoModules(List<String> dojoModules) {
        pageConfiguration.addDojoModules(dojoModules);
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
    
    @Override
    public Map<String, String> getGrids() {
        return pageConfiguration.getGrids();
    }
    
    @Override
    public void setGrids(Map<String, String> grids) {
        pageConfiguration.setGrids(grids);
    }
    
    @Override
    public void addGrid(String id, String className) {
        pageConfiguration.addGrid(id, className);
    }
    // </editor-fold>
}
