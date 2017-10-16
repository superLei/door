package update.door;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.HttpUtil;
import utils.JsonUtil;
import utils.Turl;
import utils.UrlData;

public class Test1 {
	
	HttpUtil htu = null;
	UrlData urls = new UrlData();
	Turl turl = new Turl();
	public List<String> listStr = null;
	public String user = "1";
	public String pwd = "acewill";

	public String pattern = "\"did\":\"(\\d+)\"";
	public String pattern2 = "\"total\":\"(\\d+)\"";
	
	
	@BeforeTest
	public void before() {
		htu = new HttpUtil();
	}
	
	@Test(enabled = false) //取消沽清菜品
	public void aTest() {
//		urls.ip = "192.168.1.238";
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
		//更改锁单为否
//		htu.post(UrlData.prechecklock);
	}
	

	@Test (enabled = false)
	public void weixin() throws InterruptedException {
		String url = "http://huiyuan.canxingjian.com/admin/test?shopkey=741852&money=";
		htu = new HttpUtil();
		int money = 0;
		int min = 100;
		int max = 150;
		int time = 0;
		for (int i = 0; i < 100; i++) {
			System.out.println("------------------------------------------------------------");
			money = new Random().nextInt(200);
			htu.get(url + money);
			
			System.out.println("The money is: "+money);
			System.out.println("The number is:"+ i);
			System.out.println("------------------------------------------------------------");
			
			time = new Random().nextInt(max - min + 1) + min; 
			System.out.println("The time is: "+ time);
			Thread.sleep(time);
		}
	}
	
	/**
	 *  全单退菜
	 * 
	 * */
	@Test (enabled = true) 
	public void reject() {
		String pattern1 = "\"oid\":\"(\\d+)\"";
		List<String> listOid = null;
		CloseableHttpResponse response = null;
		urls.ip = "192.168.1.79";
		HttpPost httppost = new HttpPost(urls.urlLogin());
		// 登录
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("name", user));
		formparams.add(new BasicNameValuePair("pwd", pwd));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			response = htu.httpclient.execute(httppost);
			System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
			htu.setCookieStore(response, urls.ip);
			//获取定单ID
			listOid = htu.getStrs(pattern1, htu.get2(urls.getoids()));
			for (int i = 0; i < listOid.size(); i++) {
				System.out.println(listOid.get(i));
			}

			//全单退菜
			System.out.println("The oid's count is: " + listOid.size());
			String oid = "";
			Map<String,List<NameValuePair>> map = null;
			List<NameValuePair> list = null;
			Map<String,String> map2 = null;
			for (int i = 0; i < listOid.size(); i++) {
				System.out.println("The index is: " + i);
				System.out.println("------------------------------");
				oid = listOid.get(i).replaceAll("[oid:\"]", "");
				System.out.println("The oid is : "+ oid);
				map = new HashMap<String,List<NameValuePair>>();
				list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("data","{\"oid\":\""+oid+"\",\"rsid\":\"-1\",\"authcode\":\"\"}"));
				map.put(urls.rejectall(), list);
				map2 = htu.post(map);
//				System.out.println(urls.rejectall());
				System.out.println(map2.get(urls.rejectall()));
				
			}
			System.out.println("*****共【"+listOid.size()+"】单*****");
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test (enabled = false)
	public void addDesk() {
	 
		CloseableHttpClient httpclient= HttpClients.createDefault();
		CloseableHttpResponse response = null;
		htu = new HttpUtil();
		turl.ip = "192.168.1.161";
		String sid = "299";//桌台区域id
		String slsid = "100";//门店id
		String tablename = "A10";
		String sno = "34";//桌台速记码
		 
		//登录总部
		HttpPost httppost = new HttpPost(turl.urlLogin());
        httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		// 创建参数队列
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair(urls.userId, user));
		formparams.add(new BasicNameValuePair( urls.pwdId, pwd));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			response = httpclient.execute(httppost);
			httppost.releaseConnection();
			
			System.out.println("----------------------------------------------------");
			//登录所需的post
			httppost = new HttpPost("http://192.168.1.161/chainsales/");
			response = httpclient.execute(httppost);
			htu.printResponse(response);
			httppost.releaseConnection();
			
			System.out.println("----------------------------------------------------");
			//请求role
			httppost = new HttpPost("http://192.168.1.161/chainsales/head/slsrole/role");
			response = httpclient.execute(httppost);
			htu.printResponse(response);
			httppost.releaseConnection();
			
			System.out.println("----------------------------------------------------");
			//添加桌台
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("sid", sid));
			list.add(new BasicNameValuePair("slsid", slsid));
			list.add(new BasicNameValuePair("tablename", tablename));
			list.add(new BasicNameValuePair("maxseat", "6"));
			list.add(new BasicNameValuePair("seq", "7"));
			list.add(new BasicNameValuePair("sno", sno));
			list.add(new BasicNameValuePair("type", "1"));
			list.add(new BasicNameValuePair("dfservicerate", "0.00"));
			list.add(new BasicNameValuePair("minimum", ""));
			list.add(new BasicNameValuePair("status", "1"));
			list.add(new BasicNameValuePair("bsetmember", "0"));
			try {
				UrlEncodedFormEntity uentity = new UrlEncodedFormEntity(list, "UTF-8");
				httppost = new HttpPost(turl.addDesk());
				httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				System.out.println(turl.addDesk());
				httppost.setEntity(uentity);
				response = httpclient.execute(httppost);
				htu.printResponse(response);
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				htu.httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test(enabled = false)
	public void addPrinter()  {
		//门店登录
		
		String printername = "Robot666";
		String ip = "192.168.1.166";
		String ip2 = htu.getLocalHostIP();
		urls.ip = "192.168.1.101";
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
			formparams2.add(new BasicNameValuePair("ip", ip));
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
			formparams3.add(new BasicNameValuePair("station", "Robot007"));
			formparams3.add(new BasicNameValuePair("stationtype", "0"));
			formparams3.add(new BasicNameValuePair("ip", ip2));
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
	
	@Test(enabled = false)
	public void deskUpdate() throws ClientProtocolException, IOException {
		// 登录门店
		urls.ip = "192.168.1.101";
		HttpPost httppost0 = new HttpPost(urls.urlLogin());
		List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
		formparams.add(new BasicNameValuePair("name", user));
		formparams.add(new BasicNameValuePair("pwd", pwd));
		UrlEncodedFormEntity uefEntity;
		uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
		httppost0.setEntity(uefEntity);
		CloseableHttpResponse response0 = htu.httpclient.execute(httppost0);
		try {
			htu.printResponse(response0);
		} finally {
			response0.close();
		}
		
		
		HttpPost httppost = new HttpPost(urls.deskUpdate());
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		httppost.setHeader("Content-Type","application/x-www-form-urlencoded");
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("B_TABLE_STATUS_WITH_GUEST_IN", "0"));
		params.add(new BasicNameValuePair("B_TABLE_STATUS_WITH_DIRTY", "0"));
		params.add(new BasicNameValuePair("B_REPORT_STAT", "0"));
		params.add(new BasicNameValuePair("B_ORDER_ITEM_NEED_CHECK", "0"));
		params.add(new BasicNameValuePair("B_MEMO_APPENDTOGIFTITEM", "0"));
		params.add(new BasicNameValuePair("B_SHIFT_DAILY", "0"));
		params.add(new BasicNameValuePair("B_SHIFT_INPUT", "0"));
		params.add(new BasicNameValuePair("B_ORDER_DEFAULT_WAITING", "0"));
		params.add(new BasicNameValuePair("B_ORDER_PUSHER", "1"));
		params.add(new BasicNameValuePair("B_TABLE_ALL_REJECT_CLEAR", "1"));
		params.add(new BasicNameValuePair("B_GENERATE_SERIAL_NUMBER", "0"));
		params.add(new BasicNameValuePair("B_OFF_PLANTFORM_DISH", "1"));
		params.add(new BasicNameValuePair("GENERATE_SERIAL_NUMBER_PREFIXTYPE", "字符"));
		params.add(new BasicNameValuePair("GENERATE_SERIAL_NUMBER_PREFIX_DATE", ""));
		params.add(new BasicNameValuePair("GENERATE_SERIAL_NUMBER_PREFIX_CHAR", "2015/01/01"));
		params.add(new BasicNameValuePair("B_ORDER_KITCHENS", ""));
		params.add(new BasicNameValuePair("PRINT_BOOKITEM_NUMBER", "1"));
		params.add(new BasicNameValuePair("BOOK_ORDER_NEW_TIME", "2"));
		params.add(new BasicNameValuePair("B_BOOK_SKIP_CHECKAMOUNT", "0"));
		params.add(new BasicNameValuePair("B_BOOK_PRINT_BILLS", "1"));
		params.add(new BasicNameValuePair("B_BOOK_ORDER_PRINT_BILLS", "1"));
		params.add(new BasicNameValuePair("B_BOOK_PRINT_PASSENGER_BILL", "0"));
		params.add(new BasicNameValuePair("B_BOOK_DELAY_TIME", "30"));
		params.add(new BasicNameValuePair("PRECHECK_ACCURACY", "TWODECIMAL"));
		params.add(new BasicNameValuePair("WAI_MAI_IS_PAY_ACCURACY", "TWODECIMAL"));
		params.add(new BasicNameValuePair("B_GIFT_ITEM_CALC_TOTAL_SERVICE_FEE", "0"));
		params.add(new BasicNameValuePair("B_GIFT_ITEM_CALC_COST_SERVICE_FEE", "0"));
		params.add(new BasicNameValuePair("DEFAULT_SERVICE_RATE", "0"));
		params.add(new BasicNameValuePair("DEFAULT_SERVICE_RATE_BYMINUTE", "0"));
		params.add(new BasicNameValuePair("SERVICE_RATE_MINUTE", "0"));
		params.add(new BasicNameValuePair("B_SERVICE_DISCOUNT", "0"));
		params.add(new BasicNameValuePair("DF_WIPE_ZERO", "0"));
		params.add(new BasicNameValuePair("SERVICE_CHARGE_ACCURACY", "ROUND"));
		params.add(new BasicNameValuePair("B_PRINT_IF_NODISCOUNT", "1"));
		params.add(new BasicNameValuePair("B_PRINT_WEIXIN_QRCODE", "0"));
		params.add(new BasicNameValuePair("B_PRINT_BAIDU_QRCODE", "0"));
		params.add(new BasicNameValuePair("B_PRINT_ZHIFUBAO_QRCODE", "0"));
		params.add(new BasicNameValuePair("B_PRINT_SHANHUI_QRCODE", "0"));
		params.add(new BasicNameValuePair("B_MARKET_DATE", "0"));
		params.add(new BasicNameValuePair("B_MEMBER_PRICE", "0"));
		params.add(new BasicNameValuePair("EMENU_MARKETING_INFORMATION", ""));
		params.add(new BasicNameValuePair("B_MULTI_TABLE", "0"));
		params.add(new BasicNameValuePair("B_MULTI_ORDER", "0"));
		params.add(new BasicNameValuePair("B_WEIBO_LINK", "0"));
		params.add(new BasicNameValuePair("EMENU_PHONE_PROMOTIONS", "0"));
		params.add(new BasicNameValuePair("DISHKIND_NUMzh_cn", "5"));
		params.add(new BasicNameValuePair("EMENU_TIPzh_cn", ""));
		params.add(new BasicNameValuePair("EMENU_SERVER_TIPzh_cn", ""));
		params.add(new BasicNameValuePair("SPECIAL_PTIDS[]", "-1"));
		params.add(new BasicNameValuePair("SPECIAL_PTIDS[]", "-16"));
		params.add(new BasicNameValuePair("SPECIAL_PTIDS[]", "-23"));
		params.add(new BasicNameValuePair("SPECIAL_PTIDS[]", "463"));
		
		params.add(new BasicNameValuePair("SPECIAL_PTIDS[]", "-25"));
		params.add(new BasicNameValuePair("SPECIAL_RATE", "30"));
		params.add(new BasicNameValuePair("C_DOWNLOAD_TABLE", "0"));
		params.add(new BasicNameValuePair("C_SHIFT_LOGIN", "1"));
		params.add(new BasicNameValuePair("B_TABLE_IS_ORDER", "1")); // 这个是桌台存在未结账订单是否允许更新数据:是
		params.add(new BasicNameValuePair("B_AUTO_CLEAR_TABLE", "0"));
		params.add(new BasicNameValuePair("B_DEFAULT_INVOICE", "0"));
		params.add(new BasicNameValuePair("B_DEFAULT_GUEST_LEAVE", "1"));
		params.add(new BasicNameValuePair("B_CASHDRAW_OPEN", "0"));
		params.add(new BasicNameValuePair("B_MUTI_PAY", "1"));
		params.add(new BasicNameValuePair("B_PRINT_COMMENT_QRCODE", "0"));
		params.add(new BasicNameValuePair("B_PRINT_WEIXIN_code", "0"));
		params.add(new BasicNameValuePair("ALIPAY_GUESTCODE", "0"));
		params.add(new BasicNameValuePair("B_CURRENT_ALIPAY_GUESTCODE", "1"));
		params.add(new BasicNameValuePair("B_WEIXIN_REFUND", "1"));
		params.add(new BasicNameValuePair("B_IS_OPEN_DIRECT_CHECKOUT", "0"));
		params.add(new BasicNameValuePair("A_CRM_CHAIN", "1"));
		params.add(new BasicNameValuePair("W_CRM_CHAIN", "0"));
		params.add(new BasicNameValuePair("Hui_CRM_CHAIN", "1"));
		params.add(new BasicNameValuePair("MAX_CASH_AMOUNT", "0"));
		params.add(new BasicNameValuePair("B_ARE_CHAIN_ACCOUNT", "0"));
		params.add(new BasicNameValuePair("CHECKOUT_PRINTA4", "0"));
		params.add(new BasicNameValuePair("B_DCB_ORDERMEMO_TO_DISH", "1"));
		params.add(new BasicNameValuePair("DCB_VER", "4.7"));
		
		params.add(new BasicNameValuePair("DCB_DATA_PATH", "D:/xampp/acewill/app/../sync/boli/txt/"));
		params.add(new BasicNameValuePair("DB_BACKUP_PATH", "E:\\backup\\"));
		params.add(new BasicNameValuePair("B_DATA_MULTI_LANG", "0"));
		params.add(new BasicNameValuePair("CURRENTICONSIZE", "small"));
		params.add(new BasicNameValuePair("REFSH_TIME", "3"));
		params.add(new BasicNameValuePair("TAKE_REFSH_TIME", "10"));
		params.add(new BasicNameValuePair("PAYID_TAKE_ORDER", "0"));
		params.add(new BasicNameValuePair("B_Select_Waiter", "0"));
		params.add(new BasicNameValuePair("B_Select_Cooker", "0"));
		params.add(new BasicNameValuePair("Setmeal_Pattern", "0"));
		params.add(new BasicNameValuePair("MORETABLE_AMOUNT", "0"));
		params.add(new BasicNameValuePair("BUFFET_PRICE", "0"));
		params.add(new BasicNameValuePair("B_SAMES_DISHES", "1"));
		params.add(new BasicNameValuePair("IF_COMPLEX_FONT", "0"));
		params.add(new BasicNameValuePair("B_PRECHECKlOCK_ORDER", "0"));
		params.add(new BasicNameValuePair("B_SELECT_TABLE", "0"));
		params.add(new BasicNameValuePair("B_USEDISCOUNT_RATE", "1"));
		params.add(new BasicNameValuePair("B_CHECKOUT_PEOPLE", "1"));
		params.add(new BasicNameValuePair("LOCK_SYSTEM_TIME", "0"));
		params.add(new BasicNameValuePair("B_NOPRE_CHECKOUT", "1"));
		params.add(new BasicNameValuePair("SHOW_ALL_TABLES", "1"));
		params.add(new BasicNameValuePair("SHOW_FAST_CODES", "1"));
		
		params.add(new BasicNameValuePair("SET_MEAL_REPLACE", "0"));
		params.add(new BasicNameValuePair("B_CHOOSE_KEYBOARD", "0"));
		params.add(new BasicNameValuePair("B_DIRECT_PRINT", "0"));
		params.add(new BasicNameValuePair("TOUCH_SHOW_LINE", ""));
		params.add(new BasicNameValuePair("TOUCH_SHOW_NUM", ""));
		params.add(new BasicNameValuePair("B_ELECTRONIC_SCALE_URL", ""));
		params.add(new BasicNameValuePair("ARE_MEMBER_INTERFACE", "微生活会员"));
		params.add(new BasicNameValuePair("SMS_USING_PLATFORM", "0"));
		params.add(new BasicNameValuePair("SELECT_SMS_PLATFORM", "阳洋短信平台系统"));
		params.add(new BasicNameValuePair("COMPANYID", ""));
		params.add(new BasicNameValuePair("USERNAME", ""));
		params.add(new BasicNameValuePair("PASSWORD", ""));
		params.add(new BasicNameValuePair("ICBC_MODEL", "0"));
		params.add(new BasicNameValuePair("B_ZHENGZHOU_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_YOUTANG_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_WANGFUJING_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_YHYSHXINTIANDI_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_XINGKONGGUANGCHANG_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_MEILUOCHENG_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_JIANGTAIWUER_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_XINAO_MARKET_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_CHANGZHOU_UPLOAD", "0"));
		params.add(new BasicNameValuePair("B_CURRENT_MUTUAL", "0"));
		params.add(new BasicNameValuePair("MUTUAL_DELAY_TIME", "0"));
		params.add(new BasicNameValuePair("SELF_HELP_PAYTYPE[]", "1"));
		params.add(new BasicNameValuePair("SELF_HELP_PAYTYPE[]", "3"));
		params.add(new BasicNameValuePair("NODEJS_URL", ""));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpResponse response = htu.httpclient.execute(httppost);
			htu.printResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	
}
