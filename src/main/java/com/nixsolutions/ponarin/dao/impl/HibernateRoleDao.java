package com.nixsolutions.ponarin.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.entity.Role;

@Repository
public class HibernateRoleDao implements RoleDao {
    private static final Logger logger = LoggerFactory
            .getLogger(HibernateRoleDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(Role role) {
        logger.trace("create " + role);
        sessionFactory.getCurrentSession().save(role);
    }

    @Override
    @Transactional
    public void update(Role role) {
        logger.trace("update " + role);
        sessionFactory.getCurrentSession().update(role);
    }

    @Override
    @Transactional
    public void remove(Role role) {
        logger.trace("remove " + role);
        sessionFactory.getCurrentSession().delete(role);
    }

    @Override
    @Transactional
    public Role findByName(String name) {
        logger.trace("searching for role by name = " + name);

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Role.class);
        criteria.add(Restrictions.eq("name", name));
        return (Role) criteria.uniqueResult();
    }
}
