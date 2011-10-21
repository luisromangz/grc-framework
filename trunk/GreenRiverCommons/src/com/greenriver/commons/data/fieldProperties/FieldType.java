/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.fieldProperties;

/**
 * This enumerate represents the diferent kinds of field in which a given
 * property should be represented.
 * @author luis
 */
public enum FieldType {
    /**
     * A single line of text.
     */
    TEXT,

    /**
     * A password input field.
     */
    PASSWORD,

    /**
     * A password edition field.
     */
    PASSWORDEDITOR,

    /**
     * A long piece of text.
     */
    LONGTEXT,

    /**
     * A long piece of formatted text.
     */
    RICHTEXT,

    /**
     * A yes no value.
     */
    CHECKBOX,

    /**
     * A natural, non-negative value.
     */
    NUMBER,

    /**
     * An IP address.
     */
    IPADDRESS,

    /**
     * An e-mail address.
     */
    EMAIL,
    
    /**
     * A selection between several options.
     */
    SELECTION,

    /**
     * A non exclusive selection.
     */
    MULTISELECTION,
    
    /**
     * A fancy multiselect widget for use with big collections.
     */
    FILTER_MULTISELECT,

    /**
     * Shows a role selector.
     */
    ROLESELECTOR,

    /**
     * A color chooser.
     */
    COLOR,

    /**
     * A float number selector
     */
    DECIMAL,

    /**
     * An autocompleted text input. It shows a list of available completions
     * but allows to add any custom value.
     */
    AUTOCOMPLETION,

    /**
     * A field used to hold a file.
     */
    FILE,

    /**
     * A field used to select a year day, e.g. the 53th day of the year.
     */
    YEAR_DAY,

    /**
     * A field used to select a time.
     */
    TIME,

    /**
     * A field holding a date.
     */
    DATE,
    
    MONTH,
    
    YEAR,

    /**
     * Spanish OLD_NIF id code
     */
    OLD_NIF,

    /**
     * Spanish OLD_CIF id code
     */
    OLD_CIF,

    /**
     * A field used to hold an Spanish's identifier used by individuals and
     * fiscal entities. This one mixes OLD_NIF and OLD_CIF formats and 
     * validations.
     */
    NIF;
}
