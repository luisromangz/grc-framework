/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.validation;

import com.greenriver.commons.roleManagement.RoleManagerClient;

/**
 *
 * @author luis
 */
public interface FieldsValidator extends RoleManagerClient {
    public FieldsValidationResult validate(Object object);
    
}
