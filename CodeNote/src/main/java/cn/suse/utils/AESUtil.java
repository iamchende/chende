package cn.suse.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * 高级加密标准(AES,Advanced Encryption Standard)为最常见的对称加密算法
 * 加密和解密用到的密钥是相同的，这种加密方式加密速度非常快，适合经常发送数据的场合。缺点是密钥的传输比较麻烦。
 * 
 * 实际中，一般是通过RSA加密AES的密钥，传输到接收方，接收方解密得到AES密钥，然后发送方和接收方用AES密钥来通信。
 * 类AESUtil.java的实现描述：TODO 类实现描述 
 * @author chende 2019年9月1日 下午5:23:32
 */
@Slf4j
public class AESUtil {
	/**
	 * 算法/模式/补码方式
	 */
    private static final String SECURE_KEY   = "AES/ECB/PKCS5Padding";
    /**
     * 编码格式
     */
    private static final String CHARSET_UTF8 = "UTF-8";

    private AESUtil() {
        throw new IllegalStateException("Utility class need not be instantiated.");
    }

    /**
     * AES加密，加密后使用URLEncoder处理
     * 
     * @param encryptContent 待加密内容
     * @param encryptKey 加密秘钥
     * @return 成功返回密文，失败返回null
     */
    public static String encryptWithURLEncoder(String encryptContent, String encryptKey) {
        if (StringUtils.isBlank(encryptContent) || StringUtils.isBlank(encryptKey)) {
            log.warn("encryptContent or encryptKey can't be null.");
            return null;
        }
        try {
            byte[] raw = encryptKey.getBytes(CHARSET_UTF8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(SECURE_KEY);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptContent.getBytes(CHARSET_UTF8));
            return URLEncoder.encode(new String(Base64.getUrlEncoder().encode(encrypted), CHARSET_UTF8), CHARSET_UTF8);
        } catch (Exception e) {
            log.error("Encrypting failed.", e);
        }
        return null;
    }

    /**
     * AES加密，加密后不使用URLEncoder处理
     * 
     * @param encryptContent 待加密内容
     * @param encryptKey 加密秘钥
     * @return 成功返回密文，失败返回null
     */
    public static String encryptWithoutURLEncoder(String encryptContent, String encryptKey) {
        if (StringUtils.isBlank(encryptContent) || StringUtils.isBlank(encryptKey)) {
            log.warn("encryptContent or encryptKey can't be null.");
            return null;
        }
        try {
            byte[] raw = encryptKey.getBytes(CHARSET_UTF8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(SECURE_KEY);// "算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptContent.getBytes(CHARSET_UTF8));
            return new String(Base64.getUrlEncoder().encode(encrypted), CHARSET_UTF8);// 此处使用BASE64 url安全模式编码
        } catch (Exception e) {
            log.error("Encrypting failed.", e);
        }
        return null;
    }

    /**
     * AES解密，解密前使用URLDecoder处理
     * 
     * @param decryptContent 待解密内容
     * @param decryptKey 解密秘钥
     * @return 成功返回明文，失败返回null
     */
    public static String decryptWithURLDecoder(String decryptContent, String decryptKey) {
        if (StringUtils.isBlank(decryptContent) || StringUtils.isBlank(decryptKey)) {
            log.warn("decryptContent or decryptKey can't be null.");
            return null;
        }
        try {
            byte[] raw = decryptKey.getBytes(CHARSET_UTF8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(SECURE_KEY);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getUrlDecoder().decode(URLDecoder.decode(decryptContent, CHARSET_UTF8).getBytes(CHARSET_UTF8));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, CHARSET_UTF8);
        } catch (Exception ex) {
            log.error("Decrypting failed.", ex);
        }
        return null;
    }

    /**
     * AES解密，解密前不使用URLDecoder处理
     * 
     * @param decryptContent 待解密内容
     * @param decryptKey 解密秘钥
     * @return 成功返回明文，失败返回null
     */
    public static String decryptWithoutURLDecoder(String decryptContent, String decryptKey) {
        if (StringUtils.isBlank(decryptContent) || StringUtils.isBlank(decryptKey)) {
            log.warn("decryptContent or decryptKey can't be null.");
            return null;
        }
        try {
            byte[] raw = decryptKey.getBytes(CHARSET_UTF8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(SECURE_KEY);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getUrlDecoder().decode(decryptContent.getBytes(CHARSET_UTF8));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, CHARSET_UTF8);
        } catch (Exception ex) {
            log.error("Decrypting failed.", ex);
        }
        return null;
    }

    public static void main(String[] args){
    	/**
    	 * 128位、192位或256位,此处是128位16个字节
    	 */
    	String key = "abcdefghijklmnop";//16位
        String fileName = "中文编码.png";
        String f1 = encryptWithURLEncoder(fileName,key);
        System.out.println(f1);
        String f2 = decryptWithURLDecoder(f1, key);
        System.out.println(f2);
        String f3 = encryptWithoutURLEncoder(fileName,key);
        System.out.println(f3);
        String f4 = decryptWithoutURLDecoder(f3, key);
        System.out.println(f4);
        
        fileName = "https://blog.csdn.net/gulang03/article/details/81175854";
        f1 = encryptWithURLEncoder(fileName,key);
        System.out.println(f1);
        f2 = decryptWithURLDecoder(f1, key);
        System.out.println(f2);
        f3 = encryptWithoutURLEncoder(fileName,key);
        System.out.println(f3);
        f4 = decryptWithoutURLDecoder(f3, key);
        System.out.println(f4);
    }
}
