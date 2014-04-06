package com.shwootide.mdm.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class NetworkUtil {
	/**
	 * 判断网络是否可用
	 * 
	 * @param mActivity
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean checkWap(Context context) {
		// 有网络的情况下
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();

		if (netWrokInfo != null&&netWrokInfo.getExtraInfo() != null
				&& (netWrokInfo.getExtraInfo().toLowerCase().equals("cmwap")
				|| netWrokInfo.getExtraInfo().toLowerCase().equals("ctwap")
				|| netWrokInfo.getExtraInfo().toLowerCase().equals("uniwap")
				|| netWrokInfo.getExtraInfo().toLowerCase().equals("3gwap"))) {
			return true;
		} else {
			return false;
		}
	}
	
	private static Cursor getProxyPortCursor(Context context) {
		Uri uri = Uri.parse("content://telephony/carriers/preferapn"); 
		Cursor mCursor = context.getContentResolver().query(uri,
				new String[] { "port" }, null, null, null);
		return mCursor;
	}

	private static Cursor getProxyHostCursor(Context context) {
		Uri uri = Uri.parse("content://telephony/carriers/preferapn"); 
		Cursor mCursor = context.getContentResolver().query(uri,
				new String[] { "proxy" }, null, null, null);
		return mCursor;
	}

	
	public static int getProxyPort(Context context) {
		int proxyPort = 1;
		Cursor mCursor = getProxyPortCursor(context);
		if (mCursor != null && mCursor.moveToFirst()) {
			proxyPort = mCursor.getInt(mCursor.getColumnIndex("port"));
			Log.e("proxyPort", proxyPort + "");
		}

		return proxyPort;
	}


	public static String getProxyHost(Context context) {
		String proxyHost = null;
		Cursor mCursor = getProxyHostCursor(context);
		if (mCursor != null && mCursor.moveToFirst()) {
			proxyHost = mCursor.getString(mCursor.getColumnIndex("proxy"));
			Log.e("proxyHost", proxyHost);
		}
		return proxyHost;
	}
	
	 /**
     * 获取网址内容
     * @param url
     * @return
     * @throws Exception
     */
     public static String getContent(String url) throws Exception{
         StringBuilder sb = new StringBuilder();
         
         HttpClient client = new DefaultHttpClient();
         HttpParams httpParams = client.getParams();
         //设置网络超时参数
         HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
         HttpConnectionParams.setSoTimeout(httpParams, 5000);
         HttpResponse response = client.execute(new HttpGet(url));
         HttpEntity entity = response.getEntity();
         if (entity != null) {
             BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
             
             String line = null;
             while ((line = reader.readLine())!= null){
                 sb.append(line + "\n");
             }
             reader.close();
         }
         return sb.toString();
     } 
     
     //检测网络的状态,如果网络不好，就返回false
     public static boolean CheckNetworkState(Context ctx)
 	{
 		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);  
         NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();  
         if (networkinfo == null || !networkinfo.isAvailable()) {  // 当前网络不可用
         	return false;
//         	AlertDialog.Builder builder = new Builder(MainActivity.this);  
// 	        builder.setMessage("检查到没有可用的网络连接,请打开网络连接");
// 	        builder.setTitle("提示");  
// 	        builder.setPositiveButton("确认",  
// 	        		new DialogInterface.OnClickListener(){
//                 public void onClick(DialogInterface dialoginterface, int i){
//                  ComponentName cn = new ComponentName("com.android.settings","com.android.settings.Settings");
// 					Intent intent = new Intent();
// 					intent.setComponent(cn);
// 					intent.setAction("android.intent.action.VIEW");
// 					startActivity(intent);
// 					MainActivity.this.finish();
//                    finish(); //去设置网络状态的时候，就把这个程序隐藏到后台去。
//                 }
//             });
// 	        builder.setNegativeButton("取消",  
// 	        new android.content.DialogInterface.OnClickListener() {  
// 	            @Override  
// 	            public void onClick(DialogInterface dialog, int which) {  
// 	            	SysApplication.getInstance().exit();
// 	        		System.exit(0);
// 	                dialog.dismiss();  
// 	            }  
// 	        });  
// 	        builder.create().show();  
         }
         else
         	return true;
 	  }

}
