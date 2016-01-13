package com.nixsolutions.ponarin.dao;

import java.util.List;

import com.nixsolutions.ponarin.entity.User;

public interface UserDao {
    void create(User user);

    void update(User user);

    void remove(User user);

    List<User> findAll();

    User findById(long id);

    User findByLogin(String login);

    User findByEmail(String email);
}
