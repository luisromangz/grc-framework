
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.DataEntity;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserDto implements DataEntity {
    @Override
   public abstract Long getId();
   
   public abstract String getUsername();
}
