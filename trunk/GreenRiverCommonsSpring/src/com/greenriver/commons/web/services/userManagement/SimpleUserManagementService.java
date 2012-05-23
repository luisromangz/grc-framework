/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.services.Result;

/**
 *
 * @author luisro
 */
public class SimpleUserManagementService 
 extends UserManagementServiceImpl<User, UserDto, UserFormDto> {

    @Override
    public Result<UserDto> save(UserFormDto item) {
       return this.saveInternal(item);
    }
    
}
