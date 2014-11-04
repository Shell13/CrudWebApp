package com.dao;

import com.logger.LoggerWrapper;
import com.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void saveOrUpdateUser(User user) {
        LOG.info("saveOrUpdateUser");
        getSession().saveOrUpdate(user);
    }

    @Override
    public void deleteUser(User user) {
        LOG.info("deleteUser");
        getSession().delete(user);
    }

    @Override
    public User getUser(int id) {
        LOG.info("getUser");
        return (User) getSession().get(User.class, id);
    }

    @Override
    public User getLast() {
        LOG.info("getLast");
        SQLQuery sql = getSession().createSQLQuery("SELECT * FROM user");
        List list = sql.addEntity(User.class).list();
        return (User) list.get(list.size() - 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        LOG.info("getAllUsers");
        return getSession().createQuery("FROM User").list();
    }
}
