package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.web.services.Result;
import java.util.List;
import java.util.Map;
    
/**
 * Operations UserDtoor the user management service
 * @author luis
 */
public interface UserManagementService {
    /**
     * Creates a new, not persisted, user object
     * @return a new user
     */
    public Result<UserDto> getNewUser();
    
    public Result<UserFormDto> getForForm(Long userId);
    public Result<UserDto> get(Long userId);
    
    /**
     * Saves or updates the user entity
     * @param user Entity to save or update
     * @return the Result result oUserDto the operation
     */
    public Result<UserDto> save(UserFormDto userDto);
    /**
     * Changes the password UserDtoor the user
     * @param currentPassword
     * @param newPassword
     * @return the Result result oUserDto the operation
     */
    public Result<UserDto> changePassword(String currentPassword, String newPassword);
    /**
     * Removes a user entity
     * @param user The id oUserDto the user to be removed
     * @return the Result result oUserDto the operation
     */
    public Result<UserDto> remove(Long userId);
    /**
     * Gets a list with all the users
     * @return the Result result oUserDto the operation
     */
    public Result<List<UserDto>> getUsers();
    /**
     * Gets a map that matches UserDtoor each role (key) the display name (value).
     * @return a Result result oUserDto the operation
     */
    public Result<Map<String, String>> getRolesMap();
}

