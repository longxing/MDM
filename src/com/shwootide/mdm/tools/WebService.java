package com.shwootide.mdm.tools;

import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
//import android.util.Log;


public class WebService {
	static String TAG = "WebService";
	//String result = WebService.getRemoteInfo(this.context, "UpdateClientId", login, hmParam);
	public static String getRemoteInfo(Context context,String functionName,HashMap<String,String> hmParam)
    {
	    Log.d("WebService", "启动66666666");
		String strJson = "-99"; 
		try
		{

			if(!NetworkUtil.CheckNetworkState(context))//检测网络，可以提示错误界面，直接跳转
			{
			   Toast.makeText(context, "网络出现问题，请检查网络是否连接。", 1).show();
			}else
			{
		
	    	String nameSpace = "http://mypatroller.com/";//这里跟后台web服务配置对应一致
	    	String ECName = hmParam.get("companyname");//Configuration.ECNAME, "");
	    	String methodName = functionName;
	    	String endPoint = "";
	    	endPoint = "http://mobile.mypatroller.com/androidservice.asmx";
	    	String soapAction = nameSpace+methodName;  
	    	HttpTransportSE transport = new HttpTransportSE(endPoint,300000);
	    	SoapObject soapObject = new SoapObject(nameSpace, methodName);
	    	for(String strKey:hmParam.keySet())
	    	{
	    		soapObject.addProperty(strKey,hmParam.get(strKey));
	    		
	    	}
	    	
	    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12); 
	    	envelope.bodyOut = transport;
	    	envelope.dotNet = true;
	    	envelope.setOutputSoapObject(soapObject);
	    	try{
	    		transport.call(soapAction, envelope);	
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    	SoapObject object = (SoapObject)envelope.bodyIn;//这里抛异常，奇怪了...
	    	
	    	if(object == null)
	    	{
	    		Log.d(TAG, "object is null");
	    		//Log.e("Exception","2-------");
	    	}else
	    	{
	    		strJson = object.getProperty(0).toString() ;
	    	}
	    	Log.d("WebService", strJson+"66666666");
			}
		}catch(Exception e)
		{
//			context.startActivity(intent);
		}
		if(strJson.equals("anyType{}"))
			strJson = "-99";
		return strJson;
    }
}
