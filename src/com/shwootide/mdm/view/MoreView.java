package com.shwootide.mdm.view;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.sqlite.Table;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.AfficheView;
import com.shwootide.mdm.pojo.Attachments;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.PreferenceUtils;
import com.shwootide.mdm.tools.WebService;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@Table(name = "NoticeV")
public class MoreView extends ListActivity {
    Gson gson = new Gson();
    private String TAG = "MoreView";
    ListView listView;
    List<AfficheView> listMyLog = new ArrayList<AfficheView>();
    MyLogAdapter myLogAdapter = new MyLogAdapter(listMyLog);
    DataLoadTask dlt = null;
    ProgressDialog dialog = null;
    FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	super.onCreate(savedInstanceState);
	ExitApplication.getInstance().addActivity(this);
	setContentView(R.layout.more_view);
	listView = getListView();
	db = FinalDb.create(this);
	ActivityTools.headView(MoreView.this, "公告管理");
	if ((listMyLog = db.findAll(AfficheView.class)).size() != 0) {
	    myLogAdapter = new MyLogAdapter(listMyLog);
	    listView.setAdapter(myLogAdapter);
	} else {
	    if (dialog == null) {
		dialog = ProgressDialog.show(MoreView.this, "请等待...",
			"正在加载数据，请稍候...", true);

	    }
	    dlt = new DataLoadTask();
	    dlt.execute("GetAnnounceList");
	}
	listView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		String json = listMyLog.get(arg2).getJson();
		// String json = gson.toJson(attachments);
		Intent intent = new Intent(MoreView.this, MainNoticeView.class);
		intent.putExtra("attachments", json);
		intent.putExtra("name", listMyLog.get(arg2).getUserName());
		intent.putExtra("title", listMyLog.get(arg2).getHeadline());
		intent.putExtra("time", listMyLog.get(arg2).getPublishTime());
		intent.putExtra("AnnounceContent", listMyLog.get(arg2)
			.getAnnounceContent());
		startActivity(intent);
	    }
	});
	Button butt = (Button) findViewById(R.id.btn_refresh);
	butt.setVisibility(View.VISIBLE);

	butt.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		dialog = ProgressDialog.show(MoreView.this, "请等待...",
			"正在加载数据，请稍候...", true);
		db.deleteByWhere(AfficheView.class, null);
		listMyLog.clear();
		dlt = new DataLoadTask();
		dlt.execute("GetAnnounceList");
	    }
	});
    }

    class MyLogAdapter extends BaseAdapter {
	public List<AfficheView> listMyLog = new ArrayList<AfficheView>();

	public MyLogAdapter() {
	}

	public MyLogAdapter(List<AfficheView> listMyLog) {
	    this.listMyLog = listMyLog;
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return listMyLog.size();
	}

	@Override
	public Object getItem(int position) {
	    // TODO Auto-generated method stub
	    return listMyLog.get(position);
	}

	@Override
	public long getItemId(int position) {
	    // TODO Auto-generated method stub
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    // TODO Auto-generated method stub
	    View view = convertView;
	    ViewHolder holder;
	    if (view == null) {
		view = getLayoutInflater().inflate(R.layout.logview_list_item,
			null);
		holder = new ViewHolder();
		holder.LogName = (TextView) view.findViewById(R.id.LogName);
		holder.promulgator = (TextView) view
			.findViewById(R.id.promulgator);
		holder.time = (TextView) view.findViewById(R.id.time);
		view.setTag(holder);
	    } else {
		holder = (ViewHolder) view.getTag();
	    }
	    holder.LogName.setText(listMyLog.get(position).getHeadline());
	    holder.time.setText(listMyLog.get(position).getPublishTime());
	    holder.promulgator.setText(listMyLog.get(position).getUserName());
	    return view;
	}

	class ViewHolder {
	    TextView LogName, promulgator, time;// 跟list模板文件对应
	}

    }

    class DataLoadTask extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
	    // TODO Auto-generated method stub
	    String functionName = params[0];
	    HashMap<String, String> hmParam = new HashMap<String, String>();
	    hmParam.put("IMEI",
		    PreferenceUtils.getPrefString(MoreView.this, "imei", ""));
	    String strJson = WebService.getRemoteInfo(MoreView.this,
		    functionName, hmParam);
	    Log.e(TAG, strJson);
	    return strJson;
	}

	@Override
	protected void onPostExecute(String result) {
	    // TODO Auto-generated method stub
	    super.onPostExecute(result);
	    if (dialog != null)
		dialog.dismiss();
	    if (result.equals("-99")) {
		Toast.makeText(MoreView.this, "网络出现问题，请检查网络是否连接。", 1).show();
		return;
	    }
	    JsonReader reader = new JsonReader(new StringReader(result));

	    listMyLog = gson.fromJson(reader,
		    new TypeToken<List<AfficheView>>() {
		    }.getType());
	    for (int i = 0; listMyLog.size() > i; i++) {
		Attachments[] attachments = listMyLog.get(i).getAttachments();
		String json = gson.toJson(attachments);
		listMyLog.get(i).setJson(json);
	    }
	    for (int i = 0; listMyLog.size() > i; i++) {
		db.save(listMyLog.get(i));
	    }
	    myLogAdapter = new MyLogAdapter(listMyLog);
	    listView.setAdapter(myLogAdapter);

	}
    }

}
