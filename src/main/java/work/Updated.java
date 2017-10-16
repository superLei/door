package work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



/**
 *  对门店做数据库升级
 *  @author SL
 *  @date 2016-10-24
 * 
 * */
public class Updated {
	
	public static CloseableHttpClient httpclient= HttpClients.createDefault();
	public static CloseableHttpResponse response = null;
	
	public static final String IP = "127.0.0.1";
	public static final String user = "admin";
	public static final String pwd = "admin";
	public static final String urlLogin = "http://"+IP+"/tool/?do=check"; //登录
	public static final String dataUpUrl = "http://"+IP+"/tool/deploy/deploy.php";//数据库升级
	public static final String versionUrl = "http://"+IP+"/tool/welcome.php";//获取版本号
	
	
//	public static void main(String[] args) {
//		update();
//	}
	
	
	public static void update() {
		HttpPost httppost = new HttpPost(urlLogin);
		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("name", user));
		formparams.add(new BasicNameValuePair("pwd", pwd));
		UrlEncodedFormEntity uefEntity;
		int resultCode = 0;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			resultCode = response.getStatusLine().getStatusCode();
			System.out.println("--------登录"+IP+"成功---------");
			//get数据库升级
			System.out.println("----------数据库升级中...----------");
			if (resultCode == 200 && get(dataUpUrl) ==200 && get(versionUrl) == 200) {
				System.out.println("--------------完成升级!--------------");
			} else {
				System.out.println("--------------升级失败!--------------");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /** 
     * 发送 get请求 
     */  
    public static int get(String url) {  
    	int result = 0;
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);   
            // 执行get请求.    
           response = httpclient.execute(httpget);  
            try {  
				System.out.println("------------------------------------");
				// 打印响应状态
				result = response.getStatusLine().getStatusCode();
				System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
                System.out.println("------------------------------------");  
            } finally {  
                response.close();  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
        return result;
    } 
    

}
