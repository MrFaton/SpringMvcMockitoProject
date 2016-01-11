package com.nixsolutions.ponarin.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nixsolutions.ponarin.dao.UserDao;

@Service
public class PersonDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String login)
            throws UsernameNotFoundException {
        com.nixsolutions.ponarin.entity.User nativeUser = userDao
                .findByLogin(login);

        if (nativeUser == null) {
            throw new UsernameNotFoundException(
                    "User with login " + login + "is not exists");
        }

        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>(0);

        String role = nativeUser.getRole().getName();
        String springSequrityRole = "ROLE_" + role.toUpperCase();

        grantedAuthorities.add(new SimpleGrantedAuthority(springSequrityRole));

        return new User(nativeUser.getLogin(), nativeUser.getPassword(), true,
                true, true, true, grantedAuthorities);
    }

}