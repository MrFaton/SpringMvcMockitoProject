package com.nixsolutions.ponarin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    public String showCreateForm(ModelMap model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return View.FROM_CREATE_EDIT;
    }

    @RequestMapping(value = "/admin/create", method = RequestMethod.POST)
    public ModelAndView create(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result) {

        ModelAndView model = new ModelAndView();
        model.addObject("userForm", userForm);

        if (result.hasErrors()) {
            userUtils.resetPasswords(userForm);
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        if (userUtils.isLoginExists(userForm.getLogin())) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("login", "", "Login already exists");
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        if (userUtils.isEmailExists(userForm)) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        Role role = roleDao.findByName(userForm.getRole());

        userDao.create(userUtils.getUserByForm(userForm, role));
        model.setViewName("redirect:/");
        return model;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.GET)
    public String showEditForm(ModelMap model,
            @RequestParam("person") String personLogin) {

        model.addAttribute("edit", true);

        if (personLogin == null || personLogin.length() == 0) {
            model.addAttribute("error", "No passed person loggin");
            return View.FROM_CREATE_EDIT;
        }

        User user = userDao.findByLogin(personLogin);
        UserForm userForm = userUtils.getFormByUser(user);

        model.addAttribute("userForm", userForm);
        return View.FROM_CREATE_EDIT;
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    public ModelAndView edit(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result) {

        ModelAndView model = new ModelAndView();
        model.addObject("edit", true);
        model.addObject("userForm", userForm);

        if (result.hasErrors()) {
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        if (userUtils.isEmailExists(userForm)) {
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        Role role = roleDao.findByName(userForm.getRole());

        User user = userUtils.getUserByForm(userForm, role);
        User dbUser = userDao.findByLogin(user.getLogin());
        user.setId(dbUser.getId());

        userDao.update(user);

        model.setViewName("redirect:/");
        return model;
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("person") String personLogin,
            HttpServletRequest request) {

        ModelAndView model = new ModelAndView();

        if (personLogin == null || personLogin.length() == 0) {
            model.addObject("error", "No passed person loggin");
            model.setViewName(View.FROM_CREATE_EDIT);
            return model;
        }

        User user = userDao.findByLogin(personLogin);

        userDao.remove(user);

        model.setViewName("redirect:/");
        return model;
    }
}
