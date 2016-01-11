package com.nixsolutions.ponarin.form;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.nixsolutions.ponarin.annotation.PasswordMatches;

@PasswordMatches
public class UserForm {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "login should consist "
            + "of 3 to 20 characters, allows letters and digits")
    private String login;

    @NotBlank
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$%]).{6,20})", message = ""
            + "Password should consist of 6 to 20 characters and should contain "
            + "1 lowercase, 1 uppercase, 1 special symbol (!?@#$%)")
    private String password;

    @NotBlank
    private String matchingPassword;

    @NotBlank
    @Pattern(regexp = "[\\w-]+@([\\w-]+\\.)+[\\w-]+", message = "Incorrect email")
    @Length(min = 4, max = 30, message = "Email should consist "
            + "of 4 to 30 characters")
    private String email;

    @NotBlank
    @Length(min = 1, max = 30, message = "First name should consist of "
            + "1 to 30 characters")
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 30, message = "Last name should consist of "
            + "1 to 30 characters")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)", message = ""
            + "Incorrect birthday format. You shouls use pattern: dd-MM-yyyy")
    private String birthDay;

    private String role;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserForm [login=" + login + ", password=" + password
                + ", matchingPassword=" + matchingPassword + ", email=" + email
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", birthDay=" + birthDay + ", role=" + role + "]";
    }
}
