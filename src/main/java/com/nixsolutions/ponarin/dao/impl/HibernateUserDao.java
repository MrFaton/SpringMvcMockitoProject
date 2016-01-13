package com.nixsolutions.ponarin.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.User;

@Repository
public class HibernateUserDao implements UserDao {
    private static final Logger logger = LoggerFactory
            .getLogger(HibernateUserDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(User user) {
        logger.trace("create " + user);
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        logger.trace("update " + user);
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    @Transactional
    public void remove(User user) {
        logger.trace("remove " + user);
        sessionFactory.getCurrentSession().delete(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<User> findAll() {
        logger.trace("find all users");

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        return criteria.list();
    }

    @Override
    @Transactional
    public User findById(long id) {
        logger.trace("find user by id = " + id);
        return (User) sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Override
    @Transactional
    public User findByLogin(String login) {
        logger.trace("find user by login = " + login);

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("login", login));
        return (User) criteria.uniqueResult();
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        logger.trace("find user by email = " + email);

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        return (User) criteria.uniqueResult();
    }
}
