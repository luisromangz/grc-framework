package com.greenriver.commons.web.pageTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private String itemToolbarJspFile="itemToolbar.jsp";
    private List<String> extraJspFiles;
    
    private String service;
    
    private String getForViewMethod ="getForView";
    private String queryMethod = "query";
    

    public GridAndPropsPageTask() {
        this.setMainJspFileName("../../gridAndPropsPageTask.jsp");

        this.setElement("elemento");
        this.setMaleElement(true);
        
        this.extraJspFiles = new ArrayList<String>();
    }

    @Override
    public Properties getControllerInitArgs() {
        Properties props = super.getControllerInitArgs();
        props.put("service", getService());
        props.put("element",element);
        props.put("maleElement", maleElement);
        props.put("indefiniteElement", indefiniteElementLabel);
        props.put("selectedElement",selectedElementLabel);
        props.put("queryMethod",queryMethod);
        props.put("getForViewMethod", getForViewMethod);
        return props;
    }
    
    private void createElementLabels() {
         this.selectedElementLabel = String.format("%s %s %s",
                maleElement ? "el" : "la",
                element,
                maleElement ? "seleccionado" : "seleccionada");

        this.indefiniteElementLabel=String.format("%s %s",
                maleElement ? "un" : "una",
                element);
    }

    @Override
    public List<String> getDwrServices() {
        List<String> services=new ArrayList<String>(super.getDwrServices());
        services.add(this.service);;
        return services;
    }

    @Override
    public Map<String, String> getPropertiesView() {
        // New map required so we dont override config.
        Map<String, String> propsViews=  new HashMap<String,String>(super.getPropertiesView());
        propsViews.put("propsView", propsViewClass);
        return propsViews;
    }

    @Override
    public Map<String, String> getGrids() {
        // New map required so we dont override config.
        Map<String,String> grids= new HashMap<String,String>(super.getGrids());
        grids.put("grid",gridClass);
        return grids;
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

    /**
     * @return the itemToolbarJspFile
     */
    public String getItemToolbarJspFile() {
        return itemToolbarJspFile;
    }

    /**
     * @param itemToolbarJspFile the itemToolbarJspFile to set
     */
    public void setItemToolbarJspFile(String itemToolbarJspFile) {
        this.itemToolbarJspFile = itemToolbarJspFile;
    }

   
}
