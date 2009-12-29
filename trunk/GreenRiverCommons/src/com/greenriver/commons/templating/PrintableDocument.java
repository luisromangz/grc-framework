/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

/**
 *
 * @author luis
 */
public class PrintableDocument {
    private String body;
    private String cssStyles;
    private PageConfiguration pageConfiguration;
    // <editor-fold defaultstate="collapsed" desc="Getters and setters">

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the cssStyles
     */
    public String getCssStyles() {
        return cssStyles;
    }

    /**
     * @param cssStyles the cssStyles to set
     */
    public void setCssStyles(String cssStyles) {
        this.cssStyles = cssStyles;
    }

    /**
     * @return the pageConfiguration
     */
    public PageConfiguration getPageConfiguration() {
        return pageConfiguration;
    }

    /**
     * @param pageConfiguration the pageConfiguration to set
     */
    public void setPageConfiguration(PageConfiguration pageConfiguration) {
        this.pageConfiguration = pageConfiguration;
    }
    // </editor-fold>
}
