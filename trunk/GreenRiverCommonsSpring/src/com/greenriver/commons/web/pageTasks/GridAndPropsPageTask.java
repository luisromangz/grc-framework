package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * A page task including a Dojo DataGrid and a properties view.
 * @author luisro
 */
public class GridAndPropsPageTask
        extends DojoHandledPageTask {

    private String propsViewClass;
    private String gridClass;
  
    // The label to be used for the element.
    private String element;
    // A boolean marking the gender of the element.
    private boolean maleElement;
    private String indefiniteElementLabel;
    private String selectedElementLabel;
    
    private List<String> extraJspFiles;
    
    private String service;
    

    public GridAndPropsPageTask() {
        this.setMainJspFileName("../../gridAndPropsPageTask.jsp");

        this.setElement("elemento");
        this.setMaleElement(true);
        
        this.extraJspFiles = new ArrayList<String>();
    }

    @Override
    protected void initializeInternal() {
        if (Strings.isNullOrEmpty(gridClass)) {
            throw new IllegalStateException(
                    "gridClass for task " + this.getTaskName() + " cannot be null!");
        }
        
        this.addGrid("grid", gridClass);

        if (Strings.isNullOrEmpty(propsViewClass)) {
            throw new IllegalStateException(
                    "propsViewClass for task " + this.getTaskName() + " cannot be null!");
        }

        this.addPropertiesView("propsView", propsViewClass);
        
        this.addDwrService(this.service);
        
    }

    @Override
    public Properties getClientControllerInitArgs() {
        Properties props = super.getClientControllerInitArgs();
        props.put("service", getService());
        return props;
    }
    
    

    private void createElementLabels() {
        this.selectedElementLabel = String.format("%s %s %s",
                maleElement ? "el" : "la",
                element,
                maleElement ? "seleccionado" : "seleccionada");

        this.setIndefiniteElementLabel(String.format("%s %s",
                maleElement ? "un" : "una",
                element));
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the element
     */
    public String getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public final void setElement(String element) {
        this.element = element;
        createElementLabels();
    }

    /**
     * @return the maleElement
     */
    public boolean isMaleElement() {
        return maleElement;
    }

    /**
     * @param maleElement the maleElement to set
     */
    public final void setMaleElement(boolean maleElement) {
        this.maleElement = maleElement;
        createElementLabels();
    }

    /**
     * @return the indefiniteElementLabel
     */
    public String getIndefiniteElementLabel() {
        return indefiniteElementLabel;
    }

    /**
     * @param indefiniteElementLabel the indefiniteElementLabel to set
     */
    public void setIndefiniteElementLabel(String indefiniteElementLabel) {
        this.indefiniteElementLabel = indefiniteElementLabel;
    }

    /**
     * @return the selectedElementLabel
     */
    public String getSelectedElementLabel() {
        return selectedElementLabel;
    }

    /**
     * @return the propsViewClass
     */
    public String getPropsViewClass() {
        return propsViewClass;
    }

    /**
     * @param propsViewClass the propsViewClass to set
     */
    public void setPropsViewClass(String propsViewClass) {
        this.propsViewClass = propsViewClass;
    }

    /**
     * @return the gridClass
     */
    public String getGridClass() {
        return gridClass;
    }

    /**
     * @param gridClass the gridClass to set
     */
    public void setGridClass(String gridClass) {
        this.gridClass = gridClass;
    }   
    
      /**
     * @return the extraJspFiles
     */
    public List<String> getExtraJspFiles() {
        return extraJspFiles;
    }

    /**
     * @param extraJspFiles the extraJspFiles to set
     */
    public void setExtraJspFiles(List<String> extraJspFiles) {
        this.extraJspFiles = extraJspFiles;
    }
    //</editor-fold>

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

   
}
