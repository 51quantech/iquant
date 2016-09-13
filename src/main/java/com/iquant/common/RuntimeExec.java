package com.iquant.common;

import com.iquant.util.CountKRIUtil;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonggangli on 2016/9/2.
 */
public class RuntimeExec {

    private final static Logger LOG = Logger.getLogger(RuntimeExec.class);

    public static String excuteCmd(String[] cmdarry) {

        Process process = null;
        StringBuffer result = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(cmdarry);
//			BufferedReader input = new BufferedReader(new InputStreamReader(
//					process.getInputStream()));
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            InputStreamReader ire = new InputStreamReader(process.getErrorStream());
            LineNumberReader errorInput = new LineNumberReader(ire);
            String line = "";
            while ((line = input.readLine()) != null) {
                result.append(line).append("\n");
            }
			while ((line = errorInput.readLine()) != null) {
				result.append(line).append("\n");
			}
            int exitValue = process.waitFor();
            input.close();
            errorInput.close();
            if (0 != exitValue) {
                return exitValue + "";
            }
        } catch (Exception e) {
            LOG.error(cmdarry, e);
            return 1 + "";
        }
        return result.toString();
    }

    public static Map<String,Object> excuteCmd(String cmd, WebSocketSession session, String CODE) {

        Process process = null;
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            process = Runtime.getRuntime().exec(cmd);
//			BufferedReader input =
//					process.getInputStream()));
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            InputStreamReader eir = new InputStreamReader(process.getErrorStream());
            LineNumberReader einput = new LineNumberReader(eir);

            String line = "";
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if(line.startsWith(CODE)){
                    String output = line;
                    output = output.replaceAll("'", "\"");
                    System.out.println(output);
                    try {
                        data = CountKRIUtil.countKRI(output);
                        data.put("status", "success");
                    } catch (Exception e) {
                        data.put("status", "fail");
                        e.printStackTrace();
                    }
                    try {
                        data.put("data", data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String jsonStr = JsonUtil.map2json(data);
                    session.sendMessage(new TextMessage(jsonStr));
                }
            }

            String eline = "";
            StringBuilder eBuffer = new StringBuilder();
            while ((eline = einput.readLine()) != null) {
                eBuffer.append(eline);
            }
            data.put("error", eBuffer.toString());
            int exitValue = process.waitFor();
            input.close();
            einput.close();
            if (0 != exitValue) {
                System.out.println(exitValue);
                return data;
            }
        } catch (Exception e) {
            LOG.error(cmd, e);
            return data;
        }
        return data;
    }

}
