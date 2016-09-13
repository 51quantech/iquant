package com.iquant.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class JsonUtil {
    private final static Logger logger = Logger.getLogger(JsonUtil.class);

    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String || obj instanceof Integer
                || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double
                || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        } else if (obj instanceof Object[]) {
            json.append(array2json((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(list2json((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(map2json((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(set2json((Set<?>) obj));
        } else {
            json.append(bean2json(obj));
        }
        return json.toString();
    }

    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class)
                    .getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = object2json(props[i].getName());
                    String value = object2json(props[i].getReadMethod().invoke(
                            bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

    public static String jsonEncode(Object obj) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
            public boolean apply(Object source, String name, Object value) {
                if (value != null) {
                    if (Number.class.isAssignableFrom(value.getClass())) {
                        return false;
                    }
                    if (value.getClass().equals(String.class)) {
                        return false;
                    }
                    if (value.getClass().equals(Date.class)) {
                        return false;
                    }
                    if (value.getClass().equals(Boolean.class)) {
                        return false;
                    }
                    if (value.getClass().equals(JSONObject.class)) {
                        return false;
                    }
                }
                return true;
            }
        });
        if (obj instanceof List<?>) {
            JSONArray jsonArray = JSONArray.fromObject(obj, jsonConfig);
            return jsonArray.toString();
        } else {
            JSONObject jsonObject = JSONObject.fromObject(obj, jsonConfig);
            return jsonObject.toString();
        }
    }

    /**
     * 没有类型的过滤处理，直接转成string
     *
     * @param obj
     * @return
     */
    public static String jsonEncode2(Object obj) {

        try {
            if (obj instanceof List<?>) {
                JSONArray jsonArray = JSONArray.fromObject(obj);
                return jsonArray.toString();
            } else {
                JSONObject jsonObject = JSONObject.fromObject(obj);
                return jsonObject.toString();
            }
        } catch (Exception e) {
            logger.error("jsonEncode2把字符串解析成json对象异常", e);
            return null;
        }
    }

    /**
     * json串转换成指定类型的对象
     *
     * @param s
     * @param type
     * @return
     */
    public static Object jsonDecode(String s, Class<?> type) {

        JSONObject json = JSONObject.fromObject(s);
        return JSONObject.toBean(json, type);
    }

    /**
     * json串转换成指定类型的对象或对象list
     *
     * @param s
     * @param type
     * @param isList
     * @return
     */
    public static Object jsonDecode(String s, Class<?> type, Boolean isList) {
        if (!isList)
            return jsonDecode(s, type);

        JSONArray json = JSONArray.fromObject(s);
        return JSONArray.toCollection(json, type);
    }

    /**
     * 解析字符串成json
     *
     * @param sJson
     * @return
     */
    public static JSONObject getJsonObject(String sJson) {
        if (sJson != null && !sJson.isEmpty()) {
            try {
                return JSONObject.fromObject(sJson);
            } catch (Exception e) {
                logger.error("getJsonObj把字符串解析成json对象异常", e);
                return null;

            }
        }
        return null;
    }
}
