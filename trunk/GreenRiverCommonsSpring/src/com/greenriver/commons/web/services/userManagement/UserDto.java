
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.Labelled;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserDto implements Labelled {
   public abstract Long getId();
   
   public abstract String getUsername();
}
