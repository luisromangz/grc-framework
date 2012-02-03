package com.greenriver.commons.web.pageTasks;

import com.greenriver.commons.Strings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * A page task including a Dojo DataGrid and a properties view.
 *
 * @author luisro
 */
public class GridAndPropsPageTask
        extends DojoHandledPageTask {

    private String propsViewClass;
    private String gridClass;
    // The label to be used for the element.
    private String element;
    private String elementPlural;
    // A boolean marking the gender of the element.
    private boolean maleElement;
    private String indefiniteElementLabel;
    private String selectedElementLabel;
    private String customPropsViewJsp = "";
    private String customGridToolbarJsp = "";
    private String itemToolbarJspFile = "itemToolbar.jsp";
    private List<String> extraJspFiles;
    private String service;
    private String getForViewMethod = "getForView";
    private String queryMethod = "query";

    public GridAndPropsPageTask() {
        this.setMainJspFileName("../../gridAndPropsPageTask.jsp");
        this.setDojoControllerModule("grc.web.tasks.GridAndPropsPageTaskController");

        this.setElement("elemento");
        this.setMaleElement(true);

        this.extraJspFiles = new ArrayList<String>();
    }

    @Override
    public Properties getControllerInitArgs() {
        Properties props = super.getControllerInitArgs();

        if (Strings.isNullOrEmpty(service)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its service property");
        }
        props.put("service", service);
        if (Strings.isNullOrEmpty(element)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its element property");
        }
        props.put("element", element);
        props.put("elementPlural", this.getElementPlural());

        props.put("maleElement", maleElement);
        props.put("indefiniteElement", indefiniteElementLabel);
        props.put("selectedElement", selectedElementLabel);

        if (Strings.isNullOrEmpty(getQueryMethod())) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its queryMethod property");
        }
        props.put("queryMethod", getQueryMethod());
        if (Strings.isNullOrEmpty(getGetForViewMethod())) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify its getForViewMethod property");
        }
        props.put("getForViewMethod", getGetForViewMethod());
        return props;
    }

    private void createElementLabels() {
        this.selectedElementLabel = String.format("%s %s %s",
                maleElement ? "el" : "la",
                element,
                maleElement ? "seleccionado" : "seleccionada");

        this.indefiniteElementLabel = String.format("%s %s",
                maleElement ? "un" : "una",
                element);
    }

    @Override
    public List<String> getDwrServices() {
        List<String> services = new ArrayList<String>(super.getDwrServices());
        services.add(this.service);;
        return services;
    }

    @Override
    public Map<String, String> getPropsViews() {
        if (Strings.isNullOrEmpty(propsViewClass)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify propsViewClass field");
        }

        // New map required so we dont override config.
        Map<String, String> propsViews = new HashMap<String, String>(super.getPropsViews());
        propsViews.put("propsView", propsViewClass);
        return propsViews;
    }

    @Override
    public Map<String, String> getGrids() {
        if (Strings.isNullOrEmpty(gridClass)) {
            throw new IllegalStateException("Task " + this.getTaskName() + " doesn't specify gridClass field");
        }

        // New map required so we dont override config.
        Map<String, String> grids = new HashMap<String, String>(super.getGrids());
        grids.put("grid", gridClass);
        return grids;
    }

    public String getElementPlural() {
        // We support the simle case without anybody telling us.
        if (Strings.isNullOrEmpty(elementPlural)) {
            return element + "s";
        }

        return elementPlural;
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

    /**
     * @return the customPropsViewJsp
     */
    public String getCustomPropsViewJsp() {
        return customPropsViewJsp;
    }

    /**
     * @param customPropsViewJsp the customPropsViewJsp to set
     */
    public void setCustomPropsViewJsp(String customPropsViewJsp) {
        this.customPropsViewJsp = customPropsViewJsp;
    }

    /**
     * @return the customGridToolbarJsp
     */
    public String getCustomGridToolbarJsp() {
        return customGridToolbarJsp;
    }

    /**
     * @param customGridToolbarJsp the customGridToolbarJsp to set
     */
    public void setCustomGridToolbarJsp(String customGridToolbarJsp) {
        this.customGridToolbarJsp = customGridToolbarJsp;
    }

    /**
     * @return the getForViewMethod
     */
    public String getGetForViewMethod() {
        return getForViewMethod;
    }

    /**
     * @param getForViewMethod the getForViewMethod to set
     */
    public void setGetForViewMethod(String getForViewMethod) {
        this.getForViewMethod = getForViewMethod;
    }

    /**
     * @return the queryMethod
     */
    public String getQueryMethod() {
        return queryMethod;
    }

    /**
     * @param queryMethod the queryMethod to set
     */
    public void setQueryMethod(String queryMethod) {
        this.queryMethod = queryMethod;
    }

    /**
     * @param elementPlural the elementPlural to set
     */
    public void setElementPlural(String elementPlural) {
        this.elementPlural = elementPlural;
    }
    //</editor-fold>
}
