package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.web.helpers.session.UserSessionInfo;
import com.greenriver.commons.web.services.Result;
import java.util.List;
import java.util.Map;
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
    private UserDtoFactory formDtoFactory;
    private UserDtoFactory dtoFactory;
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
    public Result getNewUser() {
        Result<UserDto> r = new Result<UserDto>();
        User newUser = new User();
        newUser.setName("");
        newUser.setUsername("");
        newUser.setPassword("");
        newUser.setRoles(new String[]{"ROLE_USER"});
        r.setResult(formDtoFactory.create(newUser));
        return r;
    }

    @Override
    public Result<UserDto> getForForm(Long userId) {
        Result<UserDto> r = new Result<UserDto>();

        try {
            r.setResult(formDtoFactory.create(userDao.getById(userId)));
        } catch (RuntimeException e) {
            r.addErrorMessage("Ocurrió un error en la base de datos.");
        }

        return r;
    }

    @Override
    public Result<UserDto> get(Long userId) {

        Result<UserDto> r = new Result<UserDto>();

        try {
            r.setResult(dtoFactory.create(userDao.getById(userId)));
        } catch (RuntimeException e) {
            r.addErrorMessage("Ocurrió un error en la base de datos.");
        }

        return r;
    }

    private boolean validateUserSaving(UserDto userDto, Result result) {
        FieldsValidationResult validationResult = fieldsValidator.validate(
                userDto);

        if (!validationResult.isValid()) {
            result.setSuccess(false);
            result.addErrorMessages(validationResult.getErrorMessages());
            return false;
        }
        User user = userDto.getUser();
        User existingUser = this.userDao.getByUsername(user.getUsername());

        if (existingUser != null
                && !existingUser.isDeleted()
                && !existingUser.getId().equals(user.getId())) {
            result.addErrorMessage("Ya existe otro usuario con el mismo nombre de usuario.");
        }

        if (existingUser.isDeleted()) {
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
            res.setResult(dtoFactory.create(persistedUser));
        }

        return res;
    }

    @Override
    public Result<UserDto> changePassword(
            String currentPassword,
            String newPassword) {

        String encodedCurrentPassword = passwordEncoder.encodePassword(
                currentPassword, null);

        Result<UserDto> result = new Result<UserDto>();
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

                result.setResult(dtoFactory.create(currentUser));
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
    public Result<List<UserDto>> getUsers() {
        Result<List<UserDto>> result = new Result<List<UserDto>>();
        try {
            List<User> users = userDao.getAllNotDeletedUsers();
            
            result.setResult(Lists.apply(users, new Applicable<User, UserDto>() {

                @Override
                public UserDto apply(User element) {
                    return dtoFactory.create(element);
                }
            }));
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

    @Override
    public Result save(UserDto userDto) {

        Result<UserDto> result = new Result<UserDto>();

        if (!validateUserSaving(userDto, result)) {
            return result;
        }

        User user = userDto.getUser();

        try {
            userDao.save(user,
                    passwordEncoder.encodePassword(user.getPassword(), null));

            UserDto dUser = dtoFactory.create(user);
            result.setResult(dUser);
        } catch (RuntimeException re) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }


        return result;
    }
}