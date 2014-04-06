package com.shwootide.mdm.view;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.News;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.WebService;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsView extends ListActivity{
	
	private String TAG = "NewsView";
	ListView  listView ;
	List<News> listApp = new ArrayList<News>();
	AppAdapter appAdapter = new AppAdapter();
	
	
	DataLoadTask dlt = null;
	ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.news_view);
		ActivityTools.headView(NewsView.this,"新闻资讯");
		
		//返回 图片，标题，时间，链接（可以使用webview访问），还可以通过其他方式解析；服务器端该如何弄？直接访问html估计不是很好
		//服务器图片大小要有个限制；图片和文字单独存放在数据库当中
		//发布新闻的时候如何发布和如何记忆图片；发布一个带图文的文章；手机端解析也可以；
		//后台如何添，发布的时候可以输入文字和添加图片，都单独添加；这样子比较好控制，图片控制尺寸和大小 以及张数；一个新闻限制最多三张图；
		//图片可以添加标题，名字，链接，但是后台不展示，只手机端展示；
		//一个一个的添加可能不是很好
		//后台可能会存在问题？
		
//		WebView webView = new WebView(NewsView.this);//通过webview访问数据，可以采用两种方式访问数据试试
		
		
		if(dialog == null)
		{
			dialog = ProgressDialog.show(NewsView.this, "请等待...", "正在加载数据，请稍候...",true);
		}
		dlt = new DataLoadTask();//后台返回了所有的图片链接地址，可以启动一个新线程来加载图片，但是估计不是很理想
		dlt.execute("GetAppList","android");//这里填入合适的函数名称和相应参数
		listView = getListView();
		listView.setAdapter(appAdapter);// listView点击进入内页，先判断网络，没网络就从本地抓取数据，本地没有数据就提示无网络且本地无数据，有数据就提示当前是无网络状态下本地数据；
		//有网络的情况下，先检测本地DB当中是否已经存在这条新闻纪录，如果有，说明已经加载下来了，直接从本地抓取，如果没有就从网络下载；
		//下载图片方式都采用异步加载，防止等待时间太长
		//cnki.sgst.cn
		
		
		
		
	}
	
	class AppAdapter extends BaseAdapter
	{
		public List<News> listApp = new ArrayList<News>();
		public AppAdapter()
		{}
		public AppAdapter(List<News> listApp)
		{
			this.listApp = listApp;//初始化
		}

		@Override
		public int getCount() {
			return listApp.size();//返回list长度
		}

		@Override
		public Object getItem(int position) {
			return listApp.get(position);//返回list元素
		}

		@Override
		public long getItemId(int position) {
			return position;//返回ID
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view  = convertView;
			ViewHolder holder;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.news_list_item,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView)view.findViewById(R.id.img);
				holder.title = (TextView)view.findViewById(R.id.title);
				holder.time = (TextView)view.findViewById(R.id.time);// 6026 
				
				view.setTag(holder);
			}else
			{
				holder = (ViewHolder) view.getTag();
			}
			//照片单独处理
//			holder.img.setText(listApp.get(position).getApplicationName());//照片暂时先不要，后续可以考虑增加进去
			holder.title.setText(listApp.get(position).getTitle());
			//holder.time.setText(listApp.get(position).getTime());
			return view;
		}
		
		class ViewHolder {
			ImageView img;
			TextView title ,time ;//跟list模板文件对应
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
				hmParam.put("companyName", "");
				hmParam.put("appSysType", "");
				String strJson = WebService.getRemoteInfo(NewsView.this, functionName, hmParam);
				Log.e(TAG,strJson);
				return strJson;
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(dialog != null)
					dialog.dismiss();
				if(result.equals("-99")){
				    Toast.makeText(NewsView.this, "网络出现问题，请检查网络是否连接。", 1).show();
				    return;
				}
				
				Gson gson = new Gson();
				JsonReader reader = new JsonReader(new StringReader(result));
				reader.setLenient(true);
				listApp = gson.fromJson(reader, new TypeToken<List<News>>() {}.getType());
				appAdapter = new AppAdapter(listApp);
				listView.setAdapter(appAdapter);
				
				
			}
		}
		

}
