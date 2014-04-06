package com.shwootide.mdm.tools;
//http://lszdb1983.blog.163.com/blog/static/20426348201241175717181/
import java.util.List;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiConnect {
	static WifiManager wifiManager;
	//定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType
    {
     WIFICIPHER_WEP,WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }
//    
    //构造函数
    public WifiConnect(WifiManager wifiManager)
    {
      this.wifiManager = wifiManager;
    }
    
    //打开wifi功能
    public boolean OpenWifi()
    {
    	boolean bRet = true;
        if (!wifiManager.isWifiEnabled())
        {
         bRet = wifiManager.setWifiEnabled(true); 
        }
        return bRet;
    }
    
    //关闭wifi功能
    public static void closeWifi(Context context)
    {
    	wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled())
        {
        }else{
        	wifiManager.setWifiEnabled(false);
        }
    }
    
  //提供一个外部接口，传入要连接的无线网
    public boolean Connect(String SSID, String Password, WifiCipherType Type)
    {
       if(!this.OpenWifi())
       {
    	   return false;
       }
//     开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
//     状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
             while(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING )
             {
               try{
          //为了避免程序一直while循环，让它睡个100毫秒在检测……
                   Thread.currentThread();
          Thread.sleep(100);
                 }
                 catch(InterruptedException ie){
                }
             }
       WifiConfiguration wifiConfig = this.CreateWifiInfo(SSID, Password, Type);
       //
       if(wifiConfig == null)
       {
           return false;
       }
    
       WifiConfiguration tempConfig = this.IsExsits(SSID);
       
       if(tempConfig != null)
       {
    	   Log.i("IsExsits","IsExsits");
        this.wifiManager.removeNetwork(tempConfig.networkId);
       }
      
       int netID = this.wifiManager.addNetwork(wifiConfig);
       boolean bRet = wifiManager.enableNetwork(netID, false);  //不禁用其它网络
  //    boolean bRet = this.wifiManager.enableNetwork(netID, true); //禁用其它已经连接的网络
  
       return bRet;//返回一个业务逻辑
       
    }
    
  //查看以前是否也配置过这个网络，配置过重新配置前删除，SSID可能相同密码不同，所以先删除，然后重新配置
    public WifiConfiguration IsExsits(String SSID)
    {
     List<WifiConfiguration> existingConfigs = this.wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) 
        {
          if (existingConfig.SSID.equals("\""+SSID+"\""))
          {
              return existingConfig;//WifiConfiguration CreateWifiInfo(String SSID)
          }
        }
     return null;
    }
    
    private WifiConfiguration CreateWifiInfo(String SSID, String Password, WifiCipherType Type)
    {
    	WifiConfiguration config = new WifiConfiguration();  
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";  
        if(Type == WifiCipherType.WIFICIPHER_NOPASS)
        {
	       config.wepKeys[0] = "";
	       config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	       config.wepTxKeyIndex = 0;
        }
        if(Type == WifiCipherType.WIFICIPHER_WEP)
        {
	      config.preSharedKey = "\""+Password+"\""; 
	      config.hiddenSSID = true;  
         config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
         config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
         config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
         config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
         config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
         config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
         config.wepTxKeyIndex = 0;
     }
     if(Type == WifiCipherType.WIFICIPHER_WPA)
     {
	     config.preSharedKey = "\""+Password+"\"";
	     config.hiddenSSID = true;  
	     config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);  
	     config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                        
	     config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                        
	     config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                   
	     config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);                     
	     config.status = WifiConfiguration.Status.ENABLED; 
	   
	     
     }
     else
     {
      return null;
     }
     return config;
    }
    
}
