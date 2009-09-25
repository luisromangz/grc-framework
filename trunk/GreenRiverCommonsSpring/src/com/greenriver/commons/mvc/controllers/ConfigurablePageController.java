package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.mvc.configuration.PageConfiguration;
import com.greenriver.commons.mvc.configuration.FormsConfiguration;
import com.greenriver.commons.mvc.configuration.PageToolsConfiguration;
import com.greenriver.commons.mvc.configuration.PropertiesViewConfiguration;
import com.greenriver.commons.mvc.helpers.form.FormBuilder;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.properties.PropertiesViewBuilder;
import com.greenriver.commons.mvc.pageTools.PageTool;
import com.greenriver.commons.mvc.pageTools.PageToolManager;
import com.greenriver.commons.session.UserSessionInfo;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * This controller is intended to be used by configuring it through the Spring
 * configuration file. This allow to define the CSS files, JavaScript files,
 * etc., that are needed by the page whitout code.
 *
 * Classes specializing this one can override the <c>handleRequestInternal<c>
 * method, grabbing the <c>ModelAndView<c> object it returns (which is fully
 * configured) and add to it whatever extra objects are needed by the view.
 * @author luis
 */
public class ConfigurablePageController extends AbstractController
        implements PropertiesViewConfiguration,
        FormsConfiguration,
        HeaderConfiguration,
        PageToolsConfiguration,
        CustomizableHandleRequest {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private HeaderConfigurer headerConfigurer;
    private FormBuilder formBuilder;
    private PropertiesViewBuilder propertiesViewBuilder;
    private PageConfiguration pageConfiguration;
    private String viewName;
    private UserSessionInfo userSessionInfo;
    private PageToolManager pageToolManager;
    // </editor-fold>

    public ConfigurablePageController() {
        // We intialize the pageConfiguration object;
        pageConfiguration = new PageConfiguration();
    }

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        ModelAndView mav = new ModelAndView(viewName);

        headerConfigurer.setCssFiles(pageConfiguration.getCssFiles());
        headerConfigurer.addDojoBundles(pageConfiguration.getDojoBundles());
        headerConfigurer.addDojoModules(pageConfiguration.getDojoModules());
        headerConfigurer.setDwrServices(pageConfiguration.getDwrServices());
        headerConfigurer.setJavaScriptFiles(
                pageConfiguration.getJavaScriptFiles());
        headerConfigurer.setOnLoadScripts(pageConfiguration.getOnLoadScripts());
        headerConfigurer.setScripts(pageConfiguration.getScripts());
        headerConfigurer.setTitle(pageConfiguration.getTitle());

        configureFormEntities(this.getFormEntities(), mav, null);

        configurePropertiesView(this.getPropertiesView(), mav, null);

        configurePageTools(mav);

        headerConfigurer.configure(mav);

        if (this.userSessionInfo != null) {
            mav.addObject("userSessionInfo", this.userSessionInfo);
        }

        customHandleRequest(request, response, mav);

        // We create the page's dojo-bundle profile.
        createBundleProfile(request);


        return mav;
    }

    private void createBundleProfile(HttpServletRequest request) 
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        String path = request.getSession().getServletContext().getRealPath("");
        path += "/" + request.getServletPath() + "-dojo-bundle.profile.js";

        String profileContent =
                "dependencies={\nstripConsole:'normal',\n" +
                "layers:[{\nname:'%s',\n dependencies: [\n'%s'\n]\n}],\n " +
                "prefixes:[\n['dijit','../dijit'],\n['dojox','../dojox'],\n['grc','../grc']\n]\n}";

        // We remove the initial slash and replace the dots with hyphens.
        String fileName = request.getServletPath().substring(1);
        fileName = fileName.replace('.', '-');
        profileContent = String.format(profileContent,
                fileName+"-dojo-bundle.js",
                Lists.join(headerConfigurer.getDojoModules(), "',\n'"));

        OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(path), "UTF-8");

        out.write(profileContent);
        out.close();
    }

    public void customHandleRequest(HttpServletRequest request,
            HttpServletResponse response, ModelAndView mav) throws Exception {
    }

    // <editor-fold defaultstate="collapsed" desc="Page properties configuration methods">
    /**
     * Configures forms to edit entities from a map     
     * @param configuration
     * @param mav
     * @param prefix Prefix to append to the name of the form
     * @throws ClassNotFoundException
     */
    protected void configureFormEntities(
            Map<String, String> configuration,
            ModelAndView mav,
            String prefix)
            throws ClassNotFoundException {

        if (formBuilder == null && configuration.size() > 0) {
            throw new IllegalStateException(
                    "Must configure formBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        for (String formId : configuration.keySet()) {
            String className = configuration.get(formId);
            Class entityClass = Class.forName(className);
            formBuilder.addForm(prefix + formId, mav);
            formBuilder.addFieldsFromModel(entityClass);
        }
    }

    /**
     * Configures properties view from a map     
     * @param configuration 
     * @param mav
     * @param prefix Name prefix for the generated elements
     */
    protected void configurePropertiesView(Map<String, Object> configuration,
            ModelAndView mav,
            String prefix) {
        Object value = null;

        if (propertiesViewBuilder == null && configuration.size() > 0) {
            throw new IllegalStateException(
                    "Must configure propertiesViewBuilder for this controller.");
        }

        if (prefix == null) {
            prefix = "";
        }

        for (String propsViewId : configuration.keySet()) {
            propertiesViewBuilder.addPropertiesView(prefix + propsViewId, mav);
            value = configuration.get(propsViewId);

            if (value instanceof String) {
                propertiesViewBuilder.addPropertyViewsFromModel((String) value);
            } else if (value instanceof Map) {
                propertiesViewBuilder.addPropertyViewFromConfiguration(
                        (Map<String, Object>) value);
            } else {
                throw new IllegalArgumentException(
                        "Invalid type for map value of key '" + propsViewId +
                        "'");
            }
        }
    }

    private void configurePageTools(ModelAndView mav) throws ClassNotFoundException {
        List<String> dialogJspFiles = new ArrayList<String>();
        List<String> setupJspFiles = new ArrayList<String>();

        if (pageToolManager != null) {

            for (PageTool pageTool : this.pageToolManager.getTools()) {

                dialogJspFiles.addAll(addPathPrefixToFileNames(
                        "tools/" + pageTool.getName(),
                        pageTool.getDialogJspFiles()));

                dialogJspFiles.addAll(addPathPrefixToFileNames(
                        "tools/" + pageTool.getName(),
                        pageTool.getSetupPaneJspFiles()));

                headerConfigurer.getJavaScriptFiles().addAll(addPathPrefixToFileNames(
                        "tools/" + pageTool.getName(),
                        pageTool.getJavaScriptFiles()));

                headerConfigurer.getCssFiles().addAll(addPathPrefixToFileNames(
                        pageTool.getName(),
                        pageTool.getCssFiles()));

                headerConfigurer.getDojoBundles().addAll(
                        pageTool.getDojoBundles());

                headerConfigurer.getDojoModules().addAll(
                        pageTool.getDojoModules());
                headerConfigurer.getDwrServices().addAll(
                        pageTool.getDwrServices());
                headerConfigurer.getOnLoadScripts().addAll(
                        pageTool.getOnLoadScripts());
                headerConfigurer.getScripts().addAll(pageTool.getScripts());

                //Forms ids are prefixed with the task name
                configureFormEntities(pageTool.getFormEntities(), mav,
                        pageTool.getName() + "_");

                configurePropertiesView(pageTool.getPropertiesView(), mav,
                        pageTool.getName() + "_");
            }

        }

        mav.addObject("toolsDialogJspFiles", dialogJspFiles);
        mav.addObject("toolsSetupJspFiles", setupJspFiles);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Facade methods">
    /**
     * @param headerConfigurer the headerConfigurer to set
     */
    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer) {
        this.headerConfigurer = headerConfigurer;
    }

    public HeaderConfigurer getHeaderConfigurer() {
        return headerConfigurer;
    }

    /**
     * @param formBuilder the formBuilder to set
     */
    public void setFormBuilder(FormBuilder formBuilder) {
        this.formBuilder = formBuilder;
    }

    public FormBuilder getFormBuilder() {
        return formBuilder;
    }

    public void setPropertiesViewBuilder(PropertiesViewBuilder propertiesViewBuilder) {
        this.propertiesViewBuilder = propertiesViewBuilder;
    }

    public PropertiesViewBuilder getPropertiesViewBuilder() {
        return propertiesViewBuilder;
    }

    /**
     * The name of the view that is used to render the page that is managed
     * by the controller.
     * @param viewName the view to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Sets a page configuration object containing the configuration of
     * the page regarding JavaScript files, CSS files, etc.
     * @param pageConfiguration the pageConfiguration to set
     */
    public void setPageConfiguration(PageConfiguration pageConfiguration) {
        this.pageConfiguration = pageConfiguration;
    }

    /**
     * Adds an entity's name so a form capable of editing the object is created
     * automagically.
     * @param entityName The name of the entity the form will be created for.
     */
    public void addFormEntity(String id, String entityName) {
        pageConfiguration.addFormEntity(id, entityName);
    }

    /**
     * Gets the names of the entities that will have editing forms created for.
     * @return A list containing the entity names.
     */
    public Map<String, String> getFormEntities() {
        return pageConfiguration.getFormEntities();
    }

    /**
     * Sets the list of entity names for which forms are going to be created.
     * @param formEntities A list of entity names.
     */
    public void setFormEntities(Map<String, String> formEntities) {
        pageConfiguration.setFormEntities(formEntities);
    }

    /**
     * Adds a CSS file name so the header is configured to include it.
     * @param cssFilename The name (without the extension) of the CSS file.
     */
    public void addCssFile(String cssFilename) {
        pageConfiguration.addCssFile(cssFilename);
    }

    /**
     * Adds the name of a DWRService so it will be configured for its use in
     * the page.
     * @param name The name of the service.
     */
    public void addDwrService(String name) {
        pageConfiguration.addDwrService(name);
    }

    /**
     * Adds a Dojo JavaScript bundle file so its loaded by the page.
     * @param bundleName The bundle file name (without its extension).
     */
    public void addDojoBundle(String bundleName) {
        pageConfiguration.addDojoBundle(bundleName);
    }

    /**
     * Adds a Dojo module so its loaded by the page using a <c>dojo.require(..)</c>
     * JavaScript statment.
     * @param dojoModule The full name (including path) of the Dojo module
     * being loaded (e.g. dijit.form.Button).
     */
    public void addDojoModule(String dojoModule) {
        pageConfiguration.addDojoModule(dojoModule);
    }

    /**
     * Adds a JavaScript file name so the file is loaded by the page.
     * @param jsFilename The name of the Javascript file, without extension.
     */
    public void addJavaScriptFile(String jsFilename) {
        pageConfiguration.addJavaScriptFile(jsFilename);
    }

    /**
     * Adds a piece of Javascript code so its executed after the page is loaded.
     * @param code The piece of code to be executed.
     */
    public void addOnLoadScript(String code) {
        pageConfiguration.addOnLoadScript(code);
    }

    /**
     * Adds a piece of JavaScript code so its inclued. in the page.
     * @param script The piece of code to be included.
     */
    public void addScript(String script) {
        pageConfiguration.addScript(script);
    }

    /**
     * Gets the names of the CSS files considered for use in the page.
     * @return A list of the names of the CSS files, without extensions.
     */
    public List<String> getCssFiles() {
        return pageConfiguration.getCssFiles();
    }

    /**
     * Gets the Dojo modules that will be loaded.
     * @return A list with the names of the Dojo modules to be 'required'.
     */
    public List<String> getDojoModules() {
        return pageConfiguration.getDojoModules();
    }

    /**
     * Gets the names of the Dojo Javascript bundle files used in the page.
     * @return A list with the names of the Javascript files, without the extension.
     */
    public List<String> getDojoBundles() {
        return pageConfiguration.getDojoBundles();
    }

    /**
     * Gets the names of the DWR services that will be used by the page.
     * @return A list with the services' names.
     */
    public List<String> getDwrServices() {
        return pageConfiguration.getDwrServices();
    }

    /**
     * Gets the names of the JavaScript files included in the page.
     * @return A list with the names of the files, without the extension.
     */
    public List<String> getJavaScriptFiles() {
        return pageConfiguration.getJavaScriptFiles();
    }

    /**
     * Gets the pieces of Javascript code that will be loaded after the page
     * finishes loading.
     * @return A list with the pieces of code.
     */
    public List<String> getOnLoadScripts() {
        return pageConfiguration.getOnLoadScripts();
    }

    /**
     * Gets the pieces of Javascript code included in the page.
     * @return A list with the pieces of code.
     */
    public List<String> getScripts() {
        return pageConfiguration.getScripts();
    }

    /**
     * Gets the title of the page.
     * @return The page's title.
     */
    public String getTitle() {
        return pageConfiguration.getTitle();
    }

    /**
     * Sets the title of the page.
     * @param title The page's title.
     */
    public void setTitle(String title) {
        pageConfiguration.setTitle(title);
    }

    /**
     * Sets the names of the CSS files to be loaded by the page.
     * @param cssFiles A list with the CSS filenames, without extensions.
     */
    public void setCssFiles(List<String> cssFiles) {
        pageConfiguration.setCssFiles(cssFiles);
    }

    /**
     * Sets the names of the DWR services to be used by the page.
     * @param dwrServices A list of the DWR service names.
     */
    public void setDwrServices(List<String> dwrServices) {
        pageConfiguration.setDwrServices(dwrServices);
    }

    /**
     * Sets the names of the Dojo JavaScript bundle files to be loaded
     * by the page.
     * @param dojoBundles The name of the javascript bundle files, without
     * extensions.
     */
    public void setDojoBundles(List<String> dojoBundles) {
        pageConfiguration.setDojoBundles(dojoBundles);
    }

    /**
     * Sets the Dojo modules required by the page.
     * @param dojoModules A list containing the Dojo module names
     * (e.g. dijit.form.Button) required by the page.
     */
    public void setDojoModules(List<String> dojoModules) {
        pageConfiguration.setDojoModules(dojoModules);
    }

    /**
     * Sets the JavaScript files names to be loaded by the page.
     * @param javascriptFiles A list with the names of the files (without extensions).
     */
    public void setJavaScriptFiles(List<String> javascriptFiles) {
        pageConfiguration.setJavaScriptFiles(javascriptFiles);
    }

    /**
     * Sets the pieces of JavaScript code that will be run after the page is
     * loaded.
     * @param onLoadScripts The pieces of code to be run after the page finishes
     * loading.
     */
    public void setOnLoadScripts(List<String> onLoadScripts) {
        pageConfiguration.setOnLoadScripts(onLoadScripts);
    }

    /**
     * Sets the pieces of JavaScript code that will be include in the page.
     * @param scripts A list of pieces of JavaScript code.
     */
    public void setScripts(List<String> scripts) {
        pageConfiguration.setScripts(scripts);
    }

    /**
     * @return the userSessionInfo
     */
    public UserSessionInfo getUserSessionInfo() {
        return userSessionInfo;
    }

    /**
     * @param userSessionInfo the userSessionInfo to set
     */
    public void setUserSessionInfo(UserSessionInfo userSessionInfo) {
        this.userSessionInfo = userSessionInfo;
    }

    public void addPropertiesView(String id, Object configuration) {
        this.pageConfiguration.addPropertiesView(id, configuration);
    }

    public void setPropertiesView(Map<String, Object> configuration) {
        this.pageConfiguration.setPropertiesView(configuration);
    }

    public Map<String, Object> getPropertiesView() {
        return pageConfiguration.getPropertiesView();
    }

    /**
     * @return the pageToolManager
     */
    public PageToolManager getPageToolManager() {
        return pageToolManager;
    }

    /**
     * @param pageToolManager the pageToolManager to set
     */
    public void setPageToolManager(PageToolManager pageToolManager) {
        this.pageToolManager = pageToolManager;
    }

    public void addDojoBundles(List<String> dojoBundles) {
        pageConfiguration.addDojoBundles(dojoBundles);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Auxiliary methods">
    protected List<String> addPathPrefixToFileNames(
            String path,
            List<String> filenames) {

        ArrayList<String> prefixedFileNames = new ArrayList<String>();

        for (String fileName : filenames) {
            prefixedFileNames.add(String.format("%s/%s", path, fileName));
        }

        return prefixedFileNames;
    }

    public void addDojoModules(List<String> dojoModules) {
        pageConfiguration.addDojoModules(dojoModules);
    }
    // </editor-fold>
}
