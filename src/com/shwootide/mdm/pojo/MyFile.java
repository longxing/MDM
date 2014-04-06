package com.shwootide.mdm.pojo;

import android.graphics.drawable.Drawable;

public class MyFile {
	private Drawable apk_icon;
	private String packageName;
	private String filePath;
	private String versionName; 
	private int versionCode;
	private int installed ;
	
	public int getInstalled() {
		return installed;
	}

	public void setInstalled(int installed) {
		this.installed = installed;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getApk_icon() {
		return apk_icon;
	}

	public void setApk_icon(Drawable apk_icon) {
		this.apk_icon = apk_icon;
	}
	
	

}
