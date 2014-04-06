package com.shwootide.mdm.view;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.ab.activity.AbActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.igexin.GexinSdkMsgReceiver;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.App;
import com.shwootide.mdm.pojo.Log;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;
import com.shwootide.mdm.view.AppManagerView.DataLoadTask;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainLogView extends AbActivity {
    ListView listView;
    List<Log> loglist = new ArrayList<Log>();
    ProgressDialog progressDialog = null;
    Gson json = new Gson();
    FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	ExitApplication.getInstance().addActivity(this);
	db = FinalDb.create(this);
	setContentView(R.layout.mainlog);
	ActivityTools.headView(MainLogView.this, "日志总况");
	listView = (ListView) findViewById(R.id.mainlog_list);
	loglist = db.findAll(Log.class);
	if (loglist.size() == 0) {
	    if (progressDialog == null) {
		progressDialog = ProgressDialog.show(this, "请等待..",
			"正在加载数据..请稍后..");
	    }
	    MyTask task = new MyTask();
	    task.execute();
	} else {
	    MyAdapter adapter = new MyAdapter();
	    listView.setAdapter(adapter);
	}
	Button butt = (Button) findViewById(R.id.btn_refresh);
	butt.setVisibility(View.VISIBLE);
	butt.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (progressDialog == null) {
		    progressDialog = ProgressDialog.show(MainLogView.this,
			    "请等待..", "正在加载数据..请稍后..");
		}
		db.deleteByWhere(App.class, null);
		loglist.clear();
		MyTask task = new MyTask();
		task.execute();
	    }
	});
    }

    class MyAdapter extends BaseAdapter {

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return loglist.size();
	}

	@Override
	public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return loglist.get(arg0);
	}

	@Override
	public long getItemId(int position) {
	    
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    Holder holder = null;
	    if (view == null) {
		view = getLayoutInflater().inflate(R.layout.log_item, null);
		holder = new Holder();
		holder.tv_one = (TextView) view.findViewById(R.id.tv_one);
		holder.tv_two = (TextView) view.findViewById(R.id.tv_two);
		holder.tv_three = (TextView) view.findViewById(R.id.tv_three);
		view.setTag(holder);
	    } else {
		holder = (Holder) view.getTag();
	    }
	    holder.tv_one.setText(loglist.get(position).getRecordTime());
	    holder.tv_two.setText(loglist.get(position).getLogName());
	    holder.tv_three.setText(loglist.get(position).isIsSuccess() + "");
	    return view;
	}

    }

    class MyTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
	    HashMap<String, String> hmParam = new HashMap<String, String>();
	    hmParam.put("IMEI",PreferenceUtils.getPrefString(MainLogView.this, "imei", ""));
	    String result = WebService.getRemoteInfo(MainLogView.this,
		    "GetLogList", hmParam);
	    return result;
	}

	@Override
	protected void onPostExecute(String result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    if (progressDialog != null) {
		progressDialog.dismiss();
	    }
	    JsonReader reader = new JsonReader(new StringReader(result));
	    loglist = json.fromJson(reader, new TypeToken<List<Log>>() {
	    }.getType());
	    for (int i = 0; loglist.size() > i; i++) {
		db.save(loglist.get(i));
	    }
	    MyAdapter adapter = new MyAdapter();
	    listView.setAdapter(adapter);
	}
    }

    class Holder {
	TextView tv_one, tv_two, tv_three;
    }
}
