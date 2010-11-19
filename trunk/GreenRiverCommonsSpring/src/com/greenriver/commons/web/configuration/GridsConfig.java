/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.web.configuration;

import java.util.Map;

/**
 *
 * @author luisro
 */
public interface GridsConfig {
    public Map<String,String> getGrids();
    public void setGrids(Map<String,String> grids);
    public void addGrid(String id, String className);
}
