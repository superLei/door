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
 *  做接口测试
 *  @author SL
 *  @date 2016-10-28
 * 
 * */

public class Upgrade {
	
	public  CloseableHttpClient httpclient= HttpClients.createDefault();
	public  CloseableHttpResponse response = null;
	
	
	/**
	 *  登录功能
	 *  @param urlLogin post方法的url
	 *  @param userP user参数
	 *  @param pwdP password 参数
	 *  @return 0 说明登录失败，200说明登录成功
	 * 
	 * */
	public int login (String urlLogin,String userP,String pwdP,String user,String pwd){
		HttpPost httppost = new HttpPost(urlLogin);
		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair(userP, user));
		formparams.add(new BasicNameValuePair(pwdP, pwd));
		UrlEncodedFormEntity uefEntity;
		int resultCode = 0;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			resultCode = response.getStatusLine().getStatusCode();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}
	
	 /** 
     * 发送 get请求 
     * @param url get方法的url
     * @return 0 说明登录失败，200说明登录成功
     */  
    public int get(String url) {  
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
