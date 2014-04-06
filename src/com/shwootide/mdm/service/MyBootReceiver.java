package com.shwootide.mdm.service;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.igexin.GexinSdkMsgReceiver;
import com.shwootide.mdm.common.Configuration;
import com.shwootide.mdm.pojo.AfficheView;
import com.shwootide.mdm.pojo.App;
import com.shwootide.mdm.pojo.AppInfo;
import com.shwootide.mdm.pojo.News;
import com.shwootide.mdm.pojo.Result;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;
import com.shwootide.mdm.view.AppManagerView;
import com.shwootide.mdm.view.LoginView;
import com.shwootide.mdm.view.MainView;
import com.shwootide.mdm.view.MoreView;
import com.shwootide.mdm.view.MyApplication;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyBootReceiver extends BroadcastReceiver {
    private static String TAG = "MyBootReceiver";
    Context context;
    Gson gson = new Gson();
    MyApplication application=MyApplication.getApplicationInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
	
	Log.i("MyBootReceiver", "测试成功");
	this.context = context;
	String appinfojson = MyApplication.appinfojson;
	if (appinfojson != null){
	    Log.i("MyBootReceiver", appinfojson);
	    String packageName = intent.getDataString().substring(8);
	    Log.i("MyBootReceiver", packageName+"packageName");
	    TelephonyManager tm = (TelephonyManager) context
		    .getSystemService(Context.TELEPHONY_SERVICE);
	    String IMEI = tm.getDeviceId();
	    JsonReader jsonReader = new JsonReader(
		    new StringReader(appinfojson));
	    List<AppInfo> fromjson = gson.fromJson(jsonReader,
		    new TypeToken<List<AppInfo>>() {
		    }.getType());

	    Log.i("MyBootReceiver", fromjson.size() + "");
	    // TODO Auto-generated method stub
	    if (intent.getAction()
		    .equals("android.intent.action.PACKAGE_ADDED")) {
		List<PackageInfo> packages = context.getPackageManager()
			.getInstalledPackages(0);
		for (int j = 0; j < packages.size(); j++) {
		    PackageInfo packageInfo = packages.get(j);
		    AppInfo tmpInfo = new AppInfo();
		    tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
			    context.getPackageManager()).toString();
		    tmpInfo.bundledId = packageInfo.packageName;
		    tmpInfo.versionName = packageInfo.versionName;
		    if (packageInfo.packageName.equals(packageName)) {
			fromjson.add(tmpInfo);
		    }
		}
	    }
	    if (intent.getAction().equals(
		    "android.intent.action.PACKAGE_REMOVED")) {

		for (int i = 0; fromjson.size() > i; i++) {
		    if (fromjson.get(i).getBundledId().equals(packageName)) {
			fromjson.remove(i);
		    }
		}

	    }
	    String json = gson.toJson(fromjson);
	    Log.i("MyBootReceiver", json + "");
	    DataLoadTask dataLoadTask = new DataLoadTask();
	    dataLoadTask.execute(json);

	}

    }

    class DataLoadTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
	    // TODO Auto-generated method stub

	    updateIMEIAndCID(PreferenceUtils.getPrefString(context, "imei", ""), params[0]);
	    return "";
	}
    }

    public void updateIMEIAndCID(String IMEI, String appinfo) {

	// public string UpdateClientId(string IMEI, string softwareInfo, string
	// storageInfo = null, string flowInfo = null, string deviceClientId =
	// null)
	
	HashMap<String, String> hmParam = new HashMap<String, String>();
	hmParam.put("IMEI", IMEI);
	hmParam.put("softwareInfo", appinfo);// 更新信息先做到这一步，更新软件信息后续会添加到一个自动轮询的服务里面去
	
	String result = WebService.getRemoteInfo(context, "UpdateClientId",
		hmParam);
	if (result.equals("-99")) {
	    Log.e(TAG, "没网络...");
	    return;
	}
	List<Result> listResult = new ArrayList<Result>();
	Gson gson = new Gson();
	JsonReader reader = new JsonReader(new StringReader(result));
	reader.setLenient(true);
	listResult = gson.fromJson(reader, new TypeToken<List<Result>>() {
	}.getType());
	String ret = listResult.get(0).getCode();
	if (ret.equals("201")) {
	    Log.e(TAG, "IMEI没登记。。。");
	    return;
	} else if (ret.equals("200")) {
	    Log.i("MyBootReceiver", appinfo+ "..........");
	    Log.e(TAG, "CID更新成功。。。");
	    return;
	} else if (ret.equals("500")) {
	    Log.e(TAG, "服务器出错。。。");
	    return;
	}

    }
}
