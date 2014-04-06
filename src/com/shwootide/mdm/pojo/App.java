package com.shwootide.mdm.pojo;

public class App {
	//返回应用的基本信息，包括应用名称，应用类型，应用版本，安装手机最低版本信息
//	private String appName;
//	private String appType;
//	private String appPackageName;
//	public String getAppName() {
//		return appName;
//	}
//	public void setAppName(String appName) {
//		this.appName = appName; 
//	}
//	public String getAppType() {
//		return appType;
//	}
//	public void setAppType(String appType) {
//		this.appType = appType;
//	}
//	public String getAppPackageName() {
//		return appPackageName;
//	}
//	public void setAppPackageName(String appPackageName) {
//		this.appPackageName = appPackageName;
//	}
	
	private String ApplicationName;//应用名称
	private String ApplicationType;//应用类型
	private String DownloadUrl;//apk地址
	private String PackageName;//apk包名，可以用来检测应用程序安装与否
	private int id;
	
	public int getId() {
	    return id;
	}
	public void setId(int id) {
	    this.id = id;
	}
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getDownloadUrl() {
		return DownloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		DownloadUrl = downloadUrl;
	}
	public String getApplicationName() {
		return ApplicationName;//直接返回应用名称
	}
	public void setApplicationName(String applicationName) {
		ApplicationName = applicationName;
	}
	public String getApplicationType() {
		return ApplicationType;
	}
	public void setApplicationType(String applicationType) {
		ApplicationType = applicationType;
	}
	

}
