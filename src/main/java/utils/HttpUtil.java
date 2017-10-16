package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;



public class HttpUtil {
	
	
	public CloseableHttpClient httpclient= HttpClients.createDefault();
	public CloseableHttpResponse response = null;
	public CookieStore cookieStore = null;
	public Map<String,String> cookieMap = new HashMap<String, String>(64);
	
	
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
		 // 设置请求和传输超时时间  
        RequestConfig requestConfig = RequestConfig.custom()  
                .setSocketTimeout(30000)  
                .setConnectTimeout(30000).build();  
        httppost.setConfig(requestConfig);
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
			System.out.println("The page's status is "+ resultCode);
			String str = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(str);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}
	
	/**
	 *  打印响应信息
	 *  @param HttpResponse HTTP响应
	 * 
	 * */
	public void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
//        System.out.println("headers:");
//        HeaderIterator iterator = httpResponse.headerIterator();
//        while (iterator.hasNext()) {
//            System.out.println("\t" + iterator.next());
//        }
        // 判断响应实体是否为空
        if (entity != null) {
            String responseString = EntityUtils.toString(entity,"UTF-8");
//            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }
	
	
	//从响应信息中获取cookie
    public String setCookie(HttpResponse httpResponse)
    {
        System.out.println("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length==0)
        {
            System.out.println("----there are no cookies");
            return null;
        }
        String cookie = "";
        for (int i = 0; i < headers.length; i++) {
            cookie += headers[i].getValue();
            if(i != headers.length-1)
            {
                cookie += ";";
            }
        }
 
        String cookies[] = cookie.split(";");
        for (String c : cookies)
        {
            c = c.trim();
            if(cookieMap.containsKey(c.split("=")[0]))
            {
                cookieMap.remove(c.split("=")[0]);
            }
            cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "":(c.split("=").length ==2?c.split("=")[1]:c.split("=",2)[1]));
        }
        System.out.println("----setCookieStore success");
        String cookiesTmp = "";
        for (String key :cookieMap.keySet())
        {
            cookiesTmp +=key+"="+cookieMap.get(key)+";";
        }
 
        return cookiesTmp.substring(0,cookiesTmp.length()-2);
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
    

	 /** 
    * 发送 get请求 
    * @param url get方法的url
    * @return 0 说明登录失败，200说明登录成功
    */  
   public String get2(String url) {  
	   String result = "";
       try {  
           // 创建httpget.    
           HttpGet httpget = new HttpGet(url);   
           // 执行get请求.    
          response = httpclient.execute(httpget);  
           try {  
				System.out.println("------------------------------------");
				// 打印响应状态
				System.out.println("response code: "+response.getStatusLine().getStatusCode());
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
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
    
 
    /**
     *  发送Json格式的post请求
     *  @param url 请求的url
     *  @return 结果返回json
     * 
     * */
    @SuppressWarnings("deprecation")
	public JSONObject postJson(String url) {
    	 HttpPost post = new HttpPost(url);  
         JSONObject json = null; 
         try {    
        	 response = httpclient.execute(post);  
             if(response.getStatusLine().getStatusCode() == 200){  
                 HttpEntity entity = response.getEntity();  
//                 String charset = EntityUtils.toString(entity, "UTF-8");  
                 json = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(),HTTP.UTF_8)));  
             }  
         } catch (Exception e) {  
             throw new RuntimeException(e);  
         } finally {
        	 post.releaseConnection();
         }  
         
         return json;  
         
    }
    
    
    /**
     *  发送post请求
     *  @param url 
     * */
    public String post(String url) {
    	 HttpPost post = new HttpPost(url);
    	 String repoStr = "";
    	 try {    
        	 response = httpclient.execute(post);  
             if(response.getStatusLine().getStatusCode() == 200){  
                 HttpEntity entity = response.getEntity();  
//                 String charset = EntityUtils.toString(entity, "UTF-8");  
                 repoStr =  EntityUtils.toString(entity, "UTF-8"); 
             }  
         } catch (Exception e) {  
             throw new RuntimeException(e);  
         }  finally {
        	 post.releaseConnection();
         }  
         return repoStr;  
    }
    
    
    /**
     *  发送Json格式的post请求
     *  @param url 请求的url
     *  @param json 请求的json
     *  @return 结果返回json
     * 
     * */
    public JSONObject post(String url,JSONObject json){  
 
        HttpPost post = new HttpPost(url);  
        JSONObject response = null;  
        try {  
            StringEntity s = new StringEntity(json.toString());  
            s.setContentEncoding("UTF-8");  
            s.setContentType("application/json");  
            post.setEntity(s);  
              
            HttpResponse res = httpclient.execute(post);  
            if(res.getStatusLine().getStatusCode() == 200){  
                HttpEntity entity = res.getEntity();  
//                String charset = EntityUtils.toString(entity, "UTF-8");  
                response = new JSONObject(new JSONTokener(new InputStreamReader(entity.getContent(),"UTF-8")));  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
        return response;  
    } 

    
    /**
     *  发送Form形式的post请求
     * 
     * */
    public Map<String,String> post(Map<String,List<NameValuePair>> urlAndNamePairList) {
    	int result = 0;
    	//返回每个URL对应的响应信息  
        Map<String,String> map = new HashMap<String,String>();  
    	UrlEncodedFormEntity uefEntity;
    	HttpPost httppost;
    	String str = "";//保存返回数据
		try {
			for (Map.Entry<String, List<NameValuePair>> entry : urlAndNamePairList
					.entrySet()) {
				String url = entry.getKey();
				List<NameValuePair> params = urlAndNamePairList.get(url);
				httppost = new HttpPost(url);
				uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
				httppost.setEntity(uefEntity);
				response = httpclient.execute(httppost);
				result = response.getStatusLine().getStatusCode();
				System.out.println("页面状态值: "+result);
				str = EntityUtils.toString(response.getEntity(), "UTF-8");
				map.put(url, str);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    	return map;
    }
    
    /**
     *  Unicode转换中文
     * 
     * */
    public  String unicodeToChinese(String str){
		String[] strings = str.split(";");
		StringBuffer aStr = new StringBuffer();
		for(int i=0;i<strings.length;i++){
			String s = strings[i].replace("&#", "");
			aStr.append((char)Integer.parseInt(s));
		}
		return aStr.toString();
	}

    
    /**
     *  保存会话cookie
     *  @param httpResponse 
     *  @param domainIp 域名
     * 
     * */
    public void setCookieStore(HttpResponse httpResponse, String domainIp) {
		System.out.println("----setCookieStore");
		cookieStore = new BasicCookieStore();
		// JSESSIONID
		String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
		String JSESSIONID = setCookie.substring("PHPSESSID=".length(),
				setCookie.indexOf(";"));
		System.out.println("PHPSESSID:" + JSESSIONID);
		// Cookie
		BasicClientCookie cookie = new BasicClientCookie("PHPSESSID",
				JSESSIONID);
		cookie.setVersion(0);
		cookie.setDomain(domainIp);
		cookie.setPath("/CwlProClient");
		// cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
		// cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
		// cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
		// cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
		cookieStore.addCookie(cookie);
	}
    
    /**
     *  保存会话cookie
     *  @param httpResponse 
     * 
     * */
    public void setCookieStore(HttpResponse httpResponse) {
		System.out.println("----setCookieStore");
		cookieStore = new BasicCookieStore();
		// JSESSIONID
		String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
		String JSESSIONID = setCookie.substring("PHPSESSID=".length(),
				setCookie.indexOf(";"));
		System.out.println("PHPSESSID:" + JSESSIONID);
		// Cookie
		BasicClientCookie cookie = new BasicClientCookie("PHPSESSID",
				JSESSIONID);
		cookie.setVersion(0);
		cookie.setPath("/CwlProClient");
		cookieStore.addCookie(cookie);
	}
  
    
    /**
	 *  获取正则后的字符串
	 * @param regex 正则表达式
	 * @param str 字符串
	 * 
	 * */
	public List<String> getStrs(String regex, String str) {
		List<String> list = new ArrayList<String>();
		if (str != null) {
			// 
			Pattern r = Pattern.compile(regex);
			//
			Matcher m = r.matcher(str);
			while (m.find()) {
				list.add(m.group(0));
//				System.out.println("Get str is: " + m.group(0));
			}
		}
		else {
		    System.out.println("The string is null");
		}
		return list;
	}
    
	/**
	 *  获取本机ip
	 * 
	 * */
	 public String getLocalHostIP() { 
         String ip; 
         try { 
              /**返回本地主机。*/ 
              InetAddress addr = InetAddress.getLocalHost(); 
              /**返回 IP 地址字符串（以文本表现形式）*/ 
              ip = addr.getHostAddress();  
         } catch(Exception ex) { 
             ip = ""; 
         } 
           
         return ip; 
    } 

	 /** 
      * 或者主机名： 
      * @return 
      */ 
     public String getLocalHostName() { 
          String hostName; 
          try { 
               /**返回本地主机。*/ 
               InetAddress addr = InetAddress.getLocalHost(); 
               /**获取此 IP 地址的主机名。*/ 
               hostName = addr.getHostName(); 
          }catch(Exception ex){ 
              hostName = ""; 
          } 
            
          return hostName; 
     } 
     
     /** 
      * 获得本地所有的IP地址 
      * @return 
      */ 
     public String[] getAllLocalHostIP() { 
            
         String[] ret = null; 
          try { 
               /**获得主机名*/ 
               String hostName = getLocalHostName(); 
               if(hostName.length()>0) { 
                   /**在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。*/ 
                    InetAddress[] addrs = InetAddress.getAllByName(hostName); 
                    if(addrs.length>0) { 
                         ret = new String[addrs.length]; 
                         for(int i=0 ; i< addrs.length ; i++) { 
                             /**.getHostAddress()   返回 IP 地址字符串（以文本表现形式）。*/ 
                             ret[i] = addrs[i].getHostAddress(); 
                         } 
                    } 
               } 
                 
          }catch(Exception ex) { 
              ret = null; 
          } 
            
          return ret; 
     } 
     
     private  boolean isSpecialIp(String ip) {
    	 if (ip.contains(":")) return true;
         if (ip.startsWith("127.")) return true;
         if (ip.startsWith("172.")) return true;
         if (ip.startsWith("169.254.")) return true;
         if (ip.equals("255.255.255.255")) return true;
         return false;
     }
     
     /**
 	 *  获取本机真正ip
 	 * 
 	 * */
     public String getLocalIp() {
    	 String localIp = "";
    	 String[] ips = this.getAllLocalHostIP();
    	 for (int i = 0; i < ips.length; i++) {
			if(isSpecialIp(ips[i]) == false) {
				localIp = ips[i];
			}
		}
    	 return localIp;
     }
     

 	/**
 	 *  读取json文件
 	 *  @param path 文件路径
 	 * 
 	 * */
 	public  String ReadFile(String path) {
// 		InputStream is = getClass().getResourceAsStream(path);
         File file = new File(path);
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
//         System.out.println();
         return sb.toString();
         
     }
     
}
