package com.dao;

import com.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Roman on 21.10.2014.
 */
//@Repository
public class UserDao implements UserDaoInterface {

//    private static final LoggerWrapper LOG = LoggerWrapper.get(UserDaoInterface.class);

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//    @Autowired
//    public void setSessionFactory(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }

    private Session getSession() {
//        try{
//            sessionFactory = new Configuration().configure().buildSessionFactory();
//        }catch (Throwable ex) {
//            System.err.println("Failed to create sessionFactory object." + ex);
////            throw new ExceptionInInitializerError(ex);
//        }
        return sessionFactory.getCurrentSession();
    }


//    public void addUser(User user) {
//        getSession().save(user);
//    }

    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveOrUpdateUser(User user) {
        try {
            getSession().saveOrUpdate(user);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteUser(User user) {
        try {
            if (user != null)
                getSession().delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = getSession().beginTransaction();
            session.createQuery("FROM User").list();
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
//        LOG.info("HELLO");
        return (User) getSession().get(User.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
//    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try{
            tx = getSession().beginTransaction();
            list = session.createQuery("FROM User").list();
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
        return list;
    }
}
