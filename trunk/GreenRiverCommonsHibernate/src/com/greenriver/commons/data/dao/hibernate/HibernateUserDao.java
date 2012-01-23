package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.dao.UserDao;
import com.greenriver.commons.data.dao.hibernate.base.HibernatePagedResultsDao;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.model.User;
import com.greenriver.commons.data.model.UserAuthority;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * User dao implementation for hibernate
 * @author luis
 */
public class HibernateUserDao 
    extends HibernatePagedResultsDao<User, QueryArgs> 
    implements UserDao<User> {

    @Override
    public void save(User entity) {
        this.save(entity, null);
    }
    
    

    @Override
    public void save(User user, String encodedPassword) {
        User existingUser = null;

        if (user.getId() != null) {
            // If the received objecte says it was persisted previously we
            // try to load it.
            existingUser = (User) getCurrentSession().get(
                    User.class,user.getId());
        }

        if (existingUser == null 
                || encodedPassword!=null) {
            // If the user is a new one or the password has changed,
            // we have to set the encoded password.

            if (Strings.isNullOrEmpty(encodedPassword)) {
                throw new IllegalArgumentException(
                        "Provided encoded password parameter is not valid.");
            }

            user.setPassword(encodedPassword);
        }

        if (existingUser == null) {
            existingUser = user;
        } else {

            getCurrentSession().merge(user);
        }

        getCurrentSession().saveOrUpdate(existingUser);

        // We need to manually flush here so the username unique constraint
        // gets checked when we expect to be. This manual flushing should be
        // done every time we are dealing with constrainted entities.
        getCurrentSession().flush();

        // We remove the older the authorities.
        removeAuthoritiesForUser(existingUser);

        // We add the new authorities.
        for (String role : existingUser.getRoles()) {
            UserAuthority authority = new UserAuthority();
            authority.setUser(existingUser);
            authority.setAuthority(role);
            getCurrentSession().save(authority);
        }
    }

    @Override
    public int getUserCount() {
        Criteria c = getCurrentSession().createCriteria(User.class);
        c.add(Restrictions.eq("deleted", false));
        c.setProjection(Projections.rowCount());

        Integer count = (Integer) c.uniqueResult();
        return count;
    }
    
    @Override
    public void remove(Long id) {
        // Firstly, we remove the authorities.
        User user = getById(id);
        removeAuthoritiesForUser(user);
        user.setDeleted(true);
        getCurrentSession().update(user);
    }

    private void removeAuthoritiesForUser(User user) {
        Query q = getCurrentSession().createQuery(
                "DELETE FROM " + UserAuthority.class.getSimpleName() + " WHERE user=:user");
        q.setParameter("user", user);

        q.executeUpdate();
    }

    @Override
    public User getByUsername(String username) {
        Criteria crit = getCurrentSession().createCriteria(User.class);
        crit.add(Restrictions.eq("username", username));
        crit.add(Restrictions.eq("deleted", false));
        User user = (User) crit.uniqueResult();
        return user;
    }


    @Override
    public User getById(Long userId) {
        User user = (User) getCurrentSession().get(User.class, userId);
        return user;
    }

    @Override
    public int query(QueryArgs qArgs, List<User> entities) {
       return query(qArgs, entities, Restrictions.eq("deleted", false));
    }

    @Override
    public List<User> getAll(String orderField) {
        return super.getAll(Order.asc(orderField),Restrictions.eq("deleted", false));
    }
}
