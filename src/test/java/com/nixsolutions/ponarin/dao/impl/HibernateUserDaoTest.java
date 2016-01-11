package com.nixsolutions.ponarin.dao.impl;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.entity.User;
import com.nixsolutions.ponarin.utils.DbTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/app-context-test.xml"))
public class HibernateUserDaoTest {
    private static final String DATASET_COMMON = "dataset/user/common.xml";
    private static final String TABLE_EMPTY = "dataset/user/empty.xml";
    private static final String TABLE_NAME = "USER";
    private static final String[] IGNORE_COLS = { "USER_ID" };

    private User[] users;
    private Role[] roles;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DbTestHelper dbTestHelper;

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

        // Configure Users
        users = new User[3];

        User user1 = new User();
        user1.setId(1L);
        user1.setLogin("login1");
        user1.setPassword("pass1");
        user1.setEmail("email1");
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");
        GregorianCalendar calendar = new GregorianCalendar(1990, 10, 15);
        user1.setBirthDay(new Date(calendar.getTimeInMillis()));
        user1.setRole(role1);

        User user2 = new User();
        user2.setId(2L);
        user2.setLogin("login2");
        user2.setPassword("pass2");
        user2.setEmail("email2");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        GregorianCalendar calendar2 = new GregorianCalendar(1995, 8, 5);
        user2.setBirthDay(new Date(calendar2.getTimeInMillis()));
        user2.setRole(role2);

        User user3 = new User();
        user3.setId(3L);
        user3.setLogin("login3");
        user3.setPassword("pass3");
        user3.setEmail("email3");
        user3.setFirstName("firstName3");
        user3.setLastName("lastName3");
        GregorianCalendar calendar3 = new GregorianCalendar(1989, 11, 18);
        user3.setBirthDay(new Date(calendar3.getTimeInMillis()));
        user3.setRole(role3);

        users[0] = user1;
        users[1] = user2;
        users[2] = user3;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNull() {
        userDao.create(null);
    }

    @Test
    public void testCreate() throws Exception {
        dbTestHelper.fill(TABLE_EMPTY);

        String afterCreate = "dataset/user/afterCreate.xml";

        userDao.create(users[1]);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterCreate);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNull() {
        userDao.update(null);
    }

    @Test
    public void testUpdate() throws Exception {
        String afterUpdate = "dataset/user/afterUpdate.xml";

        User user = new User();

        user.setId(users[0].getId());
        user.setLogin("login55");
        user.setPassword("pass55");
        user.setEmail("email55");
        user.setFirstName("firstName55");
        user.setLastName("lastName55");

        GregorianCalendar calendar = new GregorianCalendar(1950, 2, 7);
        user.setBirthDay(new Date(calendar.getTimeInMillis()));

        Role role = new Role();
        role.setId(3);
        role.setName("role3");
        user.setRole(role);

        userDao.update(user);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterUpdate);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNull() {
        userDao.remove(null);
    }

    @Test
    public void testRemove() throws Exception {
        String afterRemove = "dataset/user/afterRemove.xml";

        userDao.remove(users[2]);

        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
                afterRemove);
        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
    }

    @Test
    public void testFindAll() {
        List<User> expected = new ArrayList<>();
        expected.add(users[0]);
        expected.add(users[1]);
        expected.add(users[2]);

        List<User> actual = userDao.findAll();

        System.out.println(actual);

        Assert.assertEquals("User lists must equals", expected, actual);
    }

    @Test
    public void testFindByLogin() {
        User user = userDao.findByLogin(users[1].getLogin());
        Assert.assertEquals("Users must equals", users[1], user);
    }

    @Test
    public void testFindByEmail() {
        User user = userDao.findByEmail(users[0].getEmail());
        Assert.assertEquals("Users must equals", users[0], user);
    }
}
