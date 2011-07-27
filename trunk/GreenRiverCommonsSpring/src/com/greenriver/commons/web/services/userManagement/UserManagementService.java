package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.web.services.CRUDService;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.Result;
import java.util.Map;
    
/**
 * Operations UserDtoor the user management service
 * @author luis
 */
public interface UserManagementService 
    extends CRUDService<UserDto, UserFormDto>{

    /**
     * Changes the password UserDtoor the user
     * @return the Result result oUserDto the operation
     */
    public Result<UserDto> changePassword(PasswordChangeData changedata);

    /**
     * Gets a map that matches UserDtoor each role (key) the display name (value).
     * @return a Result result oUserDto the operation
     */
    public Result<Map<String, String>> getRolesMap();
    
    public Result<UserDto> toggleAccess(Long id);
}

