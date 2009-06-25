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
    BOOLEAN,

    /**
     * A numeric value.
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
     * A color chooser.
     */
    COLOR



}
