
package com.greenriver.commons.data.dao;

import com.greenriver.commons.data.model.User;
import java.io.Serializable;
import java.util.List;

/**
 * Dao operations for user entities
 * @author luis
 */
public interface UserDao extends Serializable {
    /**
     * Gets an user by his name
     * @param username Name of the user
     * @return The user if it exists or null if not
     */
    public User getByUsername(String username);
    
    /**
     * Gets an user from an existing user entity
     * @param user Entity to get the data to look for the user
     * @return The user if it exists or null if not
     */
    public User get(User user);

    /**
     * Gets the number of existing users
     * @return Number of existing users
     */
    public int getUserCount();

    /**
     * Removes the user
     * @param user User to be removed
     */
    public void remove(User user);

    /**
     * Saves or updates a user entity
     * @param user Entity to save or update
     * @param encodedPassword String with the hash of the password to be stored.
     */
    public void save(User user, String encodedPassword);

    /**
     * Gets all the users
     * @return a list of users
     */
    public List<User> getAllUsers();
}
