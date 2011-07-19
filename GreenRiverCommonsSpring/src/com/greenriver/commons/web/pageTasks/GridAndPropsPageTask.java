package com.greenriver.commons.web.pageTasks;

/**
 * A page task including a Dojo DataGrid and a properties view.
 * @author luisro
 */
public class GridAndPropsPageTask
    extends DojoHandledPageTask {
    
    String propsViewClass;
    String gridClass;
    String gridLoadServiceCall;
    String propsViewLoadServiceCall;
    
    
    // The label to be used for the element.
    private String element;
    // A boolean marking the gender of the element.
    private boolean maleElement;
    
    private String indefiniteElementLabel;
    private String selectedElementLabel;

    public GridAndPropsPageTask() {
        this.setMainJspFileName("../../gridAndPropsPageTask.jsp");
        
        this.setElement("elemento");
        this.setMaleElement(true);
    }
    
    private void createElementLabels() {
        this.selectedElementLabel= String.format("%s %s %s",
                maleElement?"el":"la",
                element,
                maleElement?"seleccionado":"seleccionada");
        
        this.setIndefiniteElementLabel(String.format("%s %s",
                 maleElement?"un":"una",
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
    
    //</editor-fold>

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
            

    
}
