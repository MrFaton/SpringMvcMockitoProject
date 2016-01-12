package com.nixsolutions.ponarin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nixsolutions.ponarin.consatnt.View;
import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.entity.User;
import com.nixsolutions.ponarin.form.UserForm;
import com.nixsolutions.ponarin.utils.UserUtils;

@Controller
public class PersonManageController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = "/admin/create", method = RequestMethod.GET)
    public ModelAndView showCreateForm() {
        ModelAndView model = new ModelAndView();
        UserForm userForm = new UserForm();
        model.addObject("userForm", userForm);
        model.addObject("action", "create");
        model.setViewName(View.FROM_CREATE_EDIT);
        return model;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
    public String editGet() {
        return View.PAGE_REDIRECT_MAIN;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public ModelAndView showEditForm(
            @RequestParam("person") String personLogin) {
        ModelAndView model = new ModelAndView();
        model.addObject("action", "update");

        User user = userDao.findByLogin(personLogin);
        UserForm userForm = userUtils.getFormByUser(user);

        model.addObject("userForm", userForm);
        model.setViewName(View.FROM_CREATE_EDIT);
        return model;
    }

    @RequestMapping(value = "/admin/manage", method = RequestMethod.GET)
    public String manageGet() {
        return View.PAGE_REDIRECT_MAIN;
    }

    @RequestMapping(value = "/admin/manage", method = RequestMethod.POST)
    public ModelAndView manage(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result, @RequestParam("action") String action) {
        ModelAndView model = new ModelAndView();
        model.addObject("userForm", userForm);
        model.addObject("action", action);

        if (result.hasErrors()) {
            resetPasswords(userForm, action);
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        if (userUtils.isEmailExists(userForm)) {
            resetPasswords(userForm, action);
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        switch (action) {
        case "create": {
            if (userUtils.isLoginExists(userForm.getLogin())) {
                userUtils.resetPasswords(userForm);
                result.rejectValue("login", "", "Login already exists");
                model.setViewName(View.FROM_CREATE_EDIT);
                return model;
            }
            Role role = roleDao.findByName(userForm.getRole());
            userDao.create(userUtils.getUserByForm(userForm, role));
            break;
        }
        case "update": {
            Role role = roleDao.findByName(userForm.getRole());
            User user = userUtils.getUserByForm(userForm, role);
            User dbUser = userDao.findByLogin(user.getLogin());

            if (dbUser == null) {
                model.setViewName(View.PAGE_REDIRECT_MAIN);
                return model;
            }

            user.setId(dbUser.getId());
            userDao.update(user);
            model.clear();
        }
        default:
        }

        model.setViewName(View.PAGE_REDIRECT_MAIN);
        return model;
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String getDelete() {
        return View.PAGE_REDIRECT_MAIN;
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("person") String personLogin) {
        User user = userDao.findByLogin(personLogin);
        if (user != null) {
            userDao.remove(user);
        }
        return View.PAGE_REDIRECT_MAIN;
    }

    private void resetPasswords(UserForm userForm, String action) {
        if (action.equals("create")) {
            userUtils.resetPasswords(userForm);
        }
    }
}
