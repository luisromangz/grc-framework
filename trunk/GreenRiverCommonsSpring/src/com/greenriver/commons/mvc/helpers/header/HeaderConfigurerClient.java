/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

/**
 * This interface defines a contract that should be implemented by all
 * classes willing to use a <c>HeaderConfigurer</c> instance.
 * @author luis
 */
public interface HeaderConfigurerClient {
  
    /**
     * Sets the object tasked with configuring the header.
     * @param headerConfigurer The header configurer that will be used.
     */
    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer);

    /**
     * Gets the header configurer instance being used.
     * @return The header configurer.
     */
    public HeaderConfigurer getHeaderConfigurer();
}
