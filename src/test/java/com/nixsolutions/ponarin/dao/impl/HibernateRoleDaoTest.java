package com.nixsolutions.ponarin.dao.impl;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.utils.DbTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/app-context-test.xml"))
public class HibernateRoleDaoTest {
    private static final String DATASET_COMMON = "dataset/role/common.xml";
    private static final String TABLE_EMPTY = "dataset/role/empty.xml";
    private static final String TABLE_NAME = "ROLE";
    private static final String[] IGNORE_COLS = { "ROLE_ID" };

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DbTestHelper dbTestHelper;

    private Role[] roles;

    @Before
    public void setUp() throws Exception {
        dbTestHelper.fill(DATASET_COMMON);

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

    @Test
    public void testCreate() throws Exception {
        dbTestHelper.fill(TABLE_EMPTY);
        String afterCreate = "dataset/role/afterCreate.xml";

        roleDao.create(roles[1]);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterCreate);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() {
        roleDao.update(null);
    }

    @Test
    public void testUpdate() throws Exception {
        String afterUpdate = "dataset/role/afterUpdate.xml";
        Role role = roles[2];
        role.setName("role55");

        roleDao.update(role);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterUpdate);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNull() {
        roleDao.remove(null);
    }

    @Test
    public void testRemove() throws Exception {
        String afterRemove = "dataset/role/afterRemove.xml";

        roleDao.remove(roles[1]);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterRemove);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
    }

    @Test
    public void testFindByName() {
        Role role = roleDao.findByName(roles[2].getName());
        Assert.assertEquals("Roles must equals", roles[2], role);
    }
}
