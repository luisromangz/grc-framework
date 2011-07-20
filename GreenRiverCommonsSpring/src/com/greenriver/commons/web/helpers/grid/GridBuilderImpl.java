package com.greenriver.commons.web.helpers.grid;

import com.greenriver.commons.ClassFields;
import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldProps;
import com.greenriver.commons.data.fieldProperties.FieldsInsertionMode;
import com.greenriver.commons.data.fieldProperties.GridColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Miguel Angel
 */
public class GridBuilderImpl implements GridBuilder {
    private List<GridInfo> gridInfos;
    private GridInfo currentGridInfo;
    private Map<String, Class> classCache;

    public GridBuilderImpl() {
        gridInfos = new ArrayList<GridInfo>();
        classCache = new HashMap<String, Class>();
    }

    private void assertCurrent() {
        if (currentGridInfo == null) {
            throw new IllegalStateException(
                    "No properties view have been added to this builder");
        }
    }

    @Override
    public GridInfo addGridInfo(String id, ModelAndView mav) {
        if (mav.getModel().containsKey(id)) {
            throw new IllegalArgumentException(
                    "There is already a grid with the id " + id);
        }

        currentGridInfo = new GridInfo(id);
        mav.addObject(id, currentGridInfo);

        return currentGridInfo;
    }

    @Override
    public List<GridInfo> getGridInfos() {
        return Collections.unmodifiableList(gridInfos);
    }

    @Override
    public GridInfo getCurrentGridInfo() {
        return currentGridInfo;
    }

    @Override
    public void makeCurrent(GridInfo gridInfo) {
        if (gridInfo == null) {
            throw new IllegalArgumentException(
                    "The grid  can't be null");
        }

        if (!gridInfos.contains(gridInfo)) {
            throw new IllegalArgumentException(
                    "The grid is not defined in the builder");
        }

        this.currentGridInfo = gridInfo;
    }

    private GridColumnInfo addGridColumn(
            String field, FieldProps props, GridColumn  columnProps) {

        if (props == null) {
            throw new IllegalArgumentException("Properties can't be null");
        }

        if (Strings.isNullOrEmpty(field)) {
            throw new IllegalArgumentException("Field can't be null nor empty.");
        }

        assertCurrent();

        if (currentGridInfo.containsColumnForField(field)) {
            return null;
        }

        GridColumnInfo column = new GridColumnInfo(field);
        column.setLabel(props.label());
        
        if(columnProps!=null) {
            column.setWidth(columnProps.width());
            
            if(columnProps.canSort()) {
                this.currentGridInfo.addSortableColumn(field);
            }
        }
        
     
        this.currentGridInfo.addGridColumn(column);
       

        return column;
    }

    /**
     * Adds a generic property view with only the property name and the
     * property label value.
     * @param field
     * @param label Label for the element. Can be an empty string but not null.
     * @return The created property view or null if it was not added.
     */
    @Override
    public GridColumnInfo addGridColumn(String field, String label) {
        if (Strings.isNullOrEmpty(field)) {
            throw new IllegalArgumentException("Field can't be null nor empty.");
        }

        if (label == null) {
            throw new IllegalArgumentException(
                    "Label parameter can be empty but cannot be null");
        }

        assertCurrent();

        if (currentGridInfo.containsColumnForField(field)) {
            return null;
        }

        GridColumnInfo column = new GridColumnInfo(field);
        column.setLabel(label);        
        this.currentGridInfo.addGridColumn(column);
        
        return column;
    }

    @Override
    public void addGridInfoFromClass(String classFullName) {
        addGridInfoFromClass(getClassFromName(classFullName));
    }
  

    @Override
    public void addGridInfoFromClass(Class viewClass) {

        assertCurrent();

        // If the list of properties is empty we include all of them.
        List<String>  propertiesToShow = generatePropertyList(viewClass);

        for (String propName : propertiesToShow) {
            // Let this throw an exception if the field is not defined. This
            // looks also in the super class so if the field is not defined it
            // will throw an exception.
            Field classField = ClassFields.get(propName, viewClass, true, true);

            FieldProps fieldProps = classField.getAnnotation(FieldProps.class);
            
            GridColumn columnProps = classField.getAnnotation(GridColumn.class);

            //Only go ahead if there is a field property
            if (fieldProps != null) {
                addGridColumn(propName, fieldProps, columnProps);
            }
        }
    }   

    @Override
    public void removeGridColumn(String field) {
        assertCurrent();

        currentGridInfo.removeGridColumn(field);
    }

    private Class getClassFromName(String classFullName) {
        if (classCache.containsKey(classFullName)) {
            return classCache.get(classFullName);
        }

        Class result = null;

        try {
            result = Class.forName(classFullName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "Entity class '" + classFullName + "' can't be loaded.",
                    ex);
        }

        classCache.put(classFullName, result);

        return result;
    }

    /**
     * Generates a list with all the annotated properties in the class
     * (The annotation to find is FieldProperties) and all its super classes.
     * @param entityClass
     * @return a list of properties
     */
    private List<String> generatePropertyList(Class entityClass) {

        if (entityClass == null) {
            throw new NullPointerException("Parameter entityClass is null.");
        }

        if (entityClass == Object.class) {
            return new ArrayList<String>(0);
        }

        FieldsProperties entityProperties =
                (FieldsProperties) entityClass.getAnnotation(
                FieldsProperties.class);

        return ClassFields.getNames(
                entityClass,
                true,
                entityProperties==null?FieldsInsertionMode.NONE: entityProperties.parentInsertionMode(),
                new Class[]{FieldProps.class});
    }

   
}
