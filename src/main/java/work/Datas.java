package work;

public class Datas {
	
	private String IP = "127.0.0.1";
	private String user = "admin";
	private String pwd = "admin";
	private String urlLogin = "http://"+IP+"/tool/?do=check"; //登录
	private String dataUpUrl = "http://"+IP+"/tool/deploy/deploy.php";//数据库升级
	private String versionUrl = "http://"+IP+"/tool/welcome.php";//获取版本号
	private String userp = "name";
	private String pwdp = "pwd";

	

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
    
	public String getUrlLogin() {
		return urlLogin;
	}

	public void setUrlLogin(String urlLogin) {
		this.urlLogin = urlLogin;
	}
	
	public String getDataUpUrl() {
		return dataUpUrl;
	}

	public void setDataUpUrl(String dataUpUrl) {
		this.dataUpUrl = dataUpUrl;
	}
	
	public String getVersionUrl() {
		return versionUrl;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
	
	public String getUserp() {
		return userp;
	}

	public void setUserp(String userp) {
		this.userp = userp;
	}
	
	public String getPwdp() {
		return pwdp;
	}

	public void setPwdp(String pwdp) {
		this.pwdp = pwdp;
	}


}
