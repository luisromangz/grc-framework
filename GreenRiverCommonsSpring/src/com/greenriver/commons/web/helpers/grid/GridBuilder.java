package com.greenriver.commons.web.helpers.grid;

import com.greenriver.commons.data.fieldProperties.FieldProps;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interface that must implement all grid builders.
 * @author luisro
 */
public interface GridBuilder {

    /**
     * Adds a properties view to the ModelAndView instance. The properties view
     * is created, and if another with the same name exists an exception is
     * thrown. This GridInfo is made the current one.
     * 
     * @param id Id of the properties view to create.
     * @param mav Instance where the properties view should be added with
     * the id specified.
     * @return The newly created GridInfo instance.
     */
    GridInfo addGridInfo(String id, ModelAndView mav);

    /**
     * Gets all the defined properties views.
     * @return
     */
    List<GridInfo> getGridInfos();

    /**
     * Gets the current Properties view
     * @return
     */
    GridInfo getCurrentGridInfo();

    /**
     * Changes the current properties view. If the properties view is not in
     * the builder this method throws an exception
     * @param GridInfo
     * @throws IllegalArgumentException if the properties view is not
     * defined in the builder.
     */
    void makeCurrent(GridInfo gridInfo);

    /**
     * Add a view for a property to the current properties view
     * @param classFullName Full name of the entity
     */
    void addGridInfoFromClass(String classFullName);

    /**
     * Add a view for a property to the current properties view
     * @param modelClass
     */
    void addGridInfoFromClass(Class modelClass);   

    /**
     * Removes a colum from the current grid.
     * @param id
     */
    void removeGridColumn(String id);

    /**
     * Adds a generic property view with only the property name and the
     * property label value.
     * @param id
     * @param label
     * @return The created property view or null if it was not added.
     */
    GridColumnInfo addGridColumn(String id, String label);
}
