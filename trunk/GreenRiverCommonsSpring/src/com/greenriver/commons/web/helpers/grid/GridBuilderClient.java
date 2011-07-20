
package com.greenriver.commons.web.helpers.grid;

/**
 * Interface defining the methods that a class using <b>GridBuilder</b> must
 * implement.
 * 
 * @author luisro
 */
public interface GridBuilderClient {
    /**
     * Gets the properties view builder
     * @return
     */
    GridBuilder getGridBuilder();
    
    /**
     * Sets the properties view builder
     * @param builder
     */
    void setGridBuilder(GridBuilder builder);
}
