package com.shwootide.mdm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	String action = intent.getAction();
	if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
	    NetworkInfo ni = intent
		    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
	    if (ni.getState() == State.CONNECTED
		    && ni.getType() == ConnectivityManager.TYPE_WIFI) {
		Log.v("kevin", "wifi connected");
		WifiManager wifiManager = (WifiManager) context
			.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		Log.d("wifiInfo", wifiInfo.toString());
		Log.d("SSID", wifiInfo.getSSID());

		Intent intwo = new Intent(context, MyService.class);
		intwo.putExtra("wifiname", wifiInfo.getSSID());
		context.startService(intwo);
	    } else if (ni.getState() == State.DISCONNECTED
		    && ni.getType() == ConnectivityManager.TYPE_WIFI) {
		Log.v("kevin", "wifi disconnected");
	    }
	}
    }

}
