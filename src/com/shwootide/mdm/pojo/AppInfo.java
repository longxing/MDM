package com.shwootide.mdm.pojo;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppInfo {
	public String appName="";
	public String bundledId="";
	public String versionName="";
	//public int  uid;
	
	

	//	public int versionCode=0;
//	public Drawable appIcon=null;
//	public void print()
//	{
//		Log.v("app","Name:"+appName+" Package:"+packageName);
//		Log.v("app","Name:"+appName+" versionName:"+versionName);
//		Log.v("app","Name:"+appName+" versionCode:"+versionCode);
//	}
	public String getAppName() {
		return appName;
	}
	public String getBundledId() {
	    return bundledId;
	}
	public void setBundledId(String bundledId) {
	    this.bundledId = bundledId;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
//	public String getPackageName() {
//		return packageName;
//	}
//	public void setPackageName(String packageName) {
//		this.packageName = packageName;
//	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
//	public int getVersionCode() {
//		return versionCode;
//	}
//	public void setVersionCode(int versionCode) {
//		this.versionCode = versionCode;
//	}
	
	

}
