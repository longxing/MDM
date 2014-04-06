package com.igexin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;

import android.content.res.Configuration;
import android.util.Log;

public class GexinSdkHttpPost {

	public static final String SERVICEURL = "http://sdk.open.api.igexin.com/service";
	public static final int CONNECTION_TIMEOUT_INT = 8000;
	public static final int READ_TIMEOUT_INT = 5000;

	public static void httpPost(Map<String, Object> map) {

		String param = JSONObject.toJSONString(map);

		if (param != null) {

			URL url = null;

			try {
				url = new URL(SERVICEURL);
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoInput(true); // 设置输入流采用字节流
				urlConn.setDoOutput(true); // 设置输出流采用字节流
				urlConn.setRequestMethod("POST");
				urlConn.setUseCaches(false); // 设置缓存
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.setConnectTimeout(CONNECTION_TIMEOUT_INT);
				urlConn.setReadTimeout(READ_TIMEOUT_INT);

				urlConn.connect(); // 连接既往服务端发送消息

				DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
				dop.write(param.getBytes("utf-8")); // 发送参数
				dop.flush(); // 发送，清空缓存
				dop.close(); // 关闭

				// 下面开始做接收工作
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				String result = ""; // 获取服务器返回数据
				String readLine = null;
				while ((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				bufferReader.close();
				urlConn.disconnect();

				System.out.println("result： " + result);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("param is null");
		}
	}

}
