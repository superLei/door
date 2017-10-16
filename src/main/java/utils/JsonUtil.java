package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
	
	

	/**
	 *  获取指定json变量的值
	 *  @param JsonContext 要读取的字符串
	 *  @param id json中的ID
	 *  @return 返回json中指定的字符
	 * 
	 * */
	public static String json(String JsonContext, String id) throws IOException {
		String result = "";
//		ReplaceStr.class.getClassLoader().getResourceAsStream(
//					"path.json");
		JSONArray jsonArray = new JSONArray(JsonContext);
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			result = (String) jsonObject.get(id);	 
		}
		return result;
	}
	 
	
	/**
	 *  获取指定json变量的值
	 *  @param jsonObject 
	 *  @param id json中的ID
	 *  @return 返回json中指定的字符List
	 * */
	public static List<String> json(JSONObject jsonObject,String id) {
		List<String> listStr = new ArrayList<String>();
		for (int i = 0; i < jsonObject.length(); i++) {
			listStr.add((String) jsonObject.get(id));
		}
		return listStr;
	}
	
	
	/**
	 *  获取指定json变量的值
	 *  @param jsonObject
	 *  @param id json中的ID
	 *  @return 返回json中指定的字符
	 * 
	 * */
	public static String jsonToStr(JSONObject jsonObject,String id) {
		String listStr = "";
		for (int i = 0; i < jsonObject.length(); i++) {
			listStr = (String) jsonObject.get(id);
		}
		return listStr;
	}

	
	/**
	 *  正则过滤
	 *  @param regex 正则表达式
	 *  @param str 字符串
	 * 
	 * */
	public static List<String> getStrs(String regex,String str) {
		List<String> list = new ArrayList<String>();
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(regex);
		// 现在创建 matcher 对象
		Matcher m = r.matcher(str);
		while (m.find()) {
			list.add(m.group(0));
			// System.out.println(m.group(0));
		}
		return list;
	}
}
