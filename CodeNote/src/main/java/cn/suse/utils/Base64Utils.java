package cn.suse.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * BASE64编码解码工具包
 */
public class Base64Utils {

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * BASE64字符串解码为二进制数据
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decodeBase64(base64.getBytes());
    }

    /**
     * 二进制数据编码为BASE64字符串
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * 将文件编码为BASE64字符串  大文件慎用，可能会导致内存溢出
     * @param filePath 文件绝对路径
     * @return
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * <p>
     * BASE64字符串转回文件
     * @param filePath 文件绝对路径
     * @param base64 编码字符串
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }

    /**
     * 文件转换为二进制数组
     * @param filePath  文件路径
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /**
     * 二进制数据写文件bytes 二进制数据
     * @param filePath 文件生成目录
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }
    public static void main(String[] args) throws Exception {
		String str = "你好， i am abc";
		str = encode(str.getBytes());
		System.out.println(new String(decode(str)));//中文友好
		
		str = "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";
		String fileName = "D:/ccc.png";
		decodeToFile(fileName, str);
		System.out.println(str.length());
		System.out.println(encodeFile(fileName).length());
		
		str = "https://www.baidu.com/s?ie=UTF-8&wd=english%20%E4%B8%AD%E6%96%87&param=这个参数手动加的";
		System.out.println(str);
		System.out.println(encode(str.getBytes()));
		System.out.println(new String(decode(encode(str.getBytes()))));
	}
}