package work;

public class UpTotal {
	
	public static Datas data = new Datas();
	public static String IP = data.getIP();
	public static String urlLogin = "http://"+IP+"/chainsales/tool?do=check"; //登录
	public static String dataUpUrl = "http://"+IP+"/chainsales/admin/deploy";//数据库升级,获取版本号
	
	public static void main(String[] args) {
		Upgrade up = new Upgrade();
		
		if (up.login(urlLogin, data.getUserp(), data.getPwdp(), data.getUser(), data.getPwd()) != 200) {
			System.out.println("Error,Login Unsuccessfully");
			
		} else {
			System.out.println("login ["+IP+"] successfully");
		}
		
		//get数据库升级
		System.out.println("-------DataBase Upgrading--------");
		if (up.get(dataUpUrl) == 200) {
			System.out.println("----------Upgrading Successfully----------");
		} else {
			System.out.println("----------Error, unable to Upgrade----------");
		}
	}
	
	

}
