package com.greenriver.commons.data.validation;

import com.greenriver.commons.roleManagement.RoleManagerClient;

/**
 *
 * @author luis
 */
public interface FieldsValidator extends RoleManagerClient {
    public ValidationResult validate(Object object);
    
}
