package net.bewithu.questioncommunity.Service;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;

@Service
public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * MD5加密组件
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
    /**
     * 字符串判断是否合法
     */
    public static boolean judgeLegal(String next){
        if(!StringUtils.isEmpty(next)&&next.charAt(0)=='/'){
            return true;
        }
        return  false;
    }

    /**
     * JSON辅助初始化
     */
    public static String returnJson(int code,String msg){
        JSONObject json =new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        return  json.toJSONString();
    }
}
