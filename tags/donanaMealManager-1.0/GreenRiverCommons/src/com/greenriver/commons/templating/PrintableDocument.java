/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.templating;

import java.io.Serializable;

/**
 * This class hold the information necessary to print a template-generated
 * document.
 * 
 * @author luis
 */
public class PrintableDocument implements Serializable{
    private String body;
    private String cssStyles;
    private PageConfiguration pageConfiguration;
    private String title;

    public PrintableDocument(String title) {
        this.title = title;
    }


    // <editor-fold defaultstate="collapsed" desc="Getters and setters">

    /**
     * @return the body
     */
    public String getBody() {
        return body==null?"":body;
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
        return cssStyles==null?"":cssStyles;
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

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    // </editor-fold>
}
