package com.greenriver.commons.web.configuration;

import com.greenriver.commons.web.helpers.header.PageHeaderConfig;
import com.greenriver.commons.web.pageTools.PageTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class extends <c>PageHeaderConfig</c> in order to be able
 * to hold non-header realated page info.
 * @author luis
 */
public class PageConfig
        extends PageHeaderConfig
        implements FormsContainer, PropsViewsContainer, GridsContainer, PageToolsContainer, Cloneable {

    private Map<String, String> forms;
    private Map<String, String> propertiesViews;
    private Map<String, String> grids;
    private List<PageTool> pageTools;

    public PageConfig() {
        super();

        forms = new HashMap<String, String>();
        propertiesViews = new HashMap<String, String>();
        grids = new HashMap<String, String>();
        pageTools = new ArrayList<PageTool>();
    }

    @Override
    public Object clone() {

        // We just do a superficial copy.
        PageConfig newConfiguration = new PageConfig();
        newConfiguration.setForms(forms);
        newConfiguration.setPropsViews(propertiesViews);
        newConfiguration.setPageTools(pageTools);

        newConfiguration.setCssFiles(this.getCssFiles());
        newConfiguration.setDojoBundles(this.getDojoBundles());
        newConfiguration.setDojoModules(this.getDojoModules());
        newConfiguration.setDwrServices(getDwrServices());
        newConfiguration.setJavaScriptFiles(getJavaScriptFiles());
        newConfiguration.setOnLoadScripts(getOnLoadScripts());
        newConfiguration.setScripts(getScripts());
        newConfiguration.setTitle(this.getTitle());

        return newConfiguration;

    }

    // <editor-fold defaultstate="collapsed" desc="Form classes">
    /**
     * Gets the names of the classes that will have a form created for edition
     * of their instances.
     * @return the formclasses
     */
    @Override
    public Map<String, String> getForms() {
        return forms;
    }

    /**
     * Sets the names of the classes a form will be created for.
     * @param forms the form classes to set
     */
    @Override
    public void setForms(Map<String, String> forms) {
        this.forms = new HashMap<String, String>(forms);
    }

    /**
     * Adds an class name to the list of class names for which forms have
     * to be created.
     * @param id The form's id.
     * @param className The name of the class a form will be created for.
     */
    @Override
    public void addForm(String id, String className) {
        this.forms.put(id, className);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Properties view">
    @Override
    public void addPropsView(String id, String className) {
        this.propertiesViews.put(id, className);
    }

    @Override
    public void setPropsViews(Map<String, String> propertiesView) {
        this.propertiesViews =new HashMap<String, String>(propertiesView);
    }

    @Override
    public Map<String, String> getPropsViews() {
        return this.propertiesViews;
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Page tools">
    @Override
    public void setPageTools(List<PageTool> pageTools) {
        this.pageTools = new ArrayList<PageTool>(pageTools);
    }

    @Override
    public List<PageTool> getPageTools() {
        return this.pageTools;
    }

    public void addPageTool(PageTool pageTool) {
        pageTools.add(pageTool);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Grids">
    /**
     * @return the grids
     */
    @Override
    public Map<String, String> getGrids() {
        return grids;
    }

    /**
     * @param grids the grids to set
     */
    @Override
    public void setGrids(Map<String, String> grids) {
        this.grids = new HashMap<String, String>(grids);
    }

    @Override
    public void addGrid(String id, String className) {
        this.grids.put(id, className);
    }
    // </editor-fold>
}
