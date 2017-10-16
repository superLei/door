package utils;


/**
 *   收银门店的接口url
 * 
 * */
public class UrlData {
	
	
	
	public String ip = "127.0.0.1";
	public String userId = "name";
	public String pwdId = "pwd";
	public String B_PRECHECKlOCK_ORDER = "0";//预结锁单参数，0为否，1为是;
	public String dids = "123";//沽清的菜品单号
	
	
	// 登录 post
	public String urlLogin() {
		return "http://"+ip+"/index.php?do=check";
	}
	
	// 预结锁单 post
	public String prechecklock() {
		return "http://"+ip+"/order/apps/config/sysconfig.php?do=save&tab=company";
	}
	
	// 获取沽清 post
	public String soldout() {
		return "http://"+ip+"/order/apps/res/soldout.php?do=listdishes&leftamount=soldout";
	}
	
	// 取消沽清 get
	public String cancelsoldout() {
		return "http://"+ip+"/order/apps/res/soldout.php?do=cancelsoldout&dids="+dids+"&_dc=123456789";
	}
	
	// 退菜 post
	public String rejectall() {
		return "http://"+ip+"/order/apps/order/current.php?do=rejectall";
	}
	
	// 预结 post 
	public String precheck() {
		return "http://" + ip+ "/order/apps/order/current.php?do=precheckNoDiscount";
	}
	
	// 订单ID get
	public String getoids() {
		return "http://" + ip + "/order/apps/order/current.php?do=initCurrent";
	}
	
	// 结账 
	public String checkout() {
		return "http://" + ip + "/order/apps/order/current.php?do=singleCheckout";
	}
	
	// 添加打印机
	public String printer() {
		return "http://" + ip + "/order/apps/res/printer.php?do=appendprinter";
	}
	
	// 添加工作台
	public String workStation() {
		return "http://" + ip + "/order/apps/res/workstation.php?do=appendstation";
	}
	
	// 所有的系统配置
	public String deskUpdate() {
		return  "http://" + ip + "/order/apps/config/sysconfig.php?do=save&tab=company";
	}
	
	
	
	

	
	

}
