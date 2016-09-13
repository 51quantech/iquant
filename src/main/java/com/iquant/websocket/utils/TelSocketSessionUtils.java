package com.iquant.websocket.utils;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能说明：TelSocketSessionUtils 作者：liuxing(2014-12-26 02:32)
 */
public class TelSocketSessionUtils {

    private final static Logger LOG = Logger.getLogger(TelSocketSessionUtils.class);

    private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<String, WebSocketSession>();
    private static Map<String, String> keyCache = new ConcurrentHashMap<String, String>();

    /**
     * 保存一个连接
     *
     * @param inquiryId
     * @param empNo
     * @param session
     */
    public static void add(String inquiryId, int empNo, WebSocketSession session) {
        clients.put(getKey(inquiryId, empNo), session);
    }

    /**
     * 获取一个连接
     *
     * @param inquiryId
     * @param empNo
     * @return
     */
    public static WebSocketSession get(String inquiryId, int empNo) {
        return clients.get(getKey(inquiryId, empNo));
    }

    /**
     * 移除一个连接
     *
     * @param inquiryId
     * @param empNo
     */
    public static void remove(String inquiryId, int empNo) throws IOException {
        clients.remove(getKey(inquiryId, empNo));
    }

    /**
     * 组装sessionId
     *
     * @param inquiryId
     * @param empNo
     * @return
     */
    public static String getKey(String inquiryId, int empNo) {
        return inquiryId + "_" + empNo;
    }

    /**
     * 判断是否有效连接 判断是否存在 判断连接是否开启 无效的进行清除
     *
     * @param inquiryId
     * @param empNo
     * @return
     */
    public static boolean hasConnection(String inquiryId, int empNo) {
        String key = getKey(inquiryId, empNo);
        if (clients.containsKey(key)) {
            return true;
        }

        return false;
    }

    /**
     * 获取连接数的数量
     *
     * @return
     */
    public static int getSize() {
        return clients.size();
    }

    /**
     * 发送消息到客户端
     *
     * @param inquiryId
     * @param empNo
     * @param message
     * @throws Exception
     */
    public static void sendMessage(String inquiryId, int empNo, String message)
            throws Exception {
        if (!hasConnection(inquiryId, empNo)) {
            throw new NullPointerException(getKey(inquiryId, empNo)
                    + " connection does not exist");
        }

        WebSocketSession session = get(inquiryId, empNo);
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            LOG.error("websocket sendMessage exception: "
                    + getKey(inquiryId, empNo));
            LOG.error(e.getMessage(), e);
            clients.remove(getKey(inquiryId, empNo));
        }
    }

    /**
     * 添加缓存的websocket同步key值
     *
     * @param key
     * @param value
     */
    public static void putKeyCache(String inquiryId, int empNo) {
        if (getKeyCache(inquiryId, empNo) == null) {
            synchronized (TelSocketSessionUtils.class) {
                if (getKeyCache(inquiryId, empNo) == null) {
                    keyCache.put(getKey(inquiryId, empNo),
                            getKey(inquiryId, empNo));
                }
            }
        }

    }

    /**
     * 获得缓存的websocket同步的key值
     *
     * @param key
     * @return
     */
    public static String getKeyCache(String inquiryId, int empNo) {
        return keyCache.get(getKey(inquiryId, empNo));
    }

}