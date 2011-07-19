package com.greenriver.commons.web.services;

import com.greenriver.commons.data.model.User;
import java.util.List;
import java.util.Map;
    
/**
 * Operations for the user management service
 * @author luis
 */
public interface UserManagementService {
    /**
     * Creates a new, not persisted, user object
     * @return a new user
     */
    public User getNewUser();
    /**
     * Saves or updates the user entity
     * @param user Entity to save or update
     * @return the Result result of the operation
     */
    public Result<User> save(User user);
    /**
     * Changes the password for the user
     * @param currentPassword
     * @param newPassword
     * @return the Result result of the operation
     */
    public Result<User> changePassword(String currentPassword, String newPassword);
    /**
     * Removes a user entity
     * @param user The id of the user to be removed
     * @return the Result result of the operation
     */
    public Result<User> remove(long userId);
    /**
     * Gets a list with all the users
     * @return the Result result of the operation
     */
    public Result<List<User>> getUsers();
    /**
     * Gets a map that matches for each role (key) the display name (value).
     * @return a Result result of the operation
     */
    public Result<Map<String, String>> getRolesMap();
}

