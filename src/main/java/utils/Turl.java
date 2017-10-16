package utils;


/**
 *  总部接口
 * 
 * */
public class Turl {
	
	public String ip = "127.0.0.1";
	public String userId = "name";
	public String pwdId = "pwd";
	public String user = "1";
	public String pwd = "1";
	
	//添加桌台 post
	public String addDesk() {
		return "http://"+ip+"/chainsales/operation/table/addTable";
	}

	//登录url post
	public String urlLogin() {
		return "http://"+ip+"/?do=check";
	}
	
	

}
