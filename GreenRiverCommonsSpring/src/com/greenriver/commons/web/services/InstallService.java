
package com.greenriver.commons.web.services;

import com.greenriver.commons.data.model.User;

/**
 *
 * @author luis
 */
public interface InstallService {
    /**
     * Checks the contents of the key file
     * @return the result of the operation
     */
    public ServiceResult checkKeyFile();
    /**
     * Adds a new user with a role to be administrator
     * @param adminUser User to add
     * @return the result of the operation
     */
    public ServiceResult addAdminUser(User adminUser);
}
