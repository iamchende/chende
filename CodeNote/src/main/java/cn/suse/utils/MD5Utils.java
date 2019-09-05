package cn.suse.utils;

import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

/**
 * MD5工具类
 */
@Slf4j
public class MD5Utils {
    public static String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toUpperCase();//返回32位大写
        } catch (Exception e) {
            log.error("MD5Utils.getMD5异常", e);
        }
        return result;
    }
    public static void main(String[] args) {
		String str = "MD5加密";
		System.out.println(getMD5(str));
	}
}
