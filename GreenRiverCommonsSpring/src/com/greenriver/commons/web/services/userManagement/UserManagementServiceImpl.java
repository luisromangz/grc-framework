package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.dao.queryArguments.QueryArgs;
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
    private UserDtoFactory<UserFormDto> formDtoFactory;
    private UserDtoFactory<UserDto> dtoFactory;
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
     * @return the formDtoFactory
     */
    public UserDtoFactory<UserFormDto> getFormDtoFactory() {
        return formDtoFactory;
    }

    /**
     * @param formDtoFactory the formDtoFactory to set
     */
    public void setFormDtoFactory(UserDtoFactory<UserFormDto> formDtoFactory) {
        this.formDtoFactory = formDtoFactory;
    }

    /**
     * @return the dtoFactory
     */
    public UserDtoFactory<UserDto> getDtoFactory() {
        return dtoFactory;
    }

    /**
     * @param dtoFactory the dtoFactory to set
     */
    public void setDtoFactory(UserDtoFactory<UserDto> dtoFactory) {
        this.dtoFactory = dtoFactory;
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
        r.setResult(getFormDtoFactory().create(newUser));
        return r;
    }

    @Override
    public Result<UserFormDto> getForEdit(Long userId) {
        Result<UserFormDto> r = new Result<UserFormDto>();

        User user = getUserById(userId, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(formDtoFactory.create(user));
        return r;
    }

    @Override
    public Result<UserDto> getForView(Long userId) {
        Result<UserDto> r = new Result<UserDto>();
        User user = getUserById(userId, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(dtoFactory.create(user));
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
            res.setResult(getDtoFactory().create(persistedUser));
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

                result.setResult(getDtoFactory().create(currentUser));
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
    public Result<List<UserDto>> query(QueryArgs args) {
        Result<List<UserDto>> result = new Result<List<UserDto>>();
        try {
            List<User> users = userDao.getAllNotDeletedUsers();

            result.setResult(Lists.apply(users, new Applicable<User, UserDto>() {

                @Override
                public UserDto apply(User element) {
                    return getDtoFactory().create(element);
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

    @Override
    public Result<UserDto> save(UserFormDto userDto) {

        Result<UserDto> result = new Result<UserDto>();

        User user = validateUserSaving(userDto, result);
        if (!result.isSuccess()) {
            return result;
        }

        try {
            userDao.save(user,
                    passwordEncoder.encodePassword(user.getPassword(), null));

            UserDto dUser = getDtoFactory().create(user);
            result.setResult(dUser);
        } catch (RuntimeException re) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
        }


        return result;
    }

    private User validateUserSaving(UserDto userDto, Result result) {
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
        }

        if (existingUser.isDeleted()) {
            result.addErrorMessage("El usuario fue borrado con anterioridad.");
            return null;
        }

        return existingUser;
    }
    // </editor-fold>

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
}