
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public interface UserFormDto extends UserDto {
    void copyTo(User user);
}
