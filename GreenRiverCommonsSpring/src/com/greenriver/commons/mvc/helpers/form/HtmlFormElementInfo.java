
package com.greenriver.commons.mvc.helpers.form;

import com.greenriver.commons.mvc.helpers.HtmlElementInfo;

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
