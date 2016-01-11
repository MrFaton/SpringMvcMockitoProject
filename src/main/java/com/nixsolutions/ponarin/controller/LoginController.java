package com.nixsolutions.ponarin.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nixsolutions.ponarin.consatnt.View;
import com.nixsolutions.ponarin.dao.UserDao;

@Controller
public class LoginController {
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();

        String personLogin = auth.getName();

        model.addAttribute("personLogin", personLogin);

        Collection<? extends GrantedAuthority> authorities = auth
                .getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            model.addAttribute("userList", userDao.findAll());
            return View.PAGE_ADMIN;
        } else
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            return View.PAGE_USER;
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            ModelMap model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }

        return "login";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accesssDenied() {
        return "403";
    }
}
