package com.iquant.common;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * Created by yonggangli on 2016/9/2.
 */
public class FoldeUtil {

    // 以下为创建文件以及编码相关的方法
    public static String getPythonHome() {
//        return "F:/data-iquant/resources/";
        return "/data-iquant/resources/";
    }

    // 获取当前用户使用的脚本名称
    public static String getPythonFileName(HttpServletRequest request) {
        String id = request.getParameter("id");
        String uname = WebHelper.getLoginPin();
        String fileName = "strategy";
        if (StringUtils.isNotBlank(uname)) {
            String temp = uname + (StringUtils.isNotBlank(id) ? id : "0");
            try {
                fileName = Base64Helper.encode(temp.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    // 获取当前用户使用的脚本名称
    public static String getPythonFileName(int id, String uname) {
        // String uname = WebHelper.getPin();
        String fileName = "strategy";
        if (StringUtils.isNotBlank(uname)) {
            String temp = uname + id;
            try {
                fileName = Base64Helper.encode(temp.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    // 获取当前用户使用的脚本的目录
    public static String getAndCreatePythonFolder() {
        String folderName = getPythonFolder();
        if (StringUtils.isNotBlank(folderName)) {
            File folder = new File(getPythonHome() + "/resources/" + folderName);
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdir();
            }
        }
        return folderName;
    }

    // 获取当前用户使用的脚本的目录
    public static String getPythonFolder() {
        String folderName = null;
        try {
            folderName = Base64Helper.encode(WebHelper.getLoginPin().getBytes(
                    "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folderName;
    }

    // 获取当前用户使用的脚本的目录
    public static String getPythonFolder(String username) {
        String folderName = null;
        try {
            folderName = Base64Helper.encode(username.getBytes(
                    "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folderName;
    }

    public static String buildPythonFile(HttpServletRequest request, String codeStr) {
        BufferedReader br = null;
        String readLine = null;
        StringBuffer pyPrame = new StringBuffer();
        // String pramePath = "/data-open-quant/prame.py";
        String pramePath = getPythonHome() + "/prame.py";

        String fileName = getPythonFileName(request);

        String folderName = getAndCreatePythonFolder();

        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(folderName)) {
            return null;
        }

        String outputPath = getPythonHome() + "/resources/" + folderName + "/"
                + fileName + ".py";

        // String outputPath = getPythonHome() + "/" + fileName
        // + ".py";

        try {
            br = new BufferedReader(new FileReader(pramePath));

            while ((readLine = br.readLine()) != null) {
                pyPrame.append(readLine + "\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newPyPrame = pyPrame.toString().replaceAll("REPLACE", codeStr);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
            bw.write(newPyPrame);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folderName + "/" + fileName;
        // return fileName;

    }

}
