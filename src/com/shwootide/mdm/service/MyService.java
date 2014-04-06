package com.shwootide.mdm.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shwootide.mdm.devadmin.MyAdmin;
import com.shwootide.mdm.pojo.Password;
import com.shwootide.mdm.pojo.Restriction;
import com.shwootide.mdm.pojo.Result;
import com.shwootide.mdm.pojo.Wifi;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;
import com.shwootide.mdm.tools.WifiConnect;
import com.shwootide.mdm.tools.WifiConnect.WifiCipherType;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    Context context;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;
    @Override
    public IBinder onBind(Intent intent) {
	return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	context = getApplicationContext();
	String data = intent.getStringExtra("data");
	String cid = intent.getStringExtra("cid");
	String name= intent.getStringExtra("wifiname");
	if(name!=null&&!name.equals("")){
	    Log.i("name",name+"00000000");
	    Toast.makeText(context, "wifi添加成功！", 1).show();
	    Toast.makeText(context, name, 1).show();
	}
	if(data!=null&&!data.equals("data")){
	    deviceAdmin(data);
	}
	if(cid!=null&&!cid.equals("cid")){
	    DataLoadTask dlt = new DataLoadTask();
	    dlt.execute(PreferenceUtils.getPrefString(context, "imei", ""), cid);
	}
	return super.onStartCommand(intent, flags, startId);
    }
    @SuppressLint("NewApi")
    public boolean deviceAdmin(String data) {
	 devicePolicyManager = (DevicePolicyManager) context
		.getSystemService(Context.DEVICE_POLICY_SERVICE);
	componentName = new ComponentName(context,
		MyAdmin.class);
	if (componentName != null
		&& devicePolicyManager.isAdminActive(componentName)) {
	} else {
	    Toast.makeText(context, "请激活应用", 1).show();
	    return false;
	}
	WifiManager wifiManager = (WifiManager) context
		.getSystemService(Context.WIFI_SERVICE);
	if (data != null) {
	    // 根据@解析data
	    if (data.indexOf("@") > 0) {
		String msgtype = data.split("@")[0];
		Log.i("msgtype", msgtype);		
		String minType = data.split("@")[1];
		String cmd = data.split("@")[3];
		Gson gson = new Gson();
		if (msgtype.equals("Restriction"))// 限制命令
		{
		    Log.i("Restriction", cmd+"00000");
		    Restriction restrication = new Restriction();
		    restrication = gson.fromJson(cmd,
			    new TypeToken<Restriction>() {
			    }.getType());
			//Toast.makeText(LogView.this, android.os.Build.VERSION.RELEASE.substring(0, 1)+"", 1).show();

		    if(Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1))<3){
			Toast.makeText(context,"你的手机版本过低，相机策略配置失败！", 1).show();
		    }else{
			 if (restrication.getAllowUseCamera().equals("true")) {
				devicePolicyManager.setCameraDisabled(componentName,
					false);// 容许拍照
				Toast.makeText(context, "允许相机使用", 1).show();
			    } else {
				devicePolicyManager.setCameraDisabled(componentName,
					true);// 禁止拍照
				Toast.makeText(context, "禁止相机使用", 1).show();
			    }
			 updateLog(data.split("@")[2]);
		    }
		}
		if (msgtype.equals("Password"))//密码命令
		{
		    Log.i("Password", cmd);
		    Password password = new Password();
		    password = gson.fromJson(cmd, new TypeToken<Password>() {
		    }.getType());
		    setpassword(password,data);
		}
		if (msgtype.equals("Wifi"))// wifi命令
		{
		    Log.i("Restriction", cmd+"00000");
		    Wifi wifi = new Wifi();
		    wifi = gson.fromJson(cmd, new TypeToken<Wifi>() {
		    }.getType());
		
		    WifiConnect wifiConnect = new WifiConnect(wifiManager);
		    WifiConnect.WifiCipherType type = null;
		    Log.i("type", wifi.getEncryptionType() + "");
		    if (wifi.getEncryptionType().equals("WPA")) {
			type = WifiConnect.WifiCipherType.WIFICIPHER_WPA;
		    } else if (wifi.getEncryptionType().equals("Any")) {
			type = WifiConnect.WifiCipherType.WIFICIPHER_INVALID;

		    } else if (wifi.getEncryptionType().equals("WEP")) {
			type = WifiConnect.WifiCipherType.WIFICIPHER_WEP;
		    } else {
			type = WifiConnect.WifiCipherType.WIFICIPHER_NOPASS;
		    }
		
		    boolean ret = wifiConnect.Connect(wifi.getSSID_STR(),
    			    wifi.getPassword(),type);
//		    boolean ret = wifiConnect.Connect("shwootide_mobile",
//			    "Steve198100", WifiCipherType.WIFICIPHER_WPA);
		    Log.i("connect",
			    "123" + wifi.getSSID_STR() + "..."
				    + wifi.getPassword() + "..." + type);
		    
//		    if (ret) {
//			Toast.makeText(context, "wifi添加成功！", 1).show();
//			 updateLog(data.split("@")[2]); 
//		    } else {
//			Toast.makeText(context, "wifi添加失败", 1).show();
//		    }
		}
	    }
	}
	return true;
    }

    @SuppressLint("NewApi")
    public void setpassword(Password password,String data) {

	if (password.getMaxFailedAttempts() != null
		&& !password.getMaxFailedAttempts().equals("")) {
	    devicePolicyManager.setMaximumFailedPasswordsForWipe(componentName,
		    Integer.parseInt(password.getMaxFailedAttempts()));
	}
	// 密码历史记录个数
	if (password.getPinHistory() != null
		&& !password.getPinHistory().equals("")) {
	    Log.i("componentName", "正在修改密码历史记录个数");
	    devicePolicyManager.setPasswordHistoryLength(componentName,
		    Integer.parseInt(password.getPinHistory()));
	}
	// 最短的密码长度
	Log.i("componentName", password.getMinLength() + ".......");
	if (password.getMinLength() != null
		&& !password.getMinLength().equals("")) {
	    Log.i("componentName", "正在修改密码长度");
	    devicePolicyManager.setPasswordMinimumLength(componentName,
		    Integer.parseInt(password.getMinLength()));
	}
	
	// 最长的密码有效期//mobileIron
	if(Integer.parseInt(android.os.Build.VERSION.RELEASE.substring(0, 1))<3){
		Toast.makeText(context,"你的手机版本过低，配置密码最长有效期失败！", 1).show();

	    }else{
		if (password.getMaxPINAgeInDays() != null
			&& !password.getMaxPINAgeInDays().equals("")) {
		    Log.i("componentName", "正在修改密码有效期");
		    long timeMs = Integer.parseInt(password.getMaxPINAgeInDays())*24*60*60*1000;
		    devicePolicyManager.setPasswordExpirationTimeout(componentName,timeMs);
		}
	    }

	// 自动锁定时间
	if (password.getMaxInactivity() != null
		&& !password.getMaxInactivity().equals("")) {
	    Log.i("componentName", "正在修改密码锁定时间");
	    long timeMs =1000*60* Integer.parseInt(password.getMaxInactivity());
	    devicePolicyManager.setMaximumTimeToLock(componentName, timeMs);
	}
	// 对密码的要求
	if (password.getRequireAlphanumeric() != null
		&& !password.getRequireAlphanumeric().equals("")) {

	    if (password.getRequireAlphanumeric().equals("true")) {
		Log.i("componentName", "正在修改密码数字和字母");
		devicePolicyManager.setPasswordQuality(componentName,
			DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC);
	    } else {
		Log.i("componentName", "正在修改密码没要求");
		devicePolicyManager.setPasswordQuality(componentName,
			DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
	    }
	}
	Toast.makeText(context,"密码配置成功！", 1).show();
	 updateLog(data.split("@")[2]);
    }

    public boolean updateIMEIAndCID(String IMEI, String CID) {
	if (context == null) {
	    return false;
	}
	
	// storageInfo = null, string flowInfo = null, string deviceClientId =
	// null)
	
	HashMap<String, String> hmParam = new HashMap<String, String>();
	hmParam.put("IMEI", IMEI);
	hmParam.put("softwareInfo", null);
	hmParam.put("deviceClientId", CID);
	String result = WebService.getRemoteInfo(context, "UpdateClientId",
		hmParam);

	if (result.equals("-99")) {

	    return false;
	}
	List<Result> listResult = new ArrayList<Result>();
	Gson gson = new Gson();
	JsonReader reader = new JsonReader(new StringReader(result));
	reader.setLenient(true);
	listResult = gson.fromJson(reader, new TypeToken<List<Result>>() {
	}.getType());
	String ret = listResult.get(0).getCode();
	if (ret.equals("201")) {

	    return false;
	} else if (ret.equals("200")) {
	    return true;
	} else if (ret.equals("500")) {
	    return false;
	}
	return false;
    }
    class DataLoadTask extends AsyncTask<String, Integer, String> {
	@Override
	protected String doInBackground(String... params) {
	    // TODO Auto-generated method stub
	    String imei = params[0];
	    String cid = params[1];
	    updateIMEIAndCID(imei, cid);
	    return "";
	}
    }
    
    public void updateLog(String id){
	
	HashMap<String, String> hmParam = new HashMap<String, String>();
	hmParam.put("logId",id);
	String result = WebService.getRemoteInfo(context, "UpdateLogResult",
		hmParam);
	Log.i("log",result);
    }
}
