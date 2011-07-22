
package com.greenriver.commons.web.pageTasks;

import java.util.Properties;

/**
 * Defines de config for a page task able to list add edit and remove elements.
 * @author luisro
 */
public class CRUDPageTask extends GridAndPropsPageTask {
    private String formClass;
    private String getForEditMethod="getForEdit";
    private String removeMethod="remove";
    private String getNewMethod="getNew";
    private String saveMethod = "save";

    public CRUDPageTask() {
        this.setMainJspFileName("../../crudPageTask.jsp");
    }
    
    

    @Override
    public Properties getControllerInitArgs() {
        Properties p = super.getControllerInitArgs();
        
        p.put("getForEditMethod",getForEditMethod);
        p.put("removeMethod",removeMethod);
        p.put("getNewMethod",getNewMethod);
        p.put("saveMethod",saveMethod);
        
        return p;
    }

    @Override
    protected void initializeInternal() {
        super.initializeInternal();
        
        this.addForm("form", formClass);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the formClass
     */
    public String getFormClass() {
        return formClass;
    }
    
    /**
     * @param formClass the formClass to set
     */
    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }
    
    /**
     * @return the getForEditMethod
     */
    public String getGetForEditMethod() {
        return getForEditMethod;
    }
    
    /**
     * @param getForEditMethod the getForEditMethod to set
     */
    public void setGetForEditMethod(String getForEditMethod) {
        this.getForEditMethod = getForEditMethod;
    }
    
    //</editor-fold>

    /**
     * @return the saveMethod
     */
    public String getSaveMethod() {
        return saveMethod;
    }

    /**
     * @param saveMethod the saveMethod to set
     */
    public void setSaveMethod(String saveMethod) {
        this.saveMethod = saveMethod;
    }
}
