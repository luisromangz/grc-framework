
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.model.User;
import com.greenriver.commons.web.services.FormDto;

public abstract class UserFormDto extends FormDto<User>{
    
    public abstract String getUsername();
}
