package com.iquant.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class NetWorkProtocol {


    /**
     * 成功 C0000
     */
    public static final String SUCCESS = "C0000";

    /**
     * 成功描述
     */
    public static final String SUCCESS_MSG = "C0000";

    /**
     * 连接地址不可达 C0001
     */
    public static final String EROOR_NOT_FIND = "C0001";

    /**
     * 连接地址不可达描述
     */
    public static final String EROOR_NOT_FIND_MSG = "连接地址不可达";

    // -----------------------------------HTTP请求的放回参数设置-----------------------------------
    /**
     * 响应的状态协议
     */
    public static final String RESPONSE_STATUE_PROTOCOL = "procotol";
    /**
     * 响应的状态码
     */
    public static final String RESPONSE_STATUE_CODE = "code";
    /**
     * 响应的状态码的描述
     */
    public static final String RESPONSE_STATUE_CODE_MESSAGE = "message";
    /**
     * 响应内容
     */
    public static final String RESPONSE_CONTENT = "content";
    /**
     * 响应内容的长度
     */
    public static final String RESPONSE_CONTENT_LENGTH = "length";
    /**
     * 响应内容的编码格式
     */
    public static final String RESPONSE_CONTENT_ENCODEING = "encoding";
    /**
     * 响应内容的类型
     */
    public static final String RESPONSE_CONTENT_TYPE = "type";
    /**
     * 响应的简略错误
     */
    public static final String RESPONSE_ERROR = "error";
    /**
     * 响应的详细错误
     */
    public static final String RESPONSE_ERROR_DETAIL = "error_detail";

    // -----------------------------------邮件参数设置-----------------------------------
    /**
     * 设置发信的smtp服务器
     */
    public static final String EMAIL_HOST_NAME = "smtp.exmail.qq.com";
    /**
     * 设置发信人的EMAIL
     */
    public static final String EMAIL_FROM_EMAIL = "liyonggang@pezy.cn";
    /**
     * 设置发信人的名称
     */
    public static final String EMAIL_FROM_NAME = "liyonggang";
    /**
     * 设置发信人的密码
     */
    public static final String EMAIL_FROM_PWD = "";

    private NetWorkProtocol() {
    }

    /**
     * <li>通过HTTP协议、已GET方式发送请求</li>
     *
     * @return 返回Map <li>URLCode.RESPONSE_STATUE_PROTOCOL:响应状态协议</li> <li>
     * URLCode.RESPONSE_STATUE_CODE:响应状态吗</li> <li>
     * URLCode.RESPONSE_STATUE_CODE_MESSAGE:响应状态码的描述</li> <li>
     * URLCode.RESPONSE_CONTENT_TYPE:内容类型</li> <li>
     * URLCode.RESPONSE_CONTENT_ENCODEING:编码格式</li> <li>
     * URLCode.RESPONSE_CONTENT_LENGTH:内容长度</li> <li>
     * URLCode.RESPONSE_CONTENT:内容</li> <li>
     * 连接错误返回 null</li>
     * @url 访问地址
     */
    public static Map<String, String> get(String url) {
        return get(url, "UTF-8");
    }

    /**
     * <li>通过HTTP协议、已GET方式发送请求</li>
     *
     * @return 返回Map <li>URLCode.RESPONSE_STATUE_PROTOCOL:响应状态协议</li> <li>
     * URLCode.RESPONSE_STATUE_CODE:响应状态吗</li> <li>
     * URLCode.RESPONSE_STATUE_CODE_MESSAGE:响应状态码的描述</li> <li>
     * URLCode.RESPONSE_CONTENT_TYPE:内容类型</li> <li>
     * URLCode.RESPONSE_CONTENT_ENCODEING:编码格式</li> <li>
     * URLCode.RESPONSE_CONTENT_LENGTH:内容长度</li> <li>
     * URLCode.RESPONSE_CONTENT:内容</li> <li>
     * 连接错误返回 null</li>
     * @url 访问地址
     * @charset 编码格式
     */
    public static Map<String, String> get(String url, String charset) {
        Map<String, String> data = new HashMap<String, String>();
        // 创建默认实例
        HttpClient httpClient = new DefaultHttpClient();
        // 设置socket连接超时
        httpClient.getParams().setParameter("http.socket.timeout", 30000);
        httpClient.getParams().setParameter("http.connection.timeout", 30000);
        // Get提交方式
        HttpGet httpGet = new HttpGet(url);
        try {
            // 执行Get请求
            HttpResponse response = httpClient.execute(httpGet);
            if (response == null) {
                return null;
            }
            setHeadData(data, response);
            response.setHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=" + charset);
            // 获得内容实体
            HttpEntity httpEntity = response.getEntity();
//			System.out.println(httpEntity.getContentEncoding());
            if (null != httpEntity) {
                setContentDate(data, httpEntity, charset);
                response = null;
                httpEntity = null;
                return data;
            }
            return null;
        } catch (ClientProtocolException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } finally {
            // 关闭连接、释放资源
            if (null != httpClient) {
                httpClient.getConnectionManager().shutdown();
            }
            httpGet = null;
            httpClient = null;
        }
        return data;
    }


    /**
     * <li>通过HTTP协议、已GET方式发送请求</li>
     * <p>
     * 用于自动部署自动重启的监控
     */
    public static Map<String, String> getForMonitor(String url, String charset) {
        Map<String, String> data = new HashMap<String, String>();
        // 创建默认实例
        HttpClient httpClient = new DefaultHttpClient();
        // 设置socket连接超时
        httpClient.getParams().setParameter("http.socket.timeout", 15000);
        httpClient.getParams().setParameter("http.connection.timeout", 15000);
        // Get提交方式
        HttpGet httpGet = new HttpGet(url);
        try {
            // 执行Get请求
            HttpResponse response = httpClient.execute(httpGet);
            if (response == null) {
                return null;
            }
            setHeadData(data, response);
            response.setHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=" + charset);
            // 获得内容实体
            HttpEntity httpEntity = response.getEntity();
//			System.out.println(httpEntity.getContentEncoding());
            if (null != httpEntity) {
                setContentDate(data, httpEntity, charset);
                response = null;
                httpEntity = null;
                return data;
            }
            return null;
        } catch (ClientProtocolException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } finally {
            // 关闭连接、释放资源
            if (null != httpClient) {
                httpClient.getConnectionManager().shutdown();
            }
            httpGet = null;
            httpClient = null;
        }
        return data;
    }


    /**
     * @param url 访问的连接地址
     * @param kv  参数
     * @return 返回Map <li>URLCode.RESPONSE_STATUE_PROTOCOL:响应状态协议</li> <li>
     * URLCode.RESPONSE_STATUE_CODE:响应状态吗</li> <li>
     * URLCode.RESPONSE_STATUE_CODE_MESSAGE:响应状态码的描述</li> <li>
     * URLCode.RESPONSE_CONTENT_TYPE:内容类型</li> <li>
     * URLCode.RESPONSE_CONTENT_ENCODEING:编码格式</li> <li>
     * URLCode.RESPONSE_CONTENT_LENGTH:内容长度</li> <li>
     * URLCode.RESPONSE_CONTENT:内容</li> <li>
     * 连接错误返回 null</li>
     * @charset 编码格式
     */
    public static Map<String, String> post(String url, Map<String, String> kv,
                                           String charset) {
        HttpClient httpClient = new DefaultHttpClient();
        // socket连接超时限制
        httpClient.getParams().setParameter("http.socket.timeout", 30000);
        httpClient.getParams().setParameter("http.connection.timeout", 30000);
        // POST提交方式
        HttpPost httpPost = new HttpPost(url);
        // 参数列表
        List<NameValuePair> fromParams = new ArrayList<NameValuePair>();
        // 添加具体参数
        if (null != kv) {
            for (Map.Entry<String, String> entry : kv.entrySet()) {
                fromParams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        // 设置参数列表的编码格式
        UrlEncodedFormEntity uefEntity = null;
        Map<String, String> data = new HashMap<String, String>();
        try {
            uefEntity = new UrlEncodedFormEntity(fromParams, charset);
            // 向POST方法中设置内容实体
            httpPost.setEntity(uefEntity);
            // 执行POST方法
            HttpResponse response = httpClient.execute(httpPost);
            if (response == null) {
                return null;
            }
            setHeadData(data, response);
            // 获得内容实体
            HttpEntity httpEntity = response.getEntity();
            if (null != httpEntity) {
                setContentDate(data, httpEntity, charset);
                response = null;
                httpEntity = null;
                return data;
            }
        } catch (ClientProtocolException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            setErrorStatue(data, e);
//			System.err.println(e.getMessage());
        } finally {
            if (null != httpClient) {
                httpClient.getConnectionManager().shutdown();
            }
            httpPost = null;
            httpClient = null;
        }
        return data;
    }

    /**
     * @param url 访问的连接地址
     * @param kv  参数
     * @return 返回Map <li>URLCode.RESPONSE_STATUE_PROTOCOL:响应状态协议</li> <li>
     * URLCode.RESPONSE_STATUE_CODE:响应状态吗</li> <li>
     * URLCode.RESPONSE_STATUE_CODE_MESSAGE:响应状态码的描述</li> <li>
     * URLCode.RESPONSE_CONTENT_TYPE:内容类型</li> <li>
     * URLCode.RESPONSE_CONTENT_ENCODEING:编码格式</li> <li>
     * URLCode.RESPONSE_CONTENT_LENGTH:内容长度</li> <li>
     * URLCode.RESPONSE_CONTENT:内容</li> <li>
     * 连接错误返回 null</li>
     */
    public static Map<String, String> post(String url, Map<String, String> kv) {
        return post(url, kv, "UTF-8");
    }

    // 私有方法，只供内部调用，设置响应状态头
    private static void setHeadData(Map<String, String> data,
                                    HttpResponse response) {
        data.put(RESPONSE_STATUE_PROTOCOL, response.getStatusLine()
                .getProtocolVersion().toString());
        data.put(RESPONSE_STATUE_CODE,
                String.valueOf(response.getStatusLine().getStatusCode()));
        data.put(RESPONSE_STATUE_CODE_MESSAGE, response.getStatusLine()
                .getReasonPhrase());
    }

    // 私有方法，只供内部调用，设置响应的内容
    private static void setContentDate(Map<String, String> data,
                                       HttpEntity httpEntity, String charset)
            throws IllegalStateException, IOException {
        data.put(RESPONSE_CONTENT_TYPE,
                httpEntity.getContentType() == null ? "" : httpEntity
                        .getContentType().toString());
        data.put(RESPONSE_CONTENT_ENCODEING,
                httpEntity.getContentEncoding() == null ? "" : httpEntity
                        .getContentEncoding().toString());
        data.put(RESPONSE_CONTENT_LENGTH,
                String.valueOf(httpEntity.getContentLength()));
        InputStream inputStream = httpEntity.getContent();
        /*
		 * 获得响应的内容的流 在网络环境下，不要使用inputStream的
		 * avaliable()方法，网络阻塞下会返回0，可以通过httpEntity .getContentLength()来获得内容长度
		 */
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
                inputStream, charset));
        StringBuffer readLine = new StringBuffer();
        String tmp = "";
        while (null != (tmp = bufferRead.readLine())) {
            readLine.append(tmp);
        }
        // String content = readLine.toString();
        // content = new String(content.getBytes(data
        // .get(URLCode.RESPONSE_CONTENT_ENCODEING)), "utf-8");
        data.put(RESPONSE_CONTENT, readLine.toString());
        inputStream = null;
        bufferRead = null;
    }

    // 设置http连接时的错误信息
    private static void setErrorStatue(Map<String, String> data, Exception e) {
        data.put(RESPONSE_STATUE_CODE, EROOR_NOT_FIND);
        data.put(RESPONSE_STATUE_CODE_MESSAGE, EROOR_NOT_FIND_MSG);
        // data.put(RESPONSE_CONTENT, e.getMessage());
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer, true));
        data.put(RESPONSE_ERROR, e.getMessage());
        data.put(RESPONSE_ERROR_DETAIL, writer.toString());
    }

    /**
     * 发送一封email 此方法可以设置发件的各种状态
     *
     * @param host
     *            发信的smtp服务器
     * @param to
     *            收件人账号
     * @param toName
     *            收件人
     * @param fromMail
     *            发件人账号
     * @param fromName
     *            发件人
     * @param password
     *            密码
     * @param subject
     *            主题
     * @param body
     *            内容
     * @throws EmailException
     *             邮件发送异常
     */
    // public static void email(String host, String to, String toName,
    // String from, String fromName, String password, String subject,
    // String body) throws EmailException {
    // HtmlEmail email = new HtmlEmail();
    // // 设置发信的smtp服务器
    // email.setHostName(host);
    // email.setSubject(subject);
    // // 如果smtp服务器需要认证的话，在这里设置帐号、密码
    // email.setAuthentication(from, password);
    // // 设置邮件正文和字符编码
    // // 设置收件人帐号和收件人
    // email.addTo(to, toName);
    // // 设置发信的邮件帐号和发信人
    // email.setFrom(from, fromName);
    // // 设置邮件主题
    // email.setHtmlMsg(body);
    // email.send();
    // }

    /**
     * 发送一封email 此方法提供一个简洁入口
     *
     * @param to
     *            收件人账号
     * @param toName
     *            收件人
     * @param subject
     *            主题
     * @param body
     *            内容
     * @throws EmailException
     *             邮件发送异常
     */
    // public static void email(String to, String toName, String subject,
    // String body) throws EmailException {
    // HtmlEmail email = new HtmlEmail();
    // // 设置发信的smtp服务器
    // email.setHostName(EMAIL_HOST_NAME);
    // email.setSubject(subject);
    // // 如果smtp服务器需要认证的话，在这里设置帐号、密码
    // email.setAuthentication(EMAIL_FROM_EMAIL, EMAIL_FROM_PWD);
    // // 设置邮件正文和字符编码
    // // 设置收件人帐号和收件人
    // email.addTo(to, toName);
    // // 设置发信的邮件帐号和发信人
    // email.setFrom(EMAIL_FROM_EMAIL, EMAIL_FROM_NAME);
    // // 设置邮件主题
    // email.setHtmlMsg(body);
    // email.send();
    // }

}
