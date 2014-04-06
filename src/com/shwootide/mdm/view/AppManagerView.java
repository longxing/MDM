package com.shwootide.mdm.view;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.sqlite.Table;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shwootide.mdm.R;
import com.shwootide.mdm.common.CheckUpdate;
import com.shwootide.mdm.pojo.App;
import com.shwootide.mdm.pojo.AppInfo;
import com.shwootide.mdm.pojo.Result;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.APKTools;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.CommonUtil;
import com.shwootide.mdm.tools.NetworkUtil;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;
@Table(name="app")
public class AppManagerView extends ListActivity{
	FinalDb db;
	private String TAG = "AppManagerView";
	ListView  listView ;
	List<App> listApp = new ArrayList<App>();
	List<AppInfo> listAppinfo = new ArrayList<AppInfo>();
	AppAdapter appAdapter= new AppAdapter(listApp);
	DataLoadTask dlt = null;
	ProgressDialog dialog = null; //显示加载进度条
	protected void onCreate(Bundle savedInstanceState) {
	    
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.appmanager_view);
		db=FinalDb.create(this);
		listView = getListView();
		ActivityTools.headView(AppManagerView.this,"应用商店");
		
			
		if((listApp=db.findAll(App.class)).size()!=0){
		    appAdapter = new AppAdapter(listApp);
		    listView.setAdapter(appAdapter);
		    appAdapter.notifyDataSetChanged();
		}else{
		    if(dialog == null)
			{
				dialog = ProgressDialog.show(AppManagerView.this, "请等待...", "正在加载数据，请稍候...",true);
			}
			dlt = new DataLoadTask();
			dlt.execute("GetAppList");//这里填入合适的函数名称和相应参数
			
			//数据适配
		}
		listView.setAdapter(appAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String url = appAdapter.listApp.get(arg2).getDownloadUrl();//获取路径？
				if(!NetworkUtil.isNetworkAvailable(AppManagerView.this))
				{
					CommonUtil.myToast(AppManagerView.this, "没有网络，无法安装");
					return ;
				}
				String packageName = appAdapter.listApp.get(arg2).getPackageName();
//				packageName = "com.liao";
				if(APKTools.isAppInstalled(AppManagerView.this, packageName))
				{
					APKTools.uninstallAPK(AppManagerView.this, packageName);
					
				}else
				{
					new CheckUpdate(AppManagerView.this,url);
					
				}
			
			}
		});
		
		Button butt=(Button) findViewById(R.id.btn_refresh);
		butt.setVisibility(View.VISIBLE);

		butt.setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			dialog = ProgressDialog.show(AppManagerView.this, "请等待...", "正在加载数据，请稍候...",true);
			db.deleteByWhere(App.class,null);
			listApp.clear();
			dlt = new DataLoadTask();
			dlt.execute("GetAppList");
		    }
		});
		
	}
	
	
	class AppAdapter extends BaseAdapter
	{
		public List<App> listApp = new ArrayList<App>();
		public AppAdapter()
		{}
		public AppAdapter(List<App> listApp)
		{
			//应用管理列表
			this.listApp = listApp;//初始化
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listApp.size();//返回list长度
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listApp.get(position);//返回list元素
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;//返回ID
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view  = convertView;
			ViewHolder holder;
			if (view == null) {
			    	
				view = getLayoutInflater().inflate(R.layout.appmanager_view_list_item,
						null);
				holder = new ViewHolder();
				holder.app_name = (TextView)view.findViewById(R.id.app_name);
				holder.app_type = (TextView)view.findViewById(R.id.app_type);
				holder.app_op = (TextView)view.findViewById(R.id.app_op);// 
				view.setTag(holder);
			}else
			{
				holder = (ViewHolder) view.getTag();
			}
			holder.app_name.setText(listApp.get(position).getApplicationName());
			holder.app_type.setText(listApp.get(position).getApplicationType());
			//根据包名，解析本地是否已经安装本应用，而显示安装或者卸载字样
			String packageName = listApp.get(position).getPackageName();
			if(APKTools.isAppInstalled(AppManagerView.this, packageName))
			{
				holder.app_op.setText("卸载");
				
			}else
			{
				holder.app_op.setText("安装");
			}
			//暂时先这样展示，
			return view;
		}
		
		class ViewHolder {
			TextView app_name ,app_type ,app_op ;//跟list模板文件对应
			}
		
	}
	
	//下载应用程序相关的数据
	class DataLoadTask extends AsyncTask<String,Integer,String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String functionName = params[0];
			HashMap<String,String> hmParam = new HashMap<String,String>();
			hmParam.put("IMEI",PreferenceUtils.getPrefString(AppManagerView.this, "imei", ""));
			
			String strJson = WebService.getRemoteInfo(AppManagerView.this, functionName, hmParam);
			
			return strJson;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog != null)
				dialog.dismiss();
			Log.i("appmanager",result);
			if(result.equals("-99")){
			    Toast.makeText(AppManagerView.this, "网络出现问题，请检查网络是否连接。", 1).show();
			    return;
			}
			ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(result));
			reader.setLenient(true);
			listApp = gson.fromJson(reader, new TypeToken<List<App>>() {}.getType());
			for(int i=0;listApp.size()>i;i++){
			    db.save(listApp.get(i));
			    String packageName = listApp.get(i).getPackageName();
				 // 用来存储获取的应用信息数据
				List<PackageInfo> packages = AppManagerView.this.getPackageManager()
					.getInstalledPackages(0);
				for (int j= 0; j< packages.size(); j++) {
				    PackageInfo packageInfo = packages.get(j);
				    AppInfo tmpInfo = new AppInfo();
				    tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
					    AppManagerView.this.getPackageManager()).toString();
				    tmpInfo.bundledId = packageInfo.packageName;
				    tmpInfo.versionName = packageInfo.versionName;
				    //tmpInfo.uid = packageInfo.applicationInfo.uid;
				    if(packageInfo.packageName.equals(packageName)){
					appList.add(tmpInfo);
				    }
				}
			}
			Type type = new TypeToken<List<AppInfo>>() {
			}.getType();
			beanListToJson = gson.toJson(appList, type);
			Log.i("beanListToJson",beanListToJson);
			new DataLoadTasktwo().execute();
			appAdapter = new AppAdapter(listApp);
			listView.setAdapter(appAdapter);
			
		}
	}
	String beanListToJson;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(appAdapter.listApp.size()>0)
		{
			List<App> listApp = new ArrayList<App>();
			listApp = appAdapter.listApp;
			appAdapter = new AppAdapter(listApp);
			listView.setAdapter(appAdapter);
		}
		
	}
	public void updateApp(){
		appAdapter.notifyDataSetChanged();
		listView.notify();
		listView.setAdapter(appAdapter);
	}
	
	
	    public void updateIMEIAndCID(String IMEI, String appinfo) {

		
		
		HashMap<String, String> hmParam = new HashMap<String, String>();
		hmParam.put("IMEI", IMEI);
		hmParam.put("softwareInfo", appinfo);// 更新信息先做到这一步，更新软件信息后续会添加到一个自动轮询的服务里面去
		String result = WebService.getRemoteInfo(AppManagerView.this,
			"UpdateClientId", hmParam);
		if (result.equals("-99")) {
		    Toast.makeText(this, "网络出现问题，请检查网络是否连接。", 1).show();
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
		    Log.i("beanListToJson",appinfo);
		    MyApplication application=new MyApplication();
		    application.setAppinfojson(appinfo);
		    
		    Log.e(TAG, "CID更新成功。。。");
		    return;
		} else if (ret.equals("500")) {
		    Log.e(TAG, "服务器出错。。。");
		    return;
		}
	    }
	    class DataLoadTasktwo extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
		    updateIMEIAndCID(PreferenceUtils.getPrefString(AppManagerView.this, "imei", ""),beanListToJson);
		    return "";
		}
	    }
}
