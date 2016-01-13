package com.nixsolutions.ponarin.tag;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nixsolutions.ponarin.entity.User;

public class UsersTableTag extends SimpleTagSupport {
    private List<User> userList;

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        String editControllerPath = pageContext.getServletContext()
                .getContextPath() + "/admin/edit";
        String deleteControllerPath = pageContext.getServletContext()
                .getContextPath() + "/admin/delete";
        JspWriter out = pageContext.getOut();
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("<table>");
        strBuilder.append("<tr>");

        strBuilder.append("<th>Login</th>");
        strBuilder.append("<th>First Name</th>");
        strBuilder.append("<th>Last Name</th>");
        strBuilder.append("<th>Age</th>");
        strBuilder.append("<th>Role</th>");
        strBuilder.append("<th>Actions</th>");

        strBuilder.append("</tr>");

        for (User user : userList) {
            strBuilder.append("<tr>");
            strBuilder.append("<td>" + user.getLogin() + "</td>");
            strBuilder.append("<td>" + user.getFirstName() + "</td>");
            strBuilder.append("<td>" + user.getLastName() + "</td>");
            strBuilder.append("<td>" + getAge(user.getBirthDay()) + "</td>");
            strBuilder.append("<td>" + user.getRole().getName() + "</td>");

            strBuilder.append("<td>");
            strBuilder.append("<a href=\"" + editControllerPath);
            strBuilder.append("?person_id=" + user.getId() + "\">Edit</a>");

            strBuilder.append("  ");

            strBuilder.append("<a href=\"" + deleteControllerPath);
            strBuilder.append("?person_id=" + user.getId() + "\">Delete</a>");
            strBuilder.append("</td>");

            strBuilder.append("</tr>");
        }

        strBuilder.append("</table>");
        out.println(strBuilder.toString());

    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    private int getAge(Date birthDay) {
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(birthDay);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dateOfBirth.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dateOfBirth.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dateOfBirth
                        .get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }
}
