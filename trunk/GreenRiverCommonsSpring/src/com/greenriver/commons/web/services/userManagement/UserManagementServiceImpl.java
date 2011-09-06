package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.ErrorMessagesException;
import com.greenriver.commons.Strings;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.mailing.MailServerConfig;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.validation.ValidationResult;
import com.greenriver.commons.mailing.MailSendingHelper;
import com.greenriver.commons.mailing.MailSendingHelper.BackgroundMailer;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.web.helpers.session.UserSessionInfo;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.crud.CRUDServiceImpl;
import com.greenriver.commons.web.services.Result;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * This class implements <c>UserManagementService</c> .
 * @author luis
 */
public abstract class UserManagementServiceImpl<E extends User, D extends UserDto, F extends UserFormDto>
        extends CRUDServiceImpl<E, D, F, QueryArgs>
        implements UserManagementService<D, F> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private UserSessionInfo userSessionInfo;
    private PasswordEncoder passwordEncoder;
    private RoleManager roleManager;
    private MailSendingHelper mailSendingHelper;
    private String appTitle;
    private static final String PASSWORD_CHANGE_MAIL_TEMPLATE = "generateNewPasswordMailTemplate.html";
    private static final String NEW_USER_MAIL_TEMPLATE = "newUserMailTemplate.html";

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public void setUserSessionInfo(UserSessionInfo userSessionInfo) {
        this.userSessionInfo = userSessionInfo;
    }

    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     * @return the mailSendingHelper
     */
    public MailSendingHelper getMailSendingHelper() {
        return mailSendingHelper;
    }

    /**
     * @param mailSendingHelper the mailSendingHelper to set
     */
    public void setMailSendingHelper(MailSendingHelper mailSendingHelper) {
        this.mailSendingHelper = mailSendingHelper;
    }

    /**
     * @return the appTitle
     */
    public String getAppTitle() {
        return appTitle;
    }

    /**
     * @param appTitle the appTitle to set
     */
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Service methods">

    @Override
    protected boolean validateRemoval(E entity, Result res) {
        if (!super.validateRemoval(entity, res)) {
            return false;
        }

        //Don't let a user to be removed if it is the current user
        if (entity.getId().equals(userSessionInfo.getCurrentUser().getId())) {
            res.setSuccess(false);
            res.formatErrorMessage("El usuario no puede borrarse a si mismo.");
            return false;
        }

        return true;
    }

    @Override
    public PagedResult<D> query(QueryArgs args) {
        return queryInternal(args);
    }

    @Override
    public Result<D> changePassword(PasswordChangeData changeData) {

        String encodedCurrentPassword = passwordEncoder.encodePassword(
                changeData.getCurrentPassword(), null);

        Result<D> result = new Result<D>();
        User currentUser = userSessionInfo.getCurrentUser();
        if (encodedCurrentPassword.equals(currentUser.getPassword())) {
            String newPassword = changeData.getNewPassword();
            if (newPassword.length() < 6) {
                result.setSuccess(false);
                result.formatErrorMessage(
                        "La nueva contraseña debe tener 6 o más caracteres");
            } else {
                currentUser.setPassword(newPassword);
                ((UserDao) getDao()).save(
                        currentUser,
                        passwordEncoder.encodePassword(newPassword, null));

                result.setResult(getDto((E)currentUser, false));
            }

        } else {
            // The password isn't the same, we refuse to change the password.
            result.setSuccess(false);
            result.formatErrorMessage(
                    "La <b>contraseña actual</b> no coincide con la del "
                    + "usuario que comenzó la sesión.");
        }

        return result;
    }

    @Override
    public Result<Map<String, String>> getRolesMap() {
        Result<Map<String, String>> result =
                new Result<Map<String, String>>();

        result.setResult(roleManager.getRoleMap());

        return result;
    }

    @Override
    protected Result<D> saveInternal(F userDto) {

        Result<D> result = new Result<D>();

        E user = validateUserSaving(userDto, result);
        if (!result.isSuccess()) {
            return result;
        }


        boolean isNewWithoutPwd = user.getId() == null
                && Strings.isNullOrEmpty(user.getPassword());
        String newPassword = null;
        if (isNewWithoutPwd) {
            // We create a new password
            newPassword = createRandomPassword();
            user.setPassword(createRandomPassword());
            // New users can access the app by default.
            user.addRole("ROLE_USER");
        }

        String encodedPassword = passwordEncoder.encodePassword(user.getPassword(), null);
        try {
            ((UserDao) getDao()).save(user, encodedPassword);
        } catch (RuntimeException re) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        if (newPassword != null && !sendPasswordEmail(user, newPassword, false, result)) {
            return result;
        }

        D dUser = getDto(user, true);
        dUser.setNewEntity(userDto.getId() == null);
        result.setResult(dUser);

        return result;
    }

    private boolean sendPasswordEmail(
            User user, String password, boolean changePassword, Result result) {

        String templateName = changePassword
                ? PASSWORD_CHANGE_MAIL_TEMPLATE
                : NEW_USER_MAIL_TEMPLATE;
        String mailTemplate = null;
        try {
            mailTemplate = Strings.fromInputStream(
                    UserManagementServiceImpl.class.getResourceAsStream(templateName));
        } catch (IOException ex) {
        }

        mailTemplate = mailTemplate.replaceAll("%USERNAME%", user.getUsername());
        mailTemplate = mailTemplate.replaceAll("%NAME%", user.getName());
        mailTemplate = mailTemplate.replaceAll("%PASSWORD%", password);
        mailTemplate = mailTemplate.replaceAll("%APP_NAME%", this.getAppTitle().trim());

        Mail mail = new Mail();
        mail.setBody(mailTemplate);
        mail.setSubject("Información de acceso a " + this.getAppTitle());
        mail.setTo(user.getEmailAddress());

        // We retrive the config here so we have an active transaction.
        MailServerConfig config = null;
        try {
            config = mailSendingHelper.getMailServerConfig();
        } catch (ErrorMessagesException ex) {
            result.addErrorMessages(ex.getMessages());
            return false;
        }

        BackgroundMailer mailer = new BackgroundMailer(mail, config, getMailSendingHelper());
        Thread t = new Thread(mailer);
        t.start();

        return true;
    }

    @Override
    public Result generateNewPassword(Long id) {
        Result result = new Result();

        E user = getById(id, result);
        if (!result.isSuccess()) {
            return result;
        }

        String newPassword = createRandomPassword();

        user.setPassword(passwordEncoder.encodePassword(newPassword, null));

        try {
            getDao().save(user);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        sendPasswordEmail(user, newPassword, false, result);


        return result;
    }

    private String createRandomPassword() {
        return RandomStringUtils.random(6, true, true).toLowerCase();
    }

    private E validateUserSaving(UserFormDto userDto, Result result) {
        ValidationResult validationResult =
                getFieldsValidator().validate(userDto);

        if (!validationResult.isValid()) {
            result.addErrorMessages(validationResult.getErrorMessages());
            return null;
        }


        User existingUser = ((UserDao) getDao()).getByUsername(userDto.getUsername());

        if (existingUser != null
                && !existingUser.isDeleted()
                && !existingUser.getId().equals(userDto.getId())) {

            result.addErrorMessage("Ya existe otro usuario con el mismo nombre de usuario.");
            return null;
        }


        if (userDto.getId() == null) {
            existingUser = getNewEntity(result);
            if(!result.isSuccess()) {
                return null;
            }
        } else {
            existingUser = getDao().getById(userDto.getId());
        }

        userDto.copyTo(existingUser);

        return (E)existingUser;
    }

    @Override
    public Result<D> toggleAccess(Long id) {
        Result<D> result = new Result<D>();

        E user = getById(id, result);
        if (!result.isSuccess()) {
            return result;
        }

        if (userSessionInfo.getCurrentUser().equals(user)) {
            result.addErrorMessage("El usuario actual no puede desactivarse a sí mismo.");
            return result;
        }


        if (user.hasRole("ROLE_USER")) {
            user.removeRole("ROLE_USER");
        } else {
            user.addRole("ROLE_USER");
        }

        try {
            getDao().save(user);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        result.setResult(getDto(user, true));

        return result;
    }
    // </editor-fold>
}
