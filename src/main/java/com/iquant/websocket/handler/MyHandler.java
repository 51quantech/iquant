package com.iquant.websocket.handler;

import com.iquant.common.FoldeUtil;
import com.iquant.common.JsonUtil;
import com.iquant.common.RuntimeExec;
import com.iquant.util.CountKRIUtil;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class MyHandler extends TextWebSocketHandler {

    private final static Logger LOG = Logger.getLogger(MyHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        // session.sendMessage(message);
        System.out.println(session.getAttributes().get("inquiryId"));
        System.out.println(session.getUri());
        System.out.println(session.getId());

        if (message.getPayload().equals("1")) {

            Map<String, Object> res = execRuningShell(
                    "python " + FoldeUtil.getPythonHome() + " strategy.py ", session);
            String output = res.get("output").toString();
            output = output.replaceAll("'", "\"");
            Map<String, Object> data = new HashMap<String, Object>();

            Map<String, Object> jsonChart = new HashMap<String, Object>();
            try {
                data = CountKRIUtil.countKRI(output);
                jsonChart.put("status", "success");
            } catch (Exception e) {
                jsonChart.put("status", "fail");
                e.printStackTrace();
            }
            jsonChart.put("data", data);
            Object errorChart = res.get("error");
            jsonChart.put("error",
                    errorChart == null ? "" : errorChart.toString());

            Map<String, Object> json = new HashMap<String, Object>();
            try {
                data = CountKRIUtil.countKRIDetail(output);
                json.put("status", "success");
            } catch (Exception e) {
                json.put("status", "fail");
                e.printStackTrace();
            }
            Object error = res.get("error");
            json.put("data", data);
            json.put("error", error == null ? "" : error.toString());
            String jsonStr = JsonUtil.map2json(json);

            session.sendMessage(new TextMessage("[finish]" + jsonStr));

        }

    }

    private Map<String, Object> execRuningShell(String cmd,WebSocketSession webSocketSession) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        result = RuntimeExec.excuteCmd(cmd,webSocketSession,"[C0001]");
        return result;
    }

}
