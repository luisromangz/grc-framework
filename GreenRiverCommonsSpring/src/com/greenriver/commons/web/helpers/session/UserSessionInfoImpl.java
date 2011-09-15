package com.greenriver.commons.web.helpers.session;

import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class is used to store information about the logged user, so it is not
 * neccessary to retrieve it from the database.
 * @author luis
 */
public class UserSessionInfoImpl implements UserSessionInfo {

    private UserDao userDao;

    /**
     * Retrieves the current User objecte for the session.
     * @return The session's User.
     */
    @Override
    public User getCurrentUser() {


        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.getByUsername(username);

        // If the user still is null, it wasn't in the database, so it MUST be
        // the anonimous user.
        if (user == null) {
            user = new User();
            user.setUsername("anonymous");
        }
        return user;


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
