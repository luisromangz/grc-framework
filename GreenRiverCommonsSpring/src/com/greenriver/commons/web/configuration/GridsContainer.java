package com.greenriver.commons.web.configuration;

import java.util.Map;

/**
 * This interface defines the methods that a grids container must implement.
 * @author luisro
 */
public interface GridsContainer {
    public Map<String,String> getGrids();
    public void setGrids(Map<String,String> grids);
    public void addGrid(String id, String className);
}
