package com.nixsolutions.ponarin.tag;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

public class CaptchaTag extends SimpleTagSupport {
    private String publickey;
    private String privatekey;
    private String themeName;

    @Override
    public void doTag() throws JspException, IOException {
        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(publickey, privatekey,
                false);
        Properties properties = new Properties();

        if (themeName != null || themeName.length() != 0) {
            properties.put("theme", themeName);
        }

        String captchaHtml = captcha.createRecaptchaHtml(null, properties);
        getJspContext().getOut().write(captchaHtml);
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }
}
