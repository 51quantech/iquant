package com.iquant.controller;

import com.iquant.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonggangli on 2016/8/24.
 */
@Controller
@RequestMapping(value = "/help")
public class HelpController {

    private final static Logger LOG = Logger.getLogger(HelpController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/data")
    public String data() {
        return "help/data";
    }

    @RequestMapping(value = "/guide")
    public String guide() {
        return "help/guide";
    }

    @RequestMapping(value = "/api")
    public String api() {
        return "help/api";
    }

    @RequestMapping(value = "/top")
    public String top() {
        return "help/top";
    }

    @RequestMapping(value = "/test")
    @ResponseBody
    public Map<String,Object> test() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("a","汉字");
        return map;
    }

    @RequestMapping(value = "/test1")
    @ResponseBody
    public String test1() {
        JSONObject json = new JSONObject();
        json.put("a","汉字");
        JSONArray josnArry = new JSONArray();
        for (Object o: josnArry) {

        }
        return json.toString();
    }

}
