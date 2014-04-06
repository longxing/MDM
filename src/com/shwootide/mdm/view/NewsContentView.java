package com.shwootide.mdm.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.sqlite.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.activity.AbActivity;
import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.view.sample.AbScaleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.News;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.WebService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
@Table(name="news")
public class NewsContentView extends AbActivity {
    private ListView lv;
    private List<News> newsList = new ArrayList<News>();
    MyAdapter adapter;
    String IMEI;
    FinalDb db;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);
	ExitApplication.getInstance().addActivity(this);
	setContentView(R.layout.news_content_jack);
	ActivityTools.headView(NewsContentView.this, "新闻资讯");
	db=FinalDb.create(this);
	TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	IMEI = tm.getDeviceId();
	lv = (ListView) findViewById(R.id.news_content_list);
	initView();
	if((newsList=db.findAll(News.class)).size()!=0){
	    adapter = new MyAdapter(newsList);
	    lv.setAdapter(adapter);
	    adapter.notifyDataSetChanged();
		
	}else{
	    showProgressDialog("请稍等正在加载数据..");
		MyTask myTask = new MyTask();
		myTask.execute("GetNewsList", IMEI);
	}
	Button butt=(Button) findViewById(R.id.btn_refresh);
	butt.setVisibility(View.VISIBLE);
	butt.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		 showProgressDialog("请稍等正在加载数据..");
		 db.deleteByWhere(News.class,null);
		 newsList.clear();
		MyTask myTask = new MyTask();
		myTask.execute("GetNewsList", IMEI);
	    }
	});
    }

    public void initView() {
//	lv = (ListView) findViewById(R.id.news_content_list);
//	adapter = new MyAdapter(newsList);
//	lv.setAdapter(adapter);
	
	lv.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
	    		Intent intent=new Intent(NewsContentView.this,MainNewsContent.class);
	    		intent.putExtra("data", newsList.get(arg2).getHtmlFile());
	    		startActivity(intent);
	    }
	});
    }

    private class MyAdapter extends BaseAdapter {
	int poditiontwo=0;
	private List<News> lv = new ArrayList<News>();
	   AbImageDownloader mAbImageDownloader = null;
	public MyAdapter(List<News> lv) {
	    super();
	 
	    mAbImageDownloader = new AbImageDownloader(NewsContentView.this);
	    this.lv = lv;
	}

	@Override
	public int getCount() {

	    return lv.size();
	}

	@Override
	public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return lv.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    ViewHolder holder;
	   
    		  if (view == null) {
    		    	view = mInflater.inflate(R.layout.news_content_item, null);
    		    	
    	    		holder = new ViewHolder();
    	    	holder.llayout= (LinearLayout) view.findViewById(R.id.news_llayout);
    	    		holder.imageView = (AbScaleImageView) view
    	    			.findViewById(R.id.news_content_item_image);
    	    		holder.TextViewOne = (TextView) view
    	    			.findViewById(R.id.news_content_item_text_one);
    	    		holder.TextViewTwo = (TextView) view
    	    			.findViewById(R.id.news_content_item_text_two);
    	    		view.setTag(holder);
    	    		holder.TextViewThree = (TextView) view
        	    			.findViewById(R.id.news_content_item_dt_textview);
        	    		view.setTag(holder);
    		    } else {
    			holder = (ViewHolder) view.getTag();
    		    }
    		  holder.imageView.setImageWidth(150);
    		  holder.imageView.setImageHeight(150);
    		  mAbImageDownloader.setWidth(150);
    		  mAbImageDownloader.setHeight(150);
    		  mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
    		  mAbImageDownloader.display(holder.imageView,lv.get(position).getIcon());
    		    holder.TextViewOne.setText(lv.get(position).getTitle());
    		    holder.TextViewTwo.setText(lv.get(position).getSummary());
    		    holder.TextViewThree.setText(lv.get(position).getDt());
    		    if(position!=0){
    			  if(lv.get(position).getDt().equals(lv.get(position-1).getDt())){
    			  holder.llayout.setVisibility(View.GONE);
    	    		    }
    		    }
    		  
	    return view;
	}

    }

    private class MyTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
	    String functionName = params[0];
	    HashMap<String, String> map = new HashMap<String, String>();
	    map.put("IMEI", params[1]);
	    String strJson = WebService.getRemoteInfo(NewsContentView.this,
		    functionName, map);
	    return strJson;
	}

	@Override
	protected void onPostExecute(String result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    removeProgressDialog();
	    Gson gson = new Gson();
	    News news = new News();
	    Log.i("newscontext", result);
	    JSONArray jsa;
	    try {

		jsa = new JSONArray(result);
		for (int i = 0; jsa.length() > i; i++) {
		    JSONObject job = jsa.getJSONObject(i);
		    Log.i("newscontext", job + "");
		    JSONArray json = job.getJSONArray("singleNewsList");
		    for(int j = 0; json.length() > j; j++){
			 String jsonone = (String) job.get("dt");
			    Log.i("newscontext", jsonone + "222222222222");
			    News newstwo = new News();
			    newstwo = gson.fromJson(json.getJSONObject(j).toString(),
				    new TypeToken<News>() {
				    }.getType());
			    newstwo.setDt(jsonone);
			    db.save(newstwo);
			    newsList.add(newstwo);
		    }
		   
		}
		adapter = new MyAdapter(newsList);
		lv.setAdapter(adapter);
		
	
	    } catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    class ViewHolder {
    	AbScaleImageView imageView;
	TextView TextViewOne, TextViewTwo,TextViewThree;
	LinearLayout llayout;
    }

}
