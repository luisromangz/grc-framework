package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.mvc.helpers.form.FormBuilder;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.properties.PropertiesViewBuilder;
import com.greenriver.commons.session.UserSessionInfo;
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
	implements FormsConfiguration, HeaderConfiguration, CustomizableHandleRequest {

    private HeaderConfigurer headerConfigurer;
    private FormBuilder formBuilder;
    private PropertiesViewBuilder propertiesViewBuilder;
    private PageConfiguration pageConfiguration;
    private String viewName;
    private UserSessionInfo userSessionInfo;

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
	headerConfigurer.setDojoBundles(pageConfiguration.getDojoBundles());
	headerConfigurer.setDojoModules(pageConfiguration.getDojoModules());
	headerConfigurer.setDwrServices(pageConfiguration.getDwrServices());
	headerConfigurer.setJavaScriptFiles(pageConfiguration.getJavaScriptFiles());
	headerConfigurer.setOnLoadScripts(pageConfiguration.getOnLoadScripts());
	headerConfigurer.setScripts(pageConfiguration.getScripts());
	headerConfigurer.setTitle(pageConfiguration.getTitle());

	headerConfigurer.configure(mav);

	configureFormEntities(pageConfiguration.getFormEntities(), mav, null);

	configurePropertiesView(
		pageConfiguration.getPropertiesView(),
		mav);

	if (this.userSessionInfo != null) {
	    mav.addObject("userSessionInfo", this.userSessionInfo);
	}

	customHandleRequest(request, response, mav);

	return mav;
    }

    public void customHandleRequest(HttpServletRequest request,
	    HttpServletResponse response, ModelAndView mav) throws Exception {
    }

    /**
     * Configures forms to edit entities from a map
     * @param configuration
     * @param mav
     * @param prefix Prefix to append to the name of the form
     * @throws ClassNotFoundException
     */
    protected void configureFormEntities(Map<String, String> configuration,
	    ModelAndView mav, String prefix) throws ClassNotFoundException {

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
     */
    protected void configurePropertiesView(Map<String, Object> configuration,
	    ModelAndView mav) {
	Object value = null;

	if (propertiesViewBuilder == null && configuration.size() > 0) {
	    throw new IllegalStateException(
		    "Must configure propertiesViewBuilder for this controller.");
	}

	for (String propsViewId : configuration.keySet()) {
	    propertiesViewBuilder.addPropertiesView(propsViewId, mav);
	    value = configuration.get(propsViewId);
	    
	    if (value instanceof String) {
		propertiesViewBuilder.
			addPropertyViewsFromModel((String) value);
	    } else if (value instanceof Map) {
		propertiesViewBuilder.
			addPropertyViewFromConfiguration((Map<String, Object>) value);
	    } else {
		throw new IllegalArgumentException(
			"Invalid type for map value of key '" + propsViewId +
			"'");
	    }
	}
    }

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
}
