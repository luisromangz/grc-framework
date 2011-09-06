
package com.greenriver.commons.data.dao;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.model.User;
import java.io.Serializable;
import java.util.List;

/**
 * Dao operations for user entities
 * @author luis
 */
public interface UserDao extends CRUDDao<User,QueryArgs>, Serializable {
    /**
     * Gets an user by his name
     * @param username Name of the user
     * @return The user if it exists or null if not
     */
    public User getByUsername(String username);
    
    /**
     * Gets the number of existing users
     * @return Number of existing users
     */
    public int getUserCount();

    /**
     * Saves or updates a user entity
     * @param user Entity to save or update
     * @param encodedPassword String with the hash of the password to be stored.
     */
    public void save(User user, String encodedPassword);
}
