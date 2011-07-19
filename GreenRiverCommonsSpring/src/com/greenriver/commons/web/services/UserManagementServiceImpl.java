package com.greenriver.commons.web.services;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.web.helpers.session.UserSessionInfo;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * This class implements <c>UserManagementService</c> .
 * @author luis
 */
public class UserManagementServiceImpl implements UserManagementService {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private UserSessionInfo userSessionInfo;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private FieldsValidator fieldsValidator;
    private RoleManager roleManager;
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Service methods">
    @Override
    public User getNewUser() {
        User newUser = new User();
        newUser.setName("");
        newUser.setUsername("");
        newUser.setPassword("");
        newUser.setRoles(new String[]{"ROLE_USER"});
        return newUser;
    }

    @Override
    public Result<User> save(User user) {

        Result<User> result = new Result<User>();

        if (!validateUserSaving(user, result)) {
            return result;
        }

        try {
            userDao.save(user,
                    passwordEncoder.encodePassword(user.getPassword(), null));

            result.setResult(user);
        } catch (RuntimeException re) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }


        return result;
    }

    private boolean validateUserSaving(User user, Result result) {
        FieldsValidationResult validationResult = fieldsValidator.validate(
                user);

        if (!validationResult.isValid()) {
            result.setSuccess(false);
            result.addErrorMessages(validationResult.getErrorMessages());
            return false;
        }
        
        User existingUser = this.userDao.getByUsername(user.getUsername());
        
        if(existingUser!=null 
                && !existingUser.isDeleted() 
                && !existingUser.getId().equals(user.getId())) {
            result.addErrorMessage("Ya existe otro usuario con el mismo nombre de usuario.");
        }

        if (user.isDeleted()) {
            result.addErrorMessage("El usuario fue borrado con anterioridad.");
            return false;
        }

        if (user.hasRole("ROLE_ADMIN") && Strings.isNullOrEmpty(user.getEmailAddress())) {
            result.formatErrorMessage(
                    "Un usuario de tipo administrador necesita tener definida"
                    + " una cuenta de correo electrónico.");
            return false;
        }

        return true;
    }

    @Override
    public Result<User> remove(User user) {
        Result<User> res = new Result<User>();

        //Don't let a user to be removed if it is the current user
        if (user.getId() > 0 && user.equals(userSessionInfo.getCurrentUser())) {
            res.setSuccess(false);
            res.formatErrorMessage("El usuario no puede borrarse a si mismo");
            return res;
        }

        User persistedUser = userDao.get(user);

        if (persistedUser == null) {
            
            res.addErrorMessage("El usuario no existe.");
        } else {
            // Flag as deleted
            persistedUser.setDeleted(true);
            // We are changing the flag for an existing user so we don't
            // need to provide the encoded password.
            userDao.save(persistedUser, null);
            res.setResult(user);
        }

        return res;
    }

    @Override
    public Result<User> changePassword(
            String currentPassword,
            String newPassword) {

        String encodedCurrentPassword = passwordEncoder.encodePassword(
                currentPassword, null);

        Result<User> result = new Result<User>();
        User currentUser = userSessionInfo.getCurrentUser();
        if (encodedCurrentPassword.equals(currentUser.getPassword())) {
            if (newPassword.length() < 6) {
                result.setSuccess(false);
                result.formatErrorMessage(
                        "La nueva contraseña debe tener 6 o más caracteres");
            } else {
                currentUser.setPassword(newPassword);
                userDao.save(currentUser, passwordEncoder.encodePassword(
                        newPassword, null));

                result.setResult(currentUser);
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
    public Result<List<User>> getUsers() {
        Result<List<User>> result = new Result<List<User>>();
        try {
            result.setResult(userDao.getAllNotDeletedUsers());
        } catch (Exception ex) {
            result.setSuccess(false);
            result.formatErrorMessage(ex.getMessage());
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
    // </editor-fold>
}