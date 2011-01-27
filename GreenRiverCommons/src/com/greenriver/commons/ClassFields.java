package com.greenriver.commons;

import com.greenriver.commons.data.fieldProperties.FieldsInsertionMode;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Angel
 */
public class ClassFields {

    public static Field get(String name, Object target, boolean recursive, boolean throwExOnFail) {
        if (target == null) {
            throw new NullPointerException("Parameter 'target' is null.");
        }

        return get(name, target.getClass(), recursive, throwExOnFail);
    }

    /**
     * Gets a field from an object. If the recursive parameter is set to true
     * the search will be done first in the fields defined by the current object
     * and then in each superclass in ascending order.
     * @param name
     * @param target
     * @param recursive If true the inheritance hierarchy is followed upstream
     * until the field is defined or the base class is of type Object.
     * @param throwExOnFail
     * @return Null if the field is not found or the field descriptor if found.
     */
    public static Field get(String name, Class target, boolean recursive, boolean throwExOnFail) {
        if (target == null) {
            throw new NullPointerException("Parameter 'target' is null.");
        }

        if (target == Object.class) {
            return null;
        }

        Field field = null;
        Class cls = target;

        while (cls != Object.class && field == null) {
            try {
                field = cls.getDeclaredField(name);
                if (field == null) {
                    break;
                }
            } catch (NoSuchFieldException ex) {
                cls = cls.getSuperclass();
            }
        }

        if (throwExOnFail && field == null) {
            String msg = "The field '" + name + "' is not defined in class "
                        + target.getName();
            if (recursive) {
                throw new FieldNotFoundException(msg + " or any superclass.");
            } else {
                throw new FieldNotFoundException(msg + ".");
            }
        }

        return field;
    }

    /**
     * Gets all the names of all fields
     * @param targetClass
     * @param iterateSuperClass
     * @param appendSuperClassNames
     * @param annotatedWith
     * @return
     */
    public static List<String> getNames(
            Class targetClass,
            boolean iterateSuperClass, FieldsInsertionMode mode,
            Class[] annotatedWith) {
        
        if (targetClass == null) {
            throw new NullPointerException("Parameter 'targetClass' is null.");
        }

        if (targetClass == Object.class) {
            return new ArrayList<String>(0);
        }

        List<String> result = null;
        Field[] classFields = targetClass.getDeclaredFields();

        if (iterateSuperClass && mode==FieldsInsertionMode.PREPEND) {
            result = new ArrayList<String>(getNames(
                    targetClass.getSuperclass(),
                    true, mode,
                    annotatedWith));
        } else {
            result = new ArrayList<String>(classFields.length);
        }

        for (Field field : classFields) {
            if (annotatedWith != null) {
                for (Class cls : annotatedWith) {
                    if (field.getAnnotation(cls) != null) {
                        result.add(field.getName());
                    }
                }
            } else {
                result.add(field.getName());
            }
        }

        if (iterateSuperClass && mode == FieldsInsertionMode.APPEND) {
            result.addAll(getNames(
                    targetClass.getSuperclass(),
                    true, mode,
                    annotatedWith));
        }

        return result;
    }
}
