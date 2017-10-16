package work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.HttpUtil;
import utils.JsonUtil;
import utils.UrlData;


/**
 *  初始化门店
 * 
 * 
 * */
public class InitializationShop {
	static HttpUtil htu = new HttpUtil();
	static UrlData urls = new UrlData();
	public static List<String> listStr = null;
	public static String user = "1";
	public static String pwd = "acewill";
	public static String shopIp = "192.168.1.79";
	public static String pattern = "\"did\":\"(\\d+)\"";
	public static String pattern2 = "\"total\":\"(\\d+)\"";
	
	
	public static void main(String[] args) {
		String JsonContext = ReadFile("/conf/config.json");
		JSONArray jsonArray = new JSONArray(JsonContext);
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			user = (String) jsonObject.get("userName");
			pwd = (String) jsonObject.get("passWord");
			shopIp = (String) jsonObject.get("shopIp");
		}
		addPrinter(shopIp);
		cancelSoldOut(shopIp);
	}
	
	
	/**
	 * 取消沽清菜品
	 * @param ip 门店ip,默认为本机
	 * 
	 * */
	public static void cancelSoldOut(String... ip) {
		if(ip != null) {
			urls.ip = ip[0];
		}
		System.out.println(urls.ip);
		// 登录
		htu.login(urls.urlLogin(), urls.userId, urls.pwdId, user, pwd);
		System.out.println(urls.urlLogin());
		// 获取沽清菜品的总数
		String re = htu.post(urls.soldout());
		List<String> didsCount = JsonUtil.getStrs(pattern2,re);
		String soldout = didsCount.get(0).replaceAll("[total:\"]", "");
		System.out.println("沽清菜品数量："+soldout);
		if (soldout.equals("0")) {
			System.out.println("无沽清菜品");
			return;
		} else {
			// 获取沽清菜品的菜品ID
			String tmp = htu.post(urls.soldout());
			listStr = JsonUtil.getStrs(pattern, tmp);
			for (int i = 0; i < listStr.size(); i++) {
				urls.dids =  listStr.get(i).replaceAll("[did:\"]", "");
				System.out.println("取消沽清: "+urls.dids);
				htu.get(urls.cancelsoldout());
			}
			System.out.println("完成取消沽清");
		}
	}
	
	/**
	 *  添加打印机和工作台
	 *  @param ip 门店ip,默认为本机
	 * 
	 * */
	public static void addPrinter(String... ip)  {

		String printername = "Robot"+new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
		String printerIp = "192.168.1.166";
		String hostIp = htu.getLocalIp();
		if(ip != null) {
			urls.ip = ip[0];
		}
		try {
			// 登录门店
			HttpPost httppost = new HttpPost(urls.urlLogin());
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("name", user));
			formparams.add(new BasicNameValuePair("pwd", pwd));
			UrlEncodedFormEntity uefEntity;
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			CloseableHttpResponse response = htu.httpclient.execute(httppost);
			try {
				htu.printResponse(response);
			} finally {
                response.close();
            }
			
			// 发送post,添加打印机
			HttpPost httppost2 = new HttpPost(urls.printer());
			List<BasicNameValuePair> formparams2 = new ArrayList<BasicNameValuePair>();
			formparams2.add(new BasicNameValuePair("printername", printername));
			formparams2.add(new BasicNameValuePair("brand", "1"));
			formparams2.add(new BasicNameValuePair("bpos", "1"));
			formparams2.add(new BasicNameValuePair("bdoubleheight", "0"));
			formparams2.add(new BasicNameValuePair("cutlength", "9000"));
			formparams2.add(new BasicNameValuePair("point", "576"));
			formparams2.add(new BasicNameValuePair("ip", printerIp));
			formparams2.add(new BasicNameValuePair("memo", ""));
			formparams2.add(new BasicNameValuePair("baud_rate", "9600"));
			formparams2.add(new BasicNameValuePair("iface_type", "2"));
			formparams2.add(new BasicNameValuePair("printer_service_id", ""));
			httppost2.setEntity(new UrlEncodedFormEntity(formparams2, "UTF-8"));
			CloseableHttpResponse response2 = htu.httpclient.execute(httppost2);
			try {
				htu.printResponse(response2);
			} finally {
                response2.close();
            }
			
			// 发送post，添加工作台
			HttpPost httppost3 = new HttpPost(urls.workStation());
			List<BasicNameValuePair> formparams3 = new ArrayList<BasicNameValuePair>();
			formparams3.add(new BasicNameValuePair("station", printername));
			formparams3.add(new BasicNameValuePair("stationtype", "0"));
			formparams3.add(new BasicNameValuePair("ip", hostIp));
			formparams3.add(new BasicNameValuePair("pid", "2"));
			formparams3.add(new BasicNameValuePair("memo", ""));
			httppost3.setEntity(new UrlEncodedFormEntity(formparams3, "UTF-8"));
			CloseableHttpResponse response3 = htu.httpclient.execute(httppost3);
			try {
				htu.printResponse(response3);

			} finally {
                response3.close();
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
	}

	/**
	 *  读取json文件
	 *  @param path 文件路径
	 * 
	 * */
	public static String ReadFile(String path) {
		String dirPath = System.getProperty("user.dir") + path;
        File file = new File(dirPath);
        if(!file.exists()||file.isDirectory()) {
            try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
        StringBuffer sb = new StringBuffer();
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferReader.readLine()) != null) {
                sb.append(lineTxt);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println();
        return sb.toString();
        
    }
}
