package com.iquant.websocket.handler;

import com.iquant.common.FoldeUtil;
import com.iquant.common.JsonUtil;
import com.iquant.common.RuntimeExec;
import com.iquant.domain.beans.UserStrategy;
import com.iquant.service.LabService;
import com.iquant.util.CountKRIUtil;
import com.iquant.websocket.utils.TelSocketSessionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：WebSocket处理器 可以继承 {@link TextWebSocketHandler}/
 * {@link BinaryWebSocketHandler}， 或者简单的实现{@link WebSocketHandler}接口
 * 作者：liuxing(2015-01-25 03:42)
 */
public class TelWebSocketHandler extends TextWebSocketHandler {

    private final static Logger LOG = Logger.getLogger(TelWebSocketHandler.class);

    private final static String SYSTEM_CODE = "[C0001]:";

    @Autowired
    private LabService labService;

    /**
     * 建立连接
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        String inquiryId = MapUtils.getString(session.getAttributes(),
                "inquiryId");
        int empNo = MapUtils.getInteger(session.getAttributes(), "empNo");

        TelSocketSessionUtils.add(inquiryId, empNo, session);
        TelSocketSessionUtils.putKeyCache(inquiryId, empNo);
    }

    /**
     * 收到客户端消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        String inquiryId = MapUtils.getString(session.getAttributes(),
                "inquiryId");
        int empNo = MapUtils.getInteger(session.getAttributes(), "empNo");

        if (message.getPayload().equals("1")) {

            String output_python = FoldeUtil.getPythonFolder(inquiryId) + "/"
                    + FoldeUtil.getPythonFileName(empNo, inquiryId);
            String cmds = "python " + FoldeUtil.getPythonHome() + "/resources/" + FoldeUtil.getPythonFolder(inquiryId) + "/" + FoldeUtil.getPythonFolder(inquiryId + empNo) + ".py";
            System.out.println("=============================" + cmds);
            String synchronizedCode = TelSocketSessionUtils.getKeyCache(
                    inquiryId, empNo);

            synchronized (synchronizedCode) {

                Map<String, Object> res = RuntimeExec.excuteCmd(cmds, session, SYSTEM_CODE);

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
                String jsonChartStr = JsonUtil.map2json(jsonChart);

                try {
                    UserStrategy us = new UserStrategy();
                    us.setId(empNo);
                    us.setStrategyStatus(2);
                    us.setStrategyResult(jsonChartStr);
                    labService.updateUserStrategyConditionById(us);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.sendMessage(new TextMessage(SYSTEM_CODE + "[finish]1" + jsonChartStr));

                Map<String, Object> json = new HashMap<String, Object>();
                try {
                    data = CountKRIUtil.countKRIDetail(output);
                    // data.put("sharpeRatio", 0.0);
                    json.put("status", "success");
                } catch (Exception e) {
                    json.put("status", "fail");
                    e.printStackTrace();
                }
                Object error = res.get("error");
                json.put("data", data);
                json.put("error", error == null ? "" : error.toString());
                String jsonStr = JsonUtil.map2json(json);

                try {
                    UserStrategy us = new UserStrategy();
                    us.setId(empNo);
                    us.setStrategyStatus(3);
                    us.setStrategyDetailResult(jsonStr);
                    labService.updateUserStrategyConditionById(us);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TelSocketSessionUtils.sendMessage(inquiryId, empNo,
                // "[finish]"
                // + jsonStr);
                session.sendMessage(new TextMessage(SYSTEM_CODE + "[finish]2" + jsonStr));

            }

        }

    }

    /**
     * 出现异常
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {

        String inquiryId = MapUtils.getString(session.getAttributes(),
                "inquiryId");
        int empNo = MapUtils.getInteger(session.getAttributes(), "empNo");

        LOG.error("websocket connection exception: "
                + TelSocketSessionUtils.getKey(inquiryId, empNo));
        LOG.error(exception.getMessage(), exception);

        TelSocketSessionUtils.remove(inquiryId, empNo);
    }

    /**
     * 连接关闭
     *
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {

        String inquiryId = MapUtils.getString(session.getAttributes(),
                "inquiryId");
        int empNo = MapUtils.getInteger(session.getAttributes(), "empNo");
        TelSocketSessionUtils.remove(inquiryId, empNo);
    }

    /**
     * 是否分段发送消息
     *
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public static void main(String[] args) {
        // RemoteShellTool remoteShellTool = new RemoteShellTool("","");
    }

}