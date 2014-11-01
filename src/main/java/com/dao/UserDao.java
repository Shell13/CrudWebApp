package com.dao;

import com.logger.LoggerWrapper;
import com.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 21.10.2014.
 */

public class UserDao implements UserDaoInterface {

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserDao.class);

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public boolean saveOrUpdateUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LOG.info("saveOrUpdateUser");
            session.saveOrUpdate(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LOG.info("deleteUser");
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    @Override
    public User getUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        User user = null;
        try {
            tx = session.beginTransaction();
            LOG.info("getUser");
            user = (User) session.get(User.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return user;
    }
    @Override
    public User getLast(){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        List list = new ArrayList<>();
        User user = null;
        try {
            tx = session.beginTransaction();
            LOG.info("getLast");
            SQLQuery sql = session.createSQLQuery("SELECT * FROM user");
            sql.addEntity(User.class);
            list = sql.list();
            tx.commit();
            user = (User) list.get(list.size()-1);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LOG.info("getAllUsers");
            list = session.createQuery("FROM User").list();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return list;
    }
}
