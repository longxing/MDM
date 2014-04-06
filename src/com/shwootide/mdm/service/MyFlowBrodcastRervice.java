package com.shwootide.mdm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.shwootide.mdm.pojo.FlowrRecord;
import com.shwootide.mdm.view.MyApplication;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.widget.Toast;

public class MyFlowBrodcastRervice extends BroadcastReceiver{
    FinalDb db;
    @Override
    public void onReceive(Context context, Intent intent) {
    	 //Toast.makeText(context, "开机", 3).show();
    	 
	 db=FinalDb.create(context);
	 long millis = System.currentTimeMillis();
         Date date=new Date(millis);
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String format = formatter.format(date);
	if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) { 
             //Toast.makeText(context, "开机", 3).show();
             FlowrRecord flows=new FlowrRecord();
             flows.setStartTime(format);
             flows.setEndTime("000000");
             db.save(flows);
        }
	if(intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
	    Log.i("guanji","关机");
	    	long g3_down_total = TrafficStats.getMobileRxBytes();
		long g3_up_total = TrafficStats.getMobileTxBytes(); 
		long wifi_down_total = TrafficStats.getTotalRxBytes()-g3_down_total; 
		long wifi_up_total = TrafficStats.getTotalTxBytes()-g3_up_total; 
		String where="EndTime="+"\""+"000000"+"\"";
		List<FlowrRecord> list = db.findAllByWhere(FlowrRecord.class, where);
		if(list.size()!=0){
		    FlowrRecord flows=list.get(0);
			flows.setEndTime(format);
			flows.setWwanReceive(g3_down_total);
			flows.setWwanSend(g3_up_total);
			flows.setWifiReceive(wifi_down_total);
			flows.setWifiSend(wifi_up_total);
			db.update(flows, where);
		}
		
	}
    }
    
}
