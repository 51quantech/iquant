package com.iquant.common;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class WebHelper{

    // 登陆后存入session的key
    public static final String LOGIN_PIN = "_http_login_pin_";

    public static void setLoginPin(String value) {
        HttpServletRequest request = getRequest();
        request.getSession().setAttribute(LOGIN_PIN, value);
    }

    public static void removeLoginPin() {
        HttpServletRequest request = getRequest();
        request.getSession().removeAttribute(LOGIN_PIN);
    }

    public static String getLoginPin() {
        HttpServletRequest request = getRequest();
        Object obj = request.getSession().getAttribute(LOGIN_PIN);
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    public static String getLoginViewPin() {
        HttpServletRequest request = getRequest();
        Object obj = request.getSession().getAttribute(LOGIN_PIN);
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    private static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
