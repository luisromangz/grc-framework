package com.greenriver.commons.data.fieldProperties.fields;

/**
 * Annotation used to hold properties and constraints of file fields.
 * @author luisro
 */
public @interface FieldFile {
    int minSize() default 0;
    int maxSize() default Integer.MAX_VALUE;

    String[] allowedFileTypes();
}
