package com.greenriver.commons.mvc.helpers;

import com.greenriver.commons.Strings;

/**
 * Options for use when adding a property to a list of properties. These options
 * allow to specify where the inserted option is going to be placed and thus
 * control ordering.
 * @author Miguel Angel
 */
public class PropertyOptions {

    private String propName;
    private String referencePropName;
    private PropertyOrder order;

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getReferencePropName() {
        return referencePropName;
    }

    public void setReferencePropName(String referencePropName) {
        this.referencePropName = referencePropName;
    }

    public PropertyOrder getOrder() {
        return order;
    }

    public void setOrder(PropertyOrder order) {
        this.order = order;
    }

    public boolean hasReferencePropName() {
        return !Strings.isNullOrEmpty(referencePropName);
    }

    public PropertyOptions() {
        order = PropertyOrder.LAST;
    }

    public PropertyOptions(String propName, String referencePropName, PropertyOrder order) {
        this.propName = propName;
        this.referencePropName = referencePropName;
        this.order = order;
    }

    /**
     * Parses a string into options<br/><br>/
     * The format of the string is: <br/>
     * <p>viewId[@insertionPosition[:referenceId]]</p>
     * <ul>
     *  <li><strong>viewId: </strong>Id of the view (required)</li>
     *  <li><strong>referenceId: </strong>Id of the view used as reference
     *  when inserting this one.</li>
     *  <li><strong>insertionPosition: </strong>insertion position for the
     *  view id. see <b>PropertyOptions.Order</b> for a list of options.
     * </li>
     * </ul>
     * @param str
     * @return
     */
    public static PropertyOptions parseString(String str) {
        PropertyOptions pvo = new PropertyOptions();

        if (Strings.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("Parameter 'str' is null or empty");
        }

        int pos = str.indexOf("@");

        if (pos >= 0 && str.length() == (pos + 1)) {
            throw new IllegalArgumentException("Expected options after '@'");
        } else if (pos < 0) {
            pvo.propName = str;
            return pvo;
        }

        pvo.propName = str.substring(0, pos);
        str = str.substring(pos + 1);
        pos = str.indexOf(":");

        if (pos >= 0 && str.length() == (pos + 1)) {
            throw new IllegalArgumentException(
                    "Expected insertion position after ':'");
        } else if (pos >= 0) {
            pvo.referencePropName = str.substring(pos + 1);
            str = str.substring(0, pos);
        }

        try {
            pvo.order = PropertyOrder.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid insertion position: " + str);
        }

        if (Strings.isNullOrEmpty(pvo.referencePropName) &&
               (pvo.order == PropertyOrder.AFTER ||
                    pvo.order == PropertyOrder.BEFORE)) {
            throw new IllegalArgumentException("Can't use ordering " +
                    pvo.order + " without a reference property name");
        }

        return pvo;
    }
}
