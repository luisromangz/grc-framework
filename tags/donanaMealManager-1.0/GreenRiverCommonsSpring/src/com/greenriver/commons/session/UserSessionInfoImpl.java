package com.greenriver.commons.session;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import org.springframework.security.context.SecurityContextHolder;

/**
 * This class is used to store information about the logged user, so it is not
 * neccessary to retrieve it from the database.
 * @author luis
 */
public class UserSessionInfoImpl implements UserSessionInfo {

    private UserDao userDao;
    private User currentUser;

    /**
     * Retrieves the current User objecte for the session.
     * @return The session's User.
     */
    @Override
    public User getCurrentUser() {

        if (currentUser == null) {
            String username =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            currentUser = userDao.getByUsername(username);

            // If the user still is null, it wasn't in the database, so it MUST be
            // the anonimous user.
            if (currentUser == null) {
                currentUser = new User();
                currentUser.setUsername("anonymous");
            }
        }

        return currentUser;
    }

    /**
     * Wires a UserDao bean to this bean.
     * @param userDao the userDao to set
     */
    @Override
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
