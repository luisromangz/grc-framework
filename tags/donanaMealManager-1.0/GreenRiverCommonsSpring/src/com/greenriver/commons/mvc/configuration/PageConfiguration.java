
package com.greenriver.commons.mvc.configuration;

import com.greenriver.commons.mvc.helpers.header.PageHeaderConfiguration;
import com.greenriver.commons.mvc.pageTools.PageTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This class extends <c>PageHeaderConfiguration</c> in order to be able
 * to hold non-header realated page info.
 * @author luis
 */
public class PageConfiguration extends PageHeaderConfiguration
        implements FormsConfiguration, PropertiesViewConfiguration, Cloneable {

     private Map<String,String> formEntities;
     private Map<String, Object> propertiesViewConfiguration;
     private List<PageTool> pageTools;

     public PageConfiguration () {
         super();

         formEntities = new Hashtable<String, String>();
	 propertiesViewConfiguration = new Hashtable<String, Object>();
         pageTools =  new ArrayList<PageTool>();
     }

    @Override
     public Object clone() {

        // We just do a superficial copy.
         PageConfiguration  newConfiguration = new PageConfiguration();
         newConfiguration.setFormEntities(formEntities);
         newConfiguration.setPropertiesView(propertiesViewConfiguration);
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

    // <editor-fold defaultstate="collapsed" desc="Form entities">
    /**
     * Gets the names of the entities that will have a form created for edition
     * of their instances.
     * @return the formEntities
     */
    public Map<String, String> getFormEntities() {
        return formEntities;
    }

    /**
     * Sets the names of the entities a form will be created for.
     * @param formEntities the formEntities to set
     */
    public void setFormEntities(Map<String, String> formEntities) {
        this.formEntities = new Hashtable<String, String>(formEntities);
    }

    /**
     * Adds an entity name to the list of entity names for which forms have
     * to be created.
     * @param id The form's id.
     * @param entityName The name of the entity a form will be created for.
     */
    public void addFormEntity(String id, String entityName) {
        this.formEntities.put(id, entityName);
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Properties view">
    public void addPropertiesView(String id,
            Object configuration) {
        this.propertiesViewConfiguration.put(id, configuration);
    }

    public void setPropertiesView(
            Map<String, Object> configuration) {
        this.propertiesViewConfiguration =
                new Hashtable<String, Object>(configuration);
    }

    public Map<String, Object> getPropertiesView() {
        return this.propertiesViewConfiguration;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Page tools">
    public void setPageTools(List<PageTool> pageTools) {
        this.pageTools = new ArrayList<PageTool>(pageTools);
    }

    public List<PageTool> getPageTools() {
        return this.pageTools;
    }

    public void addPageTool(PageTool pageTool) {
        pageTools.add(pageTool);
    }// </editor-fold>
}
