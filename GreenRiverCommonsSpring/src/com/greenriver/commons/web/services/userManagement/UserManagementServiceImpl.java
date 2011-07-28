package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.ErrorMessagesException;
import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.mailing.Mail;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.mailing.MailSendingHelper;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.web.helpers.session.UserSessionInfo;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.Result;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * This class implements <c>UserManagementService</c> .
 * @author luis
 */
public class UserManagementServiceImpl
        implements UserManagementService {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private UserSessionInfo userSessionInfo;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private FieldsValidator fieldsValidator;
    private RoleManager roleManager;
    private Class<UserFormDto> formDtoClass;
    private Class<UserDto> dtoClass;
    private MailSendingHelper mailSendingHelper;
    private String appTitle;
    private static final String PASSWORD_CHANGE_MAIL_TEMPLATE = "generateNewPasswordMailTemplate.html";
    private static final String NEW_USER_MAIL_TEMPLATE = "newUserMailTemplate.html";

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public void setUserSessionInfo(UserSessionInfo userSessionInfo) {
        this.userSessionInfo = userSessionInfo;
    }

    public void setFieldsValidator(FieldsValidator validator) {
        this.fieldsValidator = validator;
    }

    public void setUserDao(UserDao dao) {
        this.userDao = dao;
    }

    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     * @return the formDtoClass
     */
    public Class<UserFormDto> getFormDtoClass() {
        return formDtoClass;
    }

    /**
     * @param formDtoClass the formDtoClass to set
     */
    public void setFormDtoClass(Class<UserFormDto> formDtoClass) {
        this.formDtoClass = formDtoClass;
    }

    /**
     * @return the dtoClass
     */
    public Class<UserDto> getDtoClass() {
        return dtoClass;
    }

    /**
     * @param dtoClass the dtoClass to set
     */
    public void setDtoClass(Class<UserDto> dtoClass) {
        this.dtoClass = dtoClass;
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
     * @return the formDtoFactory
     */
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Service methods">
    @Override
    public Result<UserFormDto> getNew() {
        Result<UserFormDto> r = new Result<UserFormDto>();

        UserFormDto dto = getUserFormDto(new User());


        r.setResult(dto);
        return r;
    }

    @Override
    public Result<UserFormDto> getForEdit(Long userId) {
        Result<UserFormDto> r = new Result<UserFormDto>();

        User user = getUserById(userId, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(getUserFormDto(user));
        return r;
    }

    @Override
    public Result<UserDto> getForView(Long userId) {
        Result<UserDto> r = new Result<UserDto>();
        User user = getUserById(userId, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(getUserDto(user, false));
        return r;
    }

    @Override
    public Result<UserDto> remove(Long userId) {
        Result<UserDto> res = new Result<UserDto>();

        //Don't let a user to be removed if it is the current user
        if (userId.equals(userSessionInfo.getCurrentUser().getId())) {
            res.setSuccess(false);
            res.formatErrorMessage("El usuario no puede borrarse a si mismo");
            return res;
        }

        User persistedUser = userDao.getById(userId);

        if (persistedUser == null) {

            res.addErrorMessage("El usuario no existe.");
        } else {
            // Flag as deleted
            persistedUser.setDeleted(true);
            // We are changing the flag for an existing user so we don't
            // need to provide the encoded password.
            userDao.save(persistedUser, null);
            res.setResult(getUserDto(persistedUser, true));
        }

        return res;
    }

    @Override
    public Result<UserDto> changePassword(PasswordChangeData changeData) {

        String encodedCurrentPassword = passwordEncoder.encodePassword(
                changeData.getCurrentPassword(), null);

        Result<UserDto> result = new Result<UserDto>();
        User currentUser = userSessionInfo.getCurrentUser();
        if (encodedCurrentPassword.equals(currentUser.getPassword())) {
            String newPassword = changeData.getNewPassword();
            if (newPassword.length() < 6) {
                result.setSuccess(false);
                result.formatErrorMessage(
                        "La nueva contraseña debe tener 6 o más caracteres");
            } else {
                currentUser.setPassword(newPassword);
                userDao.save(currentUser, passwordEncoder.encodePassword(
                        newPassword, null));

                result.setResult(getUserDto(currentUser, false));
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
    public PagedResult<UserDto> query(QueryArgs args) {
        PagedResult<UserDto> result = new PagedResult<UserDto>();

        List<User> users = new ArrayList<User>();

        try {
            result.setTotal(userDao.query(args, users));
        } catch (RuntimeException ex) {
            result.formatErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        result.setResult(Lists.apply(users, new Applicable<User, UserDto>() {

            @Override
            public UserDto apply(User element) {
                return getUserDto(element, true);
            }
        }));

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
    public Result<UserDto> save(UserFormDto userDto) {

        Result<UserDto> result = new Result<UserDto>();

        User user = validateUserSaving(userDto, result);
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
            userDao.save(user, encodedPassword);
        } catch (RuntimeException re) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }
        
        if(newPassword!=null && !sendPasswordEmail(user, newPassword, result, false)){
            return result;
        }

        UserDto dUser = getUserDto(user, true);
        dUser.setNewEntity(userDto.getId() == null);
        result.setResult(dUser);

        return result;
    }

    private boolean sendPasswordEmail(User user, String password, Result result, boolean changePassword) {
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
        try {
            mailSendingHelper.sendHtmlMail(mail);
        } catch (ErrorMessagesException ex) {
            result.addErrorMessages(ex.getMessages());
            return false;
        }

        return true;
    }

    @Override
    public Result generateNewPassword(Long id) {
        Result result = new Result();

        User user = getUserById(id, result);
        if (!result.isSuccess()) {
            return result;
        }

        String newPassword = createRandomPassword();
        
        user.setPassword(passwordEncoder.encodePassword(newPassword, null));

        try {
            userDao.save(user);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }
        
        sendPasswordEmail(user, newPassword, result, false);
        
        
        return result;
    }

    private String createRandomPassword() {
        return RandomStringUtils.random(6, true, true);
    }

    private User validateUserSaving(UserFormDto userDto, Result result) {
        FieldsValidationResult validationResult = fieldsValidator.validate(
                userDto);

        if (!validationResult.isValid()) {
            result.addErrorMessages(validationResult.getErrorMessages());
            return null;
        }


        User existingUser = this.userDao.getByUsername(userDto.getUsername());

        if (existingUser != null
                && !existingUser.isDeleted()
                && !existingUser.getId().equals(userDto.getId())) {

            result.addErrorMessage("Ya existe otro usuario con el mismo nombre de usuario.");
            return null;
        }


        if (userDto.getId() == null) {
            existingUser = new User();
        } else {
            existingUser = userDao.getById(userDto.getId());
        }

        userDto.copyTo(existingUser);

        return existingUser;
    }

    @Override
    public Result<UserDto> toggleAccess(Long id) {
        Result<UserDto> result = new Result<UserDto>();

        User user = getUserById(id, result);
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
            userDao.save(user);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        result.setResult(getUserDto(user, true));

        return result;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Auxiliary methods">
    private User getUserById(Long userId, Result r) {
        User user = null;
        try {
            user = userDao.getById(userId);

        } catch (RuntimeException e) {
            r.addErrorMessage("Ocurrió un error en la base de datos.");
        }

        if (user == null) {
            r.addErrorMessage("No se encontró el usuario especificado.");
        }
        return user;
    }

    private UserFormDto getUserFormDto(User user) {
        UserFormDto dto = null;
        try {
            dto = getFormDtoClass().newInstance();
        } catch (Exception ex) {
        }

        dto.fromUser(user);
        return dto;
    }

    private UserDto getUserDto(User user, boolean forGrid) {
        UserDto dto = null;
        try {
            dto = getDtoClass().newInstance();
        } catch (Exception ex) {
        }

        dto.fromUser(user, forGrid);
        return dto;
    }
//</editor-fold>

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
}
