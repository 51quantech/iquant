package com.iquant.controller;

import com.iquant.common.WebHelper;
import com.iquant.service.LoginService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by yonggangli on 2016/8/24.
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final static Logger LOG = Logger.getLogger(IndexController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 首页
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * 登陆
     *
     * @param request
     * @param response
     * @param modelMap
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "doLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public String doLogin(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {

        String emName = request.getParameter("emName");
        String emPassword = request.getParameter("emPassword");
        String returnUrl = request.getParameter("returnUrl");

        int message = 0;
        if (validateLoginParams(emName, emPassword) == false) {
            message = 1;
            modelMap.addAttribute("login_error", "用户名或密码不能为空，请重新登陆");
            modelMap.addAttribute("message", message);
            return "loginerror";
        }
        // int count = 1;// todo
        int count = loginService.queryUserTotalRows(emName, emPassword);
        LOG.info("username is " + emName + " pwd is " + emPassword
                + " returnUrl is " + returnUrl);

        boolean resultOk = count > 0 ? true : false;
        // boolean resultOk=true;//
        if (resultOk == false) {
            message = 1;
            modelMap.addAttribute("login_error", "用户名或密码错误，请重新登陆");
            modelMap.addAttribute("message", message);
            return "loginerror";
        }
        WebHelper.setLoginPin(emName);
        response.sendRedirect(URLDecoder.decode(returnUrl, "UTF-8"));
        return null;
    }

    /**
     * 注册账号
     *
     * @param request
     * @param response
     * @param modelMap
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "doRegister", method = {RequestMethod.GET, RequestMethod.POST})
    public String doRegister(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws IOException {

        String emName = request.getParameter("emName");
        String emPassword = request.getParameter("emPassword");
        String remPassword = request.getParameter("remPassword");

        int model = 2;
        modelMap.addAttribute("model", model);

        if (validateRegisterParams(emName, emPassword, remPassword) == false) {
            modelMap.addAttribute("register_error", "用户名或密码不能为空，请重新登陆");
            return "login";
        }
        try {
            loginService.insertUser(emName, emPassword);
            return "login";
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "index";
    }

    /**
     * 退出登录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebHelper.removeLoginPin();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        return "index";
    }

    private boolean validateLoginParams(String emName, String emPassword) {

        if (StringUtils.isBlank(emName) || StringUtils.isBlank(emPassword)) {
            return false;
        }
        emName = StringEscapeUtils.escapeHtml(emName).trim();
        emPassword = StringEscapeUtils.escapeHtml(emPassword).trim();
        return true;
    }

    private boolean validateRegisterParams(String emName, String emPassword, String remPassword) {
        if (StringUtils.isBlank(emName) || StringUtils.isBlank(emPassword)
                || StringUtils.isBlank(remPassword)
                || !emPassword.equals(remPassword)) {
            return false;
        }
        emName = StringEscapeUtils.escapeHtml(emName).trim();
        emPassword = StringEscapeUtils.escapeHtml(emPassword).trim();
        remPassword = StringEscapeUtils.escapeHtml(remPassword).trim();
        return true;
    }

}
