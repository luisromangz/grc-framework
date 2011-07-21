package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;

/**
 * Interfaces for creators of UserDto objects.
 * @author luisro
 */
public interface UserDtoFactory <D extends UserDto>  {
   D create (User user);
}
