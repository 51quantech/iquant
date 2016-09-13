package com.iquant.controller;

import com.iquant.service.UserService;
import com.iquant.common.WebHelper;
import com.iquant.domain.beans.User;
import com.iquant.domain.beans.UserStrategy;
import com.iquant.domain.query.UserStrategyQuery;
import com.iquant.service.LabService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yonggangli on 2016/8/24.
 */
@Controller
@RequestMapping(value = "/lab")
public class LabController {

    private final static Logger LOG = Logger.getLogger(LabController.class);

    @Autowired
    private LabService labService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/lab_password")
    public String password() {
        return "lab/password";
    }

    @RequestMapping(value = "/lab_doPassword")
    public String doPassword(HttpServletRequest request, ModelMap modelMap) {

        String oldPassword = request.getParameter("oldPassword");
        String emPassword = request.getParameter("emPassword");
        String remPassword = request.getParameter("remPassword");

        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(emPassword)) {
            modelMap.addAttribute("message", 1);
            return "redirect:/lab/lab_index";
        }

        if (!emPassword.equals(remPassword)) {
            modelMap.addAttribute("message", 2);
            return "redirect:/lab/lab_index";
        }

        User user = userService.queryUserByUname(WebHelper.getLoginPin());
        if (!oldPassword.equals(user.getUpwd())) {
            modelMap.addAttribute("message", 3);
            return "redirect:/lab/lab_index";
        }

        user.setUpwd(emPassword);
        int code = userService.updateUserByUname(user);
        if (code < 1) {
            modelMap.addAttribute("message", 4);
            return "redirect:/lab/lab_index";
        }

        return "redirect:/lab/lab_index";
    }

    @RequestMapping(value = "/lab_index")
    public String index(ModelMap modelMap, HttpServletRequest request) {
        UserStrategyQuery userStrategyQuery = new UserStrategyQuery();
        userStrategyQuery.setUname(WebHelper.getLoginPin());
        userStrategyQuery.setStartRow(0);
        userStrategyQuery.setEndRow(5);
        List<UserStrategy> userStrategyNameList = labService
                .findUserStrategyNameList(userStrategyQuery);
        modelMap.addAttribute("userStrategyNameList", userStrategyNameList);
        return "lab/index";
    }

    @RequestMapping(value = "/index_initial")
    public String index_initial() {
        return "lab/index_initial";
    }

    @RequestMapping(value = "/coding_status")
    public String coding_status() {
        return "lab/coding_status";
    }

    @RequestMapping(value = "/coding_index")
    public String coding_index() {
        return "lab/coding_index";
    }

    @RequestMapping(value = "/lab_coding")
    public String coding(HttpServletRequest request, ModelMap modelMap) {
        UserStrategy userStrategy = null;
        String idstr = request.getParameter("id");
        // String code = request.getParameter("code");
        if (StringUtils.isNotBlank(idstr)) {
            int id = Integer.parseInt(idstr);
            // UserStrategyQuery usq = new UserStrategyQuery();
            // usq.setUname(WebHelper.getPin());
            // usq.setSid(id);
            // int num = labService.queryUserStrategyTotalRows(usq);
            // if(num > ){
            userStrategy = labService.findUserStrategyById(id,
                    WebHelper.getLoginPin());
            // }
            if (userStrategy == null) {
                return "redirect:/lab/lab_index.html";
            }
        } else {
            return "redirect:/lab/lab_index.html";
        }
        modelMap.addAttribute("userStrategy", userStrategy);
        modelMap.addAttribute("id", idstr);
        return "lab/coding";
    }

}
