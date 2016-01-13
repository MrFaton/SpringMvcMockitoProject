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
        model.setViewName(View.FROM_CREATE);
        return model;
    }

    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public ModelAndView create(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result) {
        ModelAndView model = new ModelAndView();
        model.addObject("userForm", userForm);

        if (result.hasErrors()) {
            userUtils.resetPasswords(userForm);
            model.setViewName(View.FROM_CREATE);
            return model;
        }
        if (userUtils.isLoginExists(userForm.getLogin())) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("login", "", "Login already exists");
            model.setViewName(View.FROM_CREATE);
            return model;
        }
        if (userUtils.isEmailExists(userForm)) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FROM_CREATE);
            return model;
        }
        Role role = roleDao.findByName(userForm.getRole());
        userDao.create(userUtils.getUserByForm(userForm, role));

        model.setViewName(View.PAGE_REDIRECT_MAIN);
        return model;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
    public ModelAndView showEdit(@RequestParam("person_id") String idStr) {
        ModelAndView model = new ModelAndView();
        User user = userDao.findById(Integer.valueOf(idStr));
        UserForm userForm = userUtils.getFormByUser(user);
        model.addObject("userForm", userForm);
        model.setViewName(View.FROM_EDIT);
        return model;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public ModelAndView showEditForm(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result) {
        System.out.println("in edit post");
        ModelAndView model = new ModelAndView();
        model.addObject("userForm", userForm);

        if (result.hasErrors()) {
            model.setViewName(View.FROM_EDIT);
            return model;
        }
        if (userUtils.isEmailExists(userForm)) {
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FROM_EDIT);
            return model;
        }
        Role role = roleDao.findByName(userForm.getRole());
        User user = userUtils.getUserByForm(userForm, role);
        User dbUser = userDao.findByLogin(user.getLogin());

        model.setViewName(View.PAGE_REDIRECT_MAIN);

        if (dbUser == null) {
            return model;
        }

        user.setId(dbUser.getId());
        userDao.update(user);
        return model;
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("person_id") String idStr) {
        User user = userDao.findById(Integer.valueOf(idStr));
        if (user != null) {
            userDao.remove(user);
        }
        return View.PAGE_REDIRECT_MAIN;
    }
}
