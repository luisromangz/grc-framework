
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.services.FormDto;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserFormDto extends FormDto<User>{
    
    public abstract String getUsername();
}
