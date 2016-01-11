package com.nixsolutions.ponarin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.entity.User;
import com.nixsolutions.ponarin.form.UserForm;

@Component
public class UserUtils {
    @Autowired
    private UserDao userDao;

    public User getUserByForm(UserForm userForm, Role role) {
        User user = new User();

        user.setLogin(userForm.getLogin());
        user.setPassword(userForm.getPassword());
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());

        String birthDayStr = userForm.getBirthDay();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            user.setBirthDay(dateFormat.parse(birthDayStr));
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Birthday date is incorrect. You shoud use pattern like: dd-MM-yyyy");
        }
        user.setRole(role);

        return user;
    }

    public UserForm getFormByUser(User user) {
        UserForm userForm = new UserForm();

        userForm.setLogin(user.getLogin());
        userForm.setPassword(user.getPassword());
        userForm.setMatchingPassword(user.getPassword());
        userForm.setEmail(user.getEmail());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        userForm.setBirthDay(dateFormat.format(user.getBirthDay()));

        userForm.setRole(user.getRole().getName());

        return userForm;
    }

    public boolean isLoginExists(String login) {
        if (userDao.findByLogin(login) == null) {
            return false;
        }
        return true;
    }

    public boolean isEmailExists(UserForm form) {
        User loadedUser = userDao.findByEmail(form.getEmail());
        if (loadedUser == null) {
            return false;
        }
        if (form.getLogin().equalsIgnoreCase(loadedUser.getLogin())) {
            return false;
        }
        return true;
    }
    
    public void resetPasswords(UserForm userForm) {
        userForm.setPassword("");
        userForm.setMatchingPassword("");
    }
}
