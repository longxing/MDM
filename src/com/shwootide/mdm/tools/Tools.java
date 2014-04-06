package com.shwootide.mdm.tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.GexinSdkMsgReceiver;
import com.shwootide.mdm.pojo.AppInfo;
import com.shwootide.mdm.pojo.MyFile;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Tools {
	private final static String  TAG = "Tools";
	public static Context context;
	
	
	
	
	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		Tools.context = context;
	}

	 
	 public static String bytes2kb(long size)
		{
		/**size 如果 小于1024 * 1024,以KB单位返回,反则以MB单位返回*/
		DecimalFormat df = new DecimalFormat("###.##");
		   float f;
		   if (size < 1024 * 1024) {
		    f = (float) ((float) size / (float) 1024);
		    return (df.format(new Float(f).doubleValue())+"KB");
		   } else {
		    f = (float) ((float) size / (float) (1024 * 1024));
		    return (df.format(new Float(f).doubleValue())+"MB");
		   }  
		   
		}
	
	
}
