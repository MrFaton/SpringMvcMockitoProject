package com.nixsolutions.ponarin.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.entity.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("/dataset/role/common.xml")
@ContextConfiguration(locations = ("classpath:/app-context-test.xml"))
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class HibernateRoleDaoTest {
    @Autowired
    private RoleDao roleDao;

    private Role[] roles;

    @Before
    public void setUp() throws Exception {
        // Configure Roles
        roles = new Role[3];

        Role role1 = new Role();
        role1.setId(1);
        role1.setName("role1");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("role2");

        Role role3 = new Role();
        role3.setId(3);
        role3.setName("role3");

        roles[0] = role1;
        roles[1] = role2;
        roles[2] = role3;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() {
        roleDao.create(null);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateExists() throws Exception {
        roleDao.create(roles[0]);
    }

    @Test
    @DatabaseSetup("/dataset/role/empty.xml")
    @ExpectedDatabase(table = "Role", value = "/dataset/role/afterCreate.xml", 
    assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testCreate() throws Exception {
        roleDao.create(roles[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() {
        roleDao.update(null);
    }

    @Test
    @ExpectedDatabase(table = "Role", value = "/dataset/role/afterUpdate.xml", 
    assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testUpdate() throws Exception {
        Role role = roles[2];
        role.setName("role55");

        roleDao.update(role);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNull() {
        roleDao.remove(null);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveNotExists() throws Exception {
        Role role = new Role();
        role.setId(4);
        role.setName("role5");
        roleDao.remove(role);
    }

    @Test
    @ExpectedDatabase(table = "Role", value = "/dataset/role/afterRemove.xml", 
    assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testRemove() throws Exception {
        roleDao.remove(roles[1]);
    }

    @Test
    public void testFindByName() {
        Role role = roleDao.findByName(roles[2].getName());
        Assert.assertEquals("Roles must equals", roles[2], role);
    }
}
