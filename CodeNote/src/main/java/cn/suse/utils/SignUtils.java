package cn.suse.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

public class SignUtils {
	
    /**
     * 加签方法(map对象)
     *
     * @throws UnsupportedEncodingException
     */
    public static String sign(String jsonStr, String timestamp, String salt) throws UnsupportedEncodingException {
        Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
        map.put("timestamp", timestamp);
        return sign(map, salt);
    }

    private static String sign(Map<String, Object> map, String salt) throws UnsupportedEncodingException {
        String genSign = "";

        StringBuilder stringBuilder = new StringBuilder();
        //将map放入treeMap进行升序排序
        TreeMap<String, Object> treeMap = sortMap(map);
        String treeMapStr = JSONObject.toJSONString(treeMap);
        stringBuilder.append(treeMapStr);
        //将私钥加在排好序的map字符串后
        String orgStr = stringBuilder.append(salt).toString();

        genSign = DigestUtils.md5Hex(orgStr.getBytes("UTF-8"));//指定编码格式
        return genSign;
    }

    /**
     * 验签返回值,1通过，-1不通过
     *
     * @param res
     * @param salt
     * @return
     * @throws UnsupportedEncodingException
     */
    public static int signVerify(String res, String salt) throws UnsupportedEncodingException {
        JSONObject obj = JSONObject.parseObject(res, Feature.OrderedField);
        String signValue = obj.getString("signValue");
        if (!StringUtils.isEmpty(signValue)) {
            obj.remove("signValue");
            String sign = sign(obj, salt);
            if (!signValue.equals(sign)) {
                return -1;
            }
        }
        return 1;
    }

    /**
     * map排序
     * @param map
     * @return
     */
    private static TreeMap<String, Object> sortMap(Map<String, Object> map) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                TreeMap<String, Object> subTreeMap = sortMap((Map<String, Object>) entry.getValue());
                treeMap.put(entry.getKey(), subTreeMap);
            } else {
                treeMap.put(entry.getKey(), entry.getValue());
            }
        }
        return treeMap;
    }
    
    public static void main(String[] args) {
		String salt = "加点盐";
		Map<String, Object> data = new HashMap<>();
		data.put("userId", "123456");
		data.put("returnUrl", "www.baidu.com");
		data.put("accKey", "666666");
		String timestamp = new Date().toString();
		try {
			String sign = sign(JSON.toJSONString(data), timestamp, salt);
			data.put("signValue", sign);
			data.put("timestamp", timestamp);
			System.out.println(signVerify(JSON.toJSONString(data), salt));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
