
package com.greenriver.commons.web.helpers.form;

/**
 *
 * @author luis
 */
public class HtmlFormElementInfo extends HtmlElementInfo {

    public HtmlFormElementInfo(String fieldId) {
	super(fieldId);
	setElementType("input");
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        super.setId(id);
    }
}
