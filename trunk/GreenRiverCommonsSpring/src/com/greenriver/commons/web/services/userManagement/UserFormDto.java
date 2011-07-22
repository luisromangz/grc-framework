
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserFormDto extends UserDto {
    public abstract void copyTo(User user);
}
