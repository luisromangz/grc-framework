package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.mvc.helpers.form.FormBuilderClient;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurerClient;
import com.greenriver.commons.session.InstallHelper;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author luis
 */
public class InstallController extends ConfigurablePageController
        implements HeaderConfigurerClient, FormBuilderClient {

    // Its used to generate the key not a password xD
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private InstallHelper installHelper;
    private String userClass;
    private String pageToRedirectIfInstalled;
    private String keyFileName;

    public InstallController() {
        keyFileName = "key.txt";
        pageToRedirectIfInstalled = "login.htm";
    }

    /**
     * 
     * @param request
     * @param response
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void customHandleRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            ModelAndView modelAndView) throws Exception {

        if (userDao.getUserCount() > 0) {
            response.sendRedirect(this.pageToRedirectIfInstalled);
            return;
        }

        Date now = new Date();
        String key = passwordEncoder.encodePassword(now.toString(), null);

        installHelper.setKey(key);

        String path = request.getSession().getServletContext().getRealPath("");
        installHelper.setKeyFilePath(path + "/" + keyFileName);

        modelAndView.addObject("keyPath", path);
        modelAndView.addObject("key", key);

        getFormBuilder().addForm("adminForm", this.getPageConfiguration(), modelAndView);
        getFormBuilder().addFieldsFromModel(Class.forName(userClass));
        getFormBuilder().removeField("roles");
        getFormBuilder().removeField("enabled");

        getHeaderConfigurer().configure(modelAndView, this.getPageConfiguration());
    }

    /**
     * @param passworEncoder the passworEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passworEncoder) {
        this.passwordEncoder = passworEncoder;
    }

    /**
     * @param userDao the userDao to set
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @param installHelper the installHelper to set
     */
    public void setInstallHelper(InstallHelper installHelper) {
        this.installHelper = installHelper;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    /**
     * @return the keyFileName
     */
    public String getKeyFileName() {
        return keyFileName;
    }

    /**
     * @param keyFileName the keyFileName to set
     */
    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName.trim();
    }

    /**
     * @return the pageToRedirectIfInstalled
     */
    public String getPageToRedirectIfInstalled() {
        return pageToRedirectIfInstalled;
    }

    /**
     * @param pageToRedirectIfInstalled the pageToRedirectIfInstalled to set
     */
    public void setPageToRedirectIfInstalled(String pageToRedirectIfInstalled) {
        this.pageToRedirectIfInstalled = pageToRedirectIfInstalled.trim();
    }
}
