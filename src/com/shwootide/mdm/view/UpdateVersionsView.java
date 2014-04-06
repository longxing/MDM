package com.shwootide.mdm.view;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.Versions;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;

public class UpdateVersionsView extends AbActivity{
    LinearLayout ll_one;
    LinearLayout ll_two;
    ProgressDialog dialog;
    TextView tv_one,tv_two,tv_three,tv_four;
    String url=null;
    PackageInfo info=null;
    String versionName;
    FrameLayout fl_radar;
    int versionCode;
    private AbHttpUtil mAbHttpUtil = null;
    ImageView im_scan,im_dian;
    private int count;
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    	    // TODO Auto-generated method stub
    	    super.onCreate(savedInstanceState);
    	    setContentView(R.layout.versions);
    	ExitApplication.getInstance().addActivity(this);
    	ActivityTools.headView(UpdateVersionsView.this,"更新版本");
		try {
		    info = UpdateVersionsView.this.getPackageManager().getPackageInfo(UpdateVersionsView.this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
		    e.printStackTrace();
		}
	        versionName = info.versionName; 
	        versionCode = info.versionCode;
    	mAbHttpUtil = AbHttpUtil.getInstance(this);
    	initView();
    	//dialog=ProgressDialog.show(this, "更新提示...", "正在检查更新！请稍后...");
    	initAnim();
    	MyTask task=new MyTask();
    		task.execute();
    		
    	}
    	public void initAnim(){
    	im_scan = (ImageView) findViewById(R.id.im_scan);
	im_dian = (ImageView) findViewById(R.id.im_dian);
	RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
			0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	animation.setDuration(2000);
	animation.setRepeatCount(Animation.INFINITE);
	im_scan.startAnimation(animation);	
	AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
	animation2.setDuration(3000);
	animation2.setRepeatCount(Animation.INFINITE);
	im_dian.startAnimation(animation2);
	
	count = 0;
	
    	}
    	public void initView(){
    	
    	tv_four=(TextView) findViewById(R.id.tv_four);
    	fl_radar=(FrameLayout) findViewById(R.id.fl_radar);
    	ll_one=(LinearLayout) findViewById(R.id.linearlayout_one);
    	ll_two=(LinearLayout) findViewById(R.id.linearlayout_two);
    	 tv_one=(TextView) findViewById(R.id.vers_tv_one);
    	 tv_two=(TextView) findViewById(R.id.vers_tv_two);
    	 tv_three=(TextView) findViewById(R.id.vers_tv_three);
    	tv_one.setText(versionName);
    	tv_two.setText(versionName);
    	 Button butt=(Button) findViewById(R.id.updateversions);
    	 butt.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		
		fileDownloader(url);
	    }
	});
    	}
    	class MyTask extends AsyncTask<String, Integer, String>{
	    @Override
	    protected String doInBackground(String... params) {

		HashMap<String,String> hmParam = new HashMap<String,String>();
		hmParam.put("IMEI", PreferenceUtils.getPrefString(UpdateVersionsView.this, "imei", ""));
		 Log.i("json",PreferenceUtils.getPrefString(UpdateVersionsView.this, "imei", ""));
		String strJson = WebService.getRemoteInfo(UpdateVersionsView.this, "GetLatestSoftwareInfo", hmParam);
		return strJson;
	    }
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
//	        if(dialog.isShowing()){
//	            dialog.dismiss();
//	        }
	        im_scan.clearAnimation();
		im_dian.clearAnimation();
	        Log.i("json",result);
	        Gson json=new Gson();
	        	JsonReader reader=new JsonReader(new StringReader(result));
	        ArrayList<Versions> list = json.fromJson(reader,new TypeToken<List<Versions>>(){}.getType());
	       Versions vers=list.get(0);
	       fl_radar.setVisibility(View.GONE);
	       //handler.sendEmptyMessage(0);
	       tv_four.setVisibility(View.GONE);
	        if(vers.getVerName().equals(versionName)){
	            ll_one.setVisibility(View.VISIBLE);
	            ll_two.setVisibility(View.GONE);
	        }else{
	            url=vers.getDownloadUrl();
	            ll_one.setVisibility(View.GONE);
	            ll_two.setVisibility(View.VISIBLE);
	            tv_three.setText(vers.getVerName());
	        }
	    }
    	}
	public void fileDownloader(String url) {
		mAbHttpUtil.get(url, new AbFileHttpResponseListener(url) {
			@Override
			public void onSuccess(int statusCode, File file) {
				removeProgressDialog();
			    showToast("下载完成，正在安装。请稍后。");
			    //handler.sendEmptyMessage(1);
			    installApk(file);
			}

			// 开始执行前
			@Override
			public void onStart() {
			    showProgressDialog("正在下载最新版本。");

			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
			    	removeProgressDialog();
				 showToast("下载失败。。");
				 LoginMain();
				 
			}

			// 下载进度
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				
			}
			// 完成后调用，失败，成功
			public void onFinish() {

			};

		});
		
	};
	private void LoginMain(){    
	    Intent intent = new Intent(this,MainView.class);    
	    startActivity(intent);    
	    
	    this.finish();  
	}
	protected void installApk(File file) {    
	    Intent intent = new Intent();    
	    //执行动作     
	    intent.setAction(Intent.ACTION_VIEW); 
	    //执行的数据类型     
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.Android.package-archive");     
	    startActivity(intent);    
	}
	Handler handler=new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        // TODO Auto-generated method stub
	        super.handleMessage(msg);
	        switch(msg.what){
	        case 0:
	            //fl_radar.setVisibility(View.GONE);
	  
//	            int w=fl_radar.getWidth()/2;
//	            int h=fl_radar.getHeight()/2;
//	            int[] location = new int[2];        
//	            fl_radar.getLocationOnScreen(location);	            
//	                        int x = location[0];	            
//	                        int y = location[1];
//	          
//	            Animation mTranslateAnimation = new TranslateAnimation(x,x+w,y, y+h);// 移动
//	            mTranslateAnimation.setDuration(1000); 
//	            ScaleAnimation animation =new ScaleAnimation(1.0f, 0.5f,1.0f, 0.5f);
//	            animation.setDuration(1000);
//	            AnimationSet animationSet=new AnimationSet(true);
//	            animationSet.addAnimation(mTranslateAnimation);
//	            animationSet.addAnimation(animation);
//
//	            fl_radar.startAnimation(animation);
	           // animation.setFillAfter(true);
	            break;
	        case 10:
	            break;
	        }
	    }
	};

}
