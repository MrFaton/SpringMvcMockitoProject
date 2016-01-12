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
import org.springframework.web.servlet.ModelAndView;

import com.nixsolutions.ponarin.consatnt.View;
import com.nixsolutions.ponarin.dao.RoleDao;
import com.nixsolutions.ponarin.dao.UserDao;
import com.nixsolutions.ponarin.entity.Role;
import com.nixsolutions.ponarin.form.UserForm;
import com.nixsolutions.ponarin.utils.UserUtils;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

@Controller
public class RegistrationController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(ModelMap model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return View.FORM_REG;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(
            @ModelAttribute("userForm") @Valid UserForm userForm,
            BindingResult result, HttpServletRequest request) {

        ModelAndView model = new ModelAndView();
        model.addObject("userForm", userForm);

        if (result.hasErrors()) {
            userUtils.resetPasswords(userForm);
            model.setViewName(View.FORM_REG);
            return model;
        }

        if (userUtils.isLoginExists(userForm.getLogin())) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("login", "", "Login already exists");
            model.setViewName(View.FORM_REG);
            return model;
        }

        if (userUtils.isEmailExists(userForm)) {
            userUtils.resetPasswords(userForm);
            result.rejectValue("email", "", "Email already exists");
            model.setViewName(View.FORM_REG);
            return model;
        }

        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LeD4BQTAAAAAPRpVTFZbmv17K_YqjVtRig6cwme");

        String remoteAddr = request.getRemoteAddr();
        String challengeField = request
                .getParameter("recaptcha_challenge_field");
        String responseField = request.getParameter("recaptcha_response_field");

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
                challengeField, responseField);

        if (!reCaptchaResponse.isValid()) {
            userUtils.resetPasswords(userForm);
            model.addObject("invalidCaptcha", "Captcha is invalid");
            model.setViewName(View.FORM_REG);
            return model;
        }

        Role role = roleDao.findByName("User");

        model.addObject("userLogin", userForm.getLogin());

        userDao.create(userUtils.getUserByForm(userForm, role));
        model.setViewName(View.FROM_REG_SUCCESS);

        return model;
    }
}
