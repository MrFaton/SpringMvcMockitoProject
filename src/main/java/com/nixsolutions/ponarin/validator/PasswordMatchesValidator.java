package com.nixsolutions.ponarin.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nixsolutions.ponarin.annotation.PasswordMatches;
import com.nixsolutions.ponarin.form.UserForm;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, UserForm> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // NOP
    }

    @Override
    public boolean isValid(UserForm form, ConstraintValidatorContext context) {
        return form.getPassword().equals(form.getMatchingPassword());
    }
}
