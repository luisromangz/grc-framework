package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.web.services.crud.CRUDService;
import com.greenriver.commons.web.services.Result;
import java.util.Map;
    
/**
 * Operations UserDtoor the user management service
 * @author luis
 */
public interface UserManagementService <D extends UserDto, F extends UserFormDto>
    extends CRUDService<D, F>{

    /**
     * Changes the password UserDtoor the user
     * @return the Result result oUserDto the operation
     */
    public Result<D> changePassword(PasswordChangeData changedata);

    /**
     * Gets a map that matches UserDtoor each role (key) the display name (value).
     * @return a Result result oUserDto the operation
     */
    public Result<Map<String, String>> getRolesMap();
    
    public Result<D> toggleAccess(Long id);
    
    public Result generateNewPassword(Long id);
}

