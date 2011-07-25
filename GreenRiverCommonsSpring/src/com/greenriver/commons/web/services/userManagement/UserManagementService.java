package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.web.services.CRUDService;
import com.greenriver.commons.web.services.Result;
import java.util.List;
import java.util.Map;
    
/**
 * Operations UserDtoor the user management service
 * @author luis
 */
public interface UserManagementService 
    extends CRUDService<UserDto, UserFormDto>{
    
    /**
     * Creates a new, not persisted, user object
     * @return a new user
     */
    @Override
    public Result<UserFormDto> getNew();
    
    @Override
    public Result<UserFormDto> getForEdit(Long userId);
    
    @Override
    public Result<UserDto> getForView(Long userId);
    
    /**
     * Saves or updates the user entity
     * @param user Entity to save or update
     * @return the Result result oUserDto the operation
     */
    @Override
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
    @Override
    public Result<UserDto> remove(Long userId);
    /**
     * Gets a list with all the users
     * @return the Result result oUserDto the operation
     */
    @Override
    public Result<List<UserDto>> query(QueryArgs query);
    /**
     * Gets a map that matches UserDtoor each role (key) the display name (value).
     * @return a Result result oUserDto the operation
     */
    public Result<Map<String, String>> getRolesMap();
}

