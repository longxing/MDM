package com.shwootide.mdm.view;


import android.app.Application;


public class MyApplication extends Application {
    private static final MyApplication instance =new MyApplication();
    public static String appinfojson = null;

    public String getAppinfojson() {
	return appinfojson;
    }

    public void setAppinfojson(String appinfojson) {
	this.appinfojson = appinfojson;
    }

    @Override
    public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();
	
    }
    
    public static MyApplication getApplicationInstance(){
	return instance;
    }

}
