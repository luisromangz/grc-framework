/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.helpers.header;

import java.util.List;

/**
 * This interface is a contract that must be implemented by all classes that
 * would carry information about the elements that need inclussion in a page's
 * header section.
 * @author luis
 */
public interface HeaderConfiguration {

    /**
     * Adds a CSS stylesheet.
     * @param cssFilename The stylesheet's finename, without the extension.
     */
    void addCssFile(String cssFilename);

    /**
     * Adds a DWR js file to the page's header.
     * @param name The file's name, without the extension.
     */
    void addDwrService(String name);

    /**
     * Adds a dojo bundle Javascript file for loading.
     * @param bundleName The name of the dojo bundle.
     */
    void addDojoBundle(String bundleName);

    /**
     * Includes a Dojo module in the page.
     * @param dojoModule The module to be included.
     */
    void addDojoModule(String dojoModule);

    /**
     * Includes a Javascript file in the page.
     * @param jsFilename The script's filename, without the extension.
     */
    void addJavaScriptFile(String jsFilename);

    /**
     * Add a Javascript piece of code that will be run after
     * the page is loaded.
     * @param code The code to be run after the page is loaded.
     */
    void addOnLoadScript(String code);

    /**
     * Includes a Javacript piece of code in the page's header.
     * @param script The Javascript code to be included.
     */
    void addScript(String script);

    /**
     * Get the configured CSS stylesheets.
     * @return A list with the configured CSS stylesheets.
     */
    List<String> getCssFiles();

    /**
     * Get the configured Dojo modules required by the page.
     * @return A list with the modules.
     */
    List<String> getDojoModules();

    /**
     * Gets the Dojo bundle files required by the page.
     * @return A list with the bundle names.
     */
    List<String> getDojoBundles();

    /**
     * Get all the DWR javascript files used.
     * @return A <c>List</c> with the filenames.
     */
    List<String> getDwrServices();

    /**
     * Get the included JavaScript files.
     * @return A list with the included scripts.
     */
    List<String> getJavaScriptFiles();

    /**
     * Get the configured scripts to be run after the page is loaded.
     * @return A list with the code to be run after the page is loaded..
     */
    List<String> getOnLoadScripts();

    /**
     * Get the configured JavaScript pieces of code.
     * @return A list with the code.
     */
    List<String> getScripts();

    /**
     * Gets the page's title.
     * @return The page's title.
     */
    String getTitle();

    /**
     * Sets the page's title.
     * @param title The new page's title.
     */
    void setTitle(String title);


    /**
     * Sets the CSS files loaded by the page.
     * @param cssFiles A list of CSS file names, without extension.
     */
    void setCssFiles(List<String> cssFiles);

    /**
     * Sets the names of the DWR services used by the page.
     * @param dwrServices A list with the services' names, without extensions.
     */
    void setDwrServices(List<String> dwrServices);

    /**
     * Sets the Dojo javascript bundles used by the page.
     * @param dojoBundles A list containing the javascript Dojo bundles used,
     * without extensions.
     */
    void setDojoBundles(List<String> dojoBundles);

    /**
     * Sets the Dojo modules required by the page.
     * @param dojoModules A list with the full name of the Dojo modules to be
     * loaded (e.g. 'dijit.form.Button').
     */
    void setDojoModules(List<String> dojoModules);

    /**
     * Sets the Javascript files to be loaded by the page.
     * @param javascriptFiles A list with the names of the files to be loaded,
     * without extension.
     */
    void setJavaScriptFiles(List<String> javascriptFiles);

    /**
     * Sets the JavaScript pieces of code to be run after the page is loaded
     * @param onLoadScripts A list of pieces of code to be run after the page
     * is loaded.
     */
    void setOnLoadScripts(List<String> onLoadScripts);

    /**
     * Sets the JavaScript pieces of code to be included in the page.
     * @param scripts A list with of the pieces of code to be included.
     */
    void setScripts(List<String> scripts);
}
