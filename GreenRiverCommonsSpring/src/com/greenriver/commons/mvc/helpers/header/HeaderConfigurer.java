/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * This interface defines a model for beans used to configure a page's header.
 * @author luis
 */
public interface HeaderConfigurer {

     /**
     * Makes the actual configuration of the header.
      * @param mav The ModelAndView object that will carry the configuration.
      */
    public void configure(ModelAndView mav);

    /**
     * Adds a CSS stylesheet.
     * @param cssFilename The stylesheet's finename, without the extension.
     */
    public void useCssFile(String cssFilename);

    /**
     * Get the configured CSS stylesheets.
     * @return A list with the configured CSS stylesheets.
     */
    public List<String> getCssFiles();

    /**
     * Includes a Javascript file in the page.
     * @param jsFilename The script's filename, without the extension.
     */
    public void useJsFile(String jsFilename);

    /**
     * Get the included JavaScript files.
     * @return A list with the included scripts.
     */
    public List<String> getJsFiles();
    /**
     * Includes a Javacript piece of code in the page's header.
     * @param script The Javascript code to be included.
     */
    public void addScript(String script);

    /**
     * Get the configured JavaScript pieces of code.
     * @return A list with the code.
     */
    public List<String> getScripts();

    /**
     * Includes a Dojo module in the page.
     * @param dojoModule The module to be included.
     */
    public void addDojoModule(String dojoModule);

    /**
     * Get the configured Dojo modules pieces of code.
     * @return A list with the modules.
     */
    public List<String> getDojoModules();

    /**
     * Add a Javascript piece of code that will be run after
     * the page is loaded.
     * @param code The code to be run after the page is loaded.
     */
    public void addOnLoadScript(String code);

    /**
     * Get the configured scripts to be run after the page is loaded.
     * @return A list with the code to be run after the page is loaded..
     */
    public List<String> getOnLoadScripts();

    /**
     * Adds a DWR js file to the page's header.
     * @param name The file's name, without the extension.
     */
    public void useDWRService(String name);

    /**
     * Get all the DWR javascript files used.
     * @return A <c>List</c> with the filenames.
     */
    public List<String> getDwrScripts();

    /**
     * Sets the page's title.
     * @param title The new page's title.
     */
    public void setTitle(String title);

    /**
     * Gets the page's title.
     * @return The page's title.
     */
    public String getTitle();

    /**
     * Adds an object to the the <c>ModelAndView</c> object configured.
     * @param identifier
     * @param object
     */
    public void addObject(String identifier, Object object);

    /**
     * Adds a dojo bundle Javascript file for loading.
     * @param bundleName The name of the dojo bundle.
     */
    public void addDojoBundle(String bundleName);
}
