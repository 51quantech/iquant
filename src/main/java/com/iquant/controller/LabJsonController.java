package com.iquant.controller;

import com.iquant.common.DateUtil;
import com.iquant.common.WebHelper;
import com.iquant.domain.beans.UserStrategy;
import com.iquant.domain.query.UserStrategyQuery;
import com.iquant.service.LabService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yonggangli on 2016/9/1.
 */
@Controller
@RequestMapping(value = "/json")
public class LabJsonController {

    private final static Logger LOG = Logger.getLogger(LabJsonController.class);

    @Autowired
    private LabService labService;

    @RequestMapping(value = "/lab_coding_create.html")
    public void coding_create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        String code = request.getParameter("code");
        UserStrategyQuery userStrategyQuery = new UserStrategyQuery();
        userStrategyQuery.setUname(WebHelper.getLoginPin());
        userStrategyQuery.setStartRow(0);
        userStrategyQuery.setEndRow(Integer.MAX_VALUE);
        int len = labService.queryUserStrategyTotalRows(userStrategyQuery);
        UserStrategy userStrategy = new UserStrategy();
        userStrategy.setUname(WebHelper.getLoginPin());
        if (StringUtils.isNotBlank(code)) {
            userStrategy.setStrategyText(code);
        }
        userStrategy.setStrategyName("Untitled" + len);
        InputStream in = LabController.class.getClassLoader().getResourceAsStream(
                "strategy/strategy.txt");

        BufferedReader br = null;
        StringBuilder file = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                file.append(readLine + "\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String strategy = file.toString();
        strategy = strategy.replace("{start}",
                DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        try {
            strategy = strategy.replace("{end}", DateFormatUtils.format(
                    DateUtil.getBeforeDate(30), "yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userStrategy.setStrategyText(strategy);
        labService.insertUserStrategy(userStrategy);
        // idstr = String.valueOf(userStrategy.getId());
        json.put("id", userStrategy.getId());

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_getUserStrategyNameList")
    public void getUserStrategyNameList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*Map<String, Object> dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        UserStrategyQuery userStrategyQuery = new UserStrategyQuery();
        userStrategyQuery.setUname(WebHelper.getLoginPin());
        userStrategyQuery.setStartRow(0);
        userStrategyQuery.setEndRow(Integer.MAX_VALUE);
        List<UserStrategy> userStrategyNameList = labService
                .findUserStrategyNameList(userStrategyQuery);

        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        for (UserStrategy bean : userStrategyNameList) {
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", bean.getId());
            temp.put("title", bean.getStrategyName());
            data.add(temp);
        }
        json.put("data", data);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_getUserStrategyById")
    public void getUserStrategyById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        int id = Integer.parseInt(request.getParameter("id"));
        UserStrategy userStrategy = labService.findUserStrategyById(id);

        json.put("data", userStrategy);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_insertUserStrategy")
    public void insertUserStrategy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        UserStrategy userStrategy = new UserStrategy();
        userStrategy.setUname(WebHelper.getLoginPin());
        userStrategy.setStrategyName(request.getParameter("strategyName"));
        userStrategy.setStrategyText(request.getParameter("strategyText"));

        int code = labService.insertUserStrategy(userStrategy);

        json.put("code", userStrategy.getId());
        json.put("status", code > 0 ? "success" : "failure");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_updateUserStrategy")
    public void updateUserStrategy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        int id = Integer.parseInt(request.getParameter("id"));
        UserStrategy userStrategy = labService.findUserStrategyById(id);
        if (request.getParameter("strategyText") != null)
            userStrategy.setStrategyText(request.getParameter("strategyText"));
        if (request.getParameter("strategyName") != null)
            userStrategy.setStrategyName(request.getParameter("strategyName"));

        int code = labService.updateUserStrategyById(userStrategy);

        json.put("status", code > 0 ? "success" : "failure");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_deleteUserStrategyById")
    public void deleteUserStrategyById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*dataMap = new HashMap<String, Object>();*/
        JSONObject json = new JSONObject();
        int id = Integer.parseInt(request.getParameter("id"));
        int code = labService.deleteUserStrategyById(id);

        json.put("status", code > 0 ? "success" : "failure");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_attemptData")
    public void attemptData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        String idstr = request.getParameter("id");

        json.put("status", "0");
        try {
            int id = Integer.parseInt(idstr);
            UserStrategy userStrategy = labService.findUserStrategyById(id);
            if (userStrategy != null) {
                String result = userStrategy.getStrategyResult();
                String code = userStrategy.getStrategyText();
                String detailResult = userStrategy.getStrategyDetailResult();
                if (StringUtils.isNotBlank(result)
                        && StringUtils.isNotBlank(detailResult)) {
                    json.put("status", userStrategy.getStrategyStatus());
                    json.put("code", code);
                    json.put("strategy", result);
                    json.put("strategyDetail", detailResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    @RequestMapping(value = "/lab_coding_stats")
    public void coding_stats(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject json = new JSONObject();
        /*String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        UserStrategy userStrategy = labService.findUserStrategyById(id,
                WebHelper.getLoginPin(request));
        if (userStrategy == null) {
            return;
        }
        String imageId = userStrategy.getImageId();
        if (StringUtils.isBlank(imageId)) {
            return;
        }*/
        /*String cmds = "docker stats --no-stream " + imageId
                + " | awk '!/CONTAINER/ {print $2,$3$4$5$6$7,$8}'";*/
        /*String output = "0.00% 35.76MB\\/1.074GB3.33%5.587 GB\\/80.85";*/
        String output = "0.00% 0MB/0GB0%0 GB/0";

        json.put("stats", output);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

    // 编译策略，保存最新策略，查找对应的docker容器和启动容器
    @RequestMapping(value = "/lab_compilePython")
    public void compilePython(HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject json = new JSONObject();

        String idstr = request.getParameter("id");
        int id = Integer.parseInt(idstr);

        String codeStr = request.getParameter("codeStr");

        if (StringUtils.isNotBlank(codeStr)) {
            UserStrategy strategyText = new UserStrategy();
            strategyText.setId(id);
            strategyText.setStrategyText(codeStr);
            labService.updateUserStrategyConditionById(strategyText);
        }

        json.put("status", "success");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/Json; charset=utf-8");
        response.getWriter().print(json.toString());
    }

}
