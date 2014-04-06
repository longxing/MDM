package com.igexin;

/*
 * 推送SDK演示工程
 * 
 * SDK Ver: 1.1.14.2
 */
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.slavesdk.MessageManager;
import com.shwootide.mdm.R;

public class GexinSdkDemoActivity extends Activity implements OnClickListener {

	/**
	 * 第三方应用Master Secret，修改为正确的值
	 */
	private static final String MASTERSECRET = "a02a76119b20d4e31620d7597a3b4f35";

	// 菜单
	private static final int ADDTAG = 100;
	private static final int NETSTAT = 101;
	private static final int VERSION = 102;
	private static final int PHONEADDRESS = 103;
	private static final int SILENTTIME = 104;
	private static final int EXIT = 106;

	// UI控件
	private Button btn_clear = null;
	private Button btn_service = null;
	private Button btn_enablelog = null;
	private Button btn_bindcell = null;
	private TextView tAppkeyView = null;
	private TextView tAppSecretView = null;
	private TextView tAppIdView = null;
	private TextView tMasterSecretView = null;
	public static TextView tView = null;
	public static TextView tLogView = null;
	private Button btn_pmsg = null;
	private Button btn_psmsg = null;
	private Button btn_send_msg = null;

	/**
	 * SDK服务是否启动
	 */
	private boolean tIsRunning = true;
	private Context mContext = null;
	private SimpleDateFormat formatter = null;
	private Date curDate = null;

	// SDK参数，会自动从Manifest文件中读取，第三方无需修改下列变量，请修改AndroidManifest.xml文件中相应的meta-data信息。
	// 修改方式参见个推SDK文档
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// UI初始化
		setContentView(R.layout.main);
		mContext = this;
		tIsRunning = true;
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		btn_service = (Button) findViewById(R.id.btn_service);
		btn_service.setOnClickListener(this);
		btn_enablelog = (Button) findViewById(R.id.btn_enablelog);
		btn_enablelog.setOnClickListener(this);
		btn_bindcell = (Button) findViewById(R.id.btn_bindcell);
		btn_bindcell.setOnClickListener(this);
		tView = (TextView) findViewById(R.id.tvclientid);
		tAppkeyView = (TextView) findViewById(R.id.tvappkey);
		tAppSecretView = (TextView) findViewById(R.id.tvappsecret);
		tMasterSecretView = (TextView) findViewById(R.id.tvmastersecret);
		tAppIdView = (TextView) findViewById(R.id.tvappid);
		tLogView = (EditText) findViewById(R.id.tvlog);
		tLogView.setInputType(InputType.TYPE_NULL);
		tLogView.setSingleLine(false);
		tLogView.setHorizontallyScrolling(false);
		btn_pmsg = (Button) findViewById(R.id.btn_pmsg);
		btn_pmsg.setOnClickListener(this);
		btn_psmsg = (Button) findViewById(R.id.btn_psmsg);
		btn_psmsg.setOnClickListener(this);

		btn_send_msg = (Button) findViewById(R.id.btn_send_msg);
		btn_send_msg.setOnClickListener(this);

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 从AndroidManifest.xml的meta-data中读取SDK配置信息
		String packageName = getApplicationContext().getPackageName();
		ApplicationInfo appInfo;
		try {
			appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			if (appInfo.metaData != null) {

				appid = appInfo.metaData.getString("appid");
				appsecret = appInfo.metaData.getString("appsecret");
				appkey = (appInfo.metaData.get("appkey") != null) ? appInfo.metaData.get("appkey").toString() : null;
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tAppkeyView.setText("AppKey=" + appkey);
		tAppSecretView.setText("AppSecret=" + appsecret);
		tMasterSecretView.setText("MasterSecret=" + MASTERSECRET);
		tAppIdView.setText("AppID=" + appid);

		// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
		Log.d("GexinSdkDemo", "initializing sdk...");
		MessageManager.getInstance().initialize(this.getApplicationContext());
	}

	@Override
	public void onDestroy() {
		Log.d("GexinSdkDemo", "onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onStop() {
		Log.d("GexinSdkDemo", "onStop()");
		super.onStop();
	}

	public void onClick(View v) {

		if (v == btn_send_msg) {

			final View view = new EditText(this);
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("输入上传数据").setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					TextView text = (TextView) view;

					byte[] uploadData = text.getText().toString().getBytes();

					// 第三方发送上行消息测试
					boolean result = MessageManager.getInstance().sendMessage(GexinSdkDemoActivity.this, String.valueOf(System.currentTimeMillis()), uploadData);

					Toast.makeText(mContext, "上传结果:" + result, Toast.LENGTH_SHORT).show();
					Log.d("GexinSdkDemo", "上传数据:" + text.getText().toString());

					dialog.dismiss();
				}
			}).setView(view);
			alertBuilder.create().show();

		} else if (v == btn_clear) {
			// 开启日志（仅针对日志版SDK，现网版本SDK不支持该接口）
			MessageManager.getInstance().setLogEnable(this.getApplicationContext(), true);

			// clear log box
			tLogView.setText("");

		} else if (v == btn_service) {
			if (tIsRunning) {

				// 当前为运行状态，停止SDK服务
				Log.d("GexinSdkDemo", "stopping sdk...");
				MessageManager.getInstance().stopService(this.getApplicationContext());

				// UI更新
				tView.setText(getResources().getString(R.string.no_clientid));
				btn_service.setText(getResources().getString(R.string.start));

				tIsRunning = false;
			} else {
				// 当前未运行状态，启动SDK服务
				Log.d("GexinSdkDemo", "reinitializing sdk...");

				// 重新初始化sdk
				MessageManager.getInstance().initialize(this.getApplicationContext());

				// UI更新
				btn_service.setText(getResources().getString(R.string.stop));
				tIsRunning = true;
			}
		} else if (v == btn_enablelog) {
			// 开启日志功能
			MessageManager.getInstance().setLogEnable(this.getApplicationContext(), true);

			Toast.makeText(this, "调试日志已打开", Toast.LENGTH_SHORT).show();
			Log.d("GexinSdkDemo", "调试日志已打开");
		} else if (v == btn_bindcell) {
			// 测试号码绑定接口
			MessageManager.getInstance().bindPhoneAddress(this.getApplicationContext());
			Toast.makeText(this, "号码绑定请求已发送，请稍候...", Toast.LENGTH_SHORT).show();

		} else if (v == btn_pmsg) {

			if (isNetworkConnected()) {

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("action", "pushmessage"); // pushmessage为接口名，注意全部小写
				/*---以下代码用于设定接口相应参数---*/
				param.put("appkey", appkey);
				param.put("appid", appid);
				// 注：透传内容后面需用来验证接口调用是否成功，假定填写为hello girl~
				param.put("data", "这是一条透传测试消息");

				curDate = new Date(System.currentTimeMillis());
				param.put("time", formatter.format(curDate)); // 当前请求时间，可选
				param.put("clientid", tView.getText().toString()); // 您获取的ClientID
				param.put("expire", 3600); // 消息超时时间，单位为秒，可选

				// 生成Sign值，用于鉴权
				param.put("sign", makeSign(MASTERSECRET, param));

				GexinSdkHttpPost.httpPost(param);

			} else {

				Toast toast = Toast.makeText(this, "对不起，当前网络不可用!", Toast.LENGTH_SHORT);

				toast.show();
			}

		} else if (v == btn_psmsg) {

			if (isNetworkConnected()) {

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("action", "pushSpecifyMessage"); // pushSpecifyMessage为接口名，注意大小写
				/*---以下代码用于设定接口相应参数---*/
				param.put("appkey", appkey);
				param.put("type", 2); // 推送类型： 2为消息
				param.put("pushTitle", "通知栏测试"); // pushTitle请填写您的应用名称

				// 推送消息类型，有TransmissionMsg、LinkMsg、NotifyMsg三种，此处以LinkMsg举例
				param.put("pushType", "LinkMsg");

				param.put("offline", true); // 是否进入离线消息

				param.put("offlineTime", 72); // 消息离线保留时间
				param.put("priority", 1); // 推送任务优先级

				List<String> cidList = new ArrayList<String>();
				cidList.add(tView.getText().toString()); // 您获取的ClientID
				param.put("tokenMD5List", cidList);

				// 生成Sign值，用于鉴权，需要MasterSecret，请务必填写
				param.put("sign", makeSign(MASTERSECRET, param));

				// LinkMsg消息实体
				Map<String, Object> linkMsg = new HashMap<String, Object>();
				linkMsg.put("linkMsgIcon", "push.png"); // 消息在通知栏的图标
				linkMsg.put("linkMsgTitle", "通知栏测试"); // 推送消息的标题
				linkMsg.put("linkMsgContent", "这是一条测试消息！"); // 推送消息的内容
				linkMsg.put("linkMsgUrl", "http://www.igexin.com/"); // 点击通知跳转的目标网页
				param.put("msg", linkMsg);

				GexinSdkHttpPost.httpPost(param);

			} else {
				Toast toast = Toast.makeText(this, "对不起，当前网络不可用!", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case ADDTAG: {
//			// 测试addTag接口
//			final View view = new EditText(this);
//			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//			alertBuilder.setTitle("设置Tag").setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					TextView tagText = (TextView) view;
//
//					String[] tags = tagText.getText().toString().split(",");
//					Tag[] tagParam = new Tag[tags.length];
//					for (int i = 0; i < tags.length; i++) {
//						Tag t = new Tag();
//						t.setName(tags[i]);
//						tagParam[i] = t;
//					}
//
//					Toast.makeText(mContext, "设置tag:" + tagText.getText().toString(), Toast.LENGTH_SHORT).show();
//					Log.d("GexinSdkDemo", "设置tag:" + tagText.getText().toString());
//
//					int i = MessageManager.getInstance().setTag(mContext, tagParam);
//					String text = "ERROR";
//
//					switch (i) {
//					case Consts.SETTAG_SUCCESS:
//						text = "设置标签成功";
//						break;
//					case Consts.SETTAG_ERROR_COUNT:
//						text = "设置标签失败，tag数量过大";
//						break;
//					case Consts.SETTAG_ERROR_FREQUENCY:
//						text = "设置标签失败，频率过快";
//						break;
//					case Consts.SETTAG_ERROR_REPEAT:
//						text = "设置标签失败，标签重复";
//						break;
//					case Consts.SETTAG_ERROR_UNBIND:
//						text = "设置标签失败，aidl服务未绑定";
//						break;
//					case Consts.SETTAG_ERROR_EXCEPTION:
//					default:
//						text = "设置标签失败，setTag异常";
//						break;
//					}
//
//					Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
//					Log.d("GexinSdkDemo", text);
//
//					dialog.dismiss();
//				}
//			}).setView(view);
//			alertBuilder.create().show();
//
//			break;
//		}
//		case NETSTAT: {
//			// 测试getNetstat 网络流量统计接口
//			GexinSdkNetstat netstat = new GexinSdkNetstat();
//			MessageManager.getInstance().getNetstat(this, netstat);
//			long inbound = GexinSdkNetstat.inboundBytes;
//			long outbound = GexinSdkNetstat.outboundBytes;
//			Toast.makeText(this, "总计流量为：" + String.valueOf(inbound + outbound) + "Bytes", Toast.LENGTH_SHORT).show();
//			break;
//		}
//		case VERSION: {
//			// 测试getVersion获取版本号接口
//			String version = MessageManager.getInstance().getVersion(this);
//			Toast.makeText(this, "当前sdk版本为：" + version, Toast.LENGTH_SHORT).show();
//			break;
//		}
//		case PHONEADDRESS: {
//			// 测试getPhoneAddress获取手机号接口
//			String address = MessageManager.getInstance().getPhoneAddress(this);
//			Toast.makeText(this, "当前手机号为：" + address, Toast.LENGTH_SHORT).show();
//			break;
//		}
//		case SILENTTIME: {
//			// 测试setSilentTime设置静默时间接口
//			final View view = LayoutInflater.from(this).inflate(R.layout.silent_setting, null);
//			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
//			alertBuilder.setTitle("设置静默时间段").setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					TextView beginText = (TextView) view.findViewById(R.id.beginText);
//					TextView durationText = (TextView) view.findViewById(R.id.durationText);
//
//					String begin = beginText.getText().toString();
//					String duration = durationText.getText().toString();
//
//					if (begin.equals("") || duration.equals("")) {
//
//						Toast.makeText(mContext, "设置静默时间段失败，请输入静默时间！", Toast.LENGTH_SHORT).show();
//						Log.d("GexinSdkDemo", "设置静默时间段失败，请输入静默时间！");
//
//					} else {
//
//						int beginHour = Integer.valueOf(begin);
//						int durationHour = Integer.valueOf(duration);
//
//						boolean result = MessageManager.getInstance().setSilentTime(mContext, beginHour, durationHour);
//
//						if (result) {
//							Toast.makeText(mContext, "设置静默时间段 begin:" + beginHour + " duration:" + durationHour, Toast.LENGTH_SHORT).show();
//							Log.d("GexinSdkDemo", "设置静默时间段 begin:" + beginHour + " duration:" + durationHour);
//						} else {
//							Toast.makeText(mContext, "设置静默时间段失败，取值超范围 begin:" + beginHour + " duration:" + durationHour, Toast.LENGTH_SHORT).show();
//							Log.d("GexinSdkDemo", "设置静默时间段失败，取值超范围 begin:" + beginHour + " duration:" + durationHour);
//						}
//					}
//
//					dialog.dismiss();
//				}
//			}).setView(view);
//			alertBuilder.create().show();
//			break;
//		}
//		case EXIT: {
//			// 结束
//			finish();
//			break;
//		}
//		default:
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ADDTAG, 0, "添加Tag");
		menu.add(0, NETSTAT, 1, "流量统计");
		menu.add(0, VERSION, 2, "当前版本");
		menu.add(0, PHONEADDRESS, 3, "查询手机号");
		menu.add(0, SILENTTIME, 4, "设置静默时间");
		menu.add(0, EXIT, 5, "退出");

		return super.onCreateOptionsMenu(menu);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回键最小化程序
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public boolean isNetworkConnected() {

		// 判断网络是否连接
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 生成Sign方法
	 */
	public static String makeSign(String masterSecret, Map<String, Object> params) throws IllegalArgumentException {
		if (masterSecret == null || params == null) {
			throw new IllegalArgumentException("masterSecret and params can not be null.");
		}

		if (!(params instanceof SortedMap)) {
			params = new TreeMap<String, Object>(params);
		}

		StringBuilder input = new StringBuilder(masterSecret);
		for (Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String || value instanceof Integer || value instanceof Long) {
				input.append(entry.getKey());
				input.append(entry.getValue());
			}
		}

		return getMD5Str(input.toString());
	}

	/**
	 * MD5加密
	 */
	public static String getMD5Str(String sourceStr) {
		byte[] source = sourceStr.getBytes();
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		java.security.MessageDigest md = null;
		try {
			md = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// #debug
			e.printStackTrace();
		}
		if (md == null)
			return null;
		md.update(source);
		byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
		// 用字节表示就是 16 个字节
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
		// 所以表示成 16 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) {
			// 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			byte byte0 = tmp[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
			// >>> 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		s = new String(str); // 换后的结果转换为字符串
		return s;
	}

}
