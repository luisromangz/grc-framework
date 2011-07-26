
package com.greenriver.commons.web.services.userManagement;

import com.greenriver.commons.data.DataEntity;

/**
 * Interface that UserDtos need to implement.
 * @author luisro
 */
public abstract class UserDto implements DataEntity {
    private boolean newEntity=false;
    
    
    @Override
   public abstract Long getId();
   
   public abstract String getUsername();
   
   public boolean isNewEntity() {
       return newEntity;
   }
   
   public void setNewEntity(boolean newEntity) {
       this.newEntity = newEntity;
   }
}

