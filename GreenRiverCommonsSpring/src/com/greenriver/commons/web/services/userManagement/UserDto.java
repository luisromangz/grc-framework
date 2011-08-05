package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.services.Dto;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserDto extends Dto<User> {
    
    public abstract String getUsername();
}
