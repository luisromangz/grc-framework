
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.Labelled;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public interface UserDto extends Labelled {
   Long getId();
   
   public String getUsername();
}
