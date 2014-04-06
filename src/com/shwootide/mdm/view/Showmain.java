package com.shwootide.mdm.view;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.shwootide.mdm.R;
import com.shwootide.mdm.pojo.FlowrRecord;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;
import com.shwootide.mdm.tools.Tools;

public class Showmain extends AbActivity {
    TextView tv_one;
    TextView tv_two;
    TextView tv_three;
    TextView tv_four;
    TextView tv_five;
    long WwanReceive = 0;
	 long wwanSend = 0;
	 long wifiReceive = 0;
	 long wifiSend = 0;
	 long flowtotal=0;
    FinalDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	ExitApplication.getInstance().addActivity(this);
	setContentView(R.layout.flow_main);
	ActivityTools.headView(Showmain.this, "Á÷Á¿ÏêÇé");
	db=FinalDb.create(this);

	initView();
    }

    public void initView() {
	tv_one = (TextView) findViewById(R.id.flow_tv_one);
	tv_two = (TextView) findViewById(R.id.flow_tv_two);
	tv_three = (TextView) findViewById(R.id.flow_tv_three);
	tv_four = (TextView) findViewById(R.id.flow_tv_four);
	tv_five = (TextView) findViewById(R.id.flow_tv_five);
	List<FlowrRecord> list = db.findAll(FlowrRecord.class);
	
		for(int i=0;list.size()>i;i++){
		    long g_down=list.get(i).getWwanReceive();
		    long g_up= list.get(i).getWwanSend();
		    long wifi_down = list.get(i).getWifiReceive();
		    long wifi_up = list.get(i).getWifiSend();
		    Log.i("Showmain",g_down+"");
		    Log.i("Showmain",g_up+"");
		    Log.i("Showmain",wifi_down+"");
		    Log.i("Showmain",wifi_up+"");
		    WwanReceive=WwanReceive+g_down;
		    wwanSend=wwanSend+g_up;
		    wifiReceive=wifiReceive+wifi_down;
		    wifiSend=wifiSend+wifi_up;
		};
		flowtotal=WwanReceive+wifiReceive+wifiSend+flowtotal;
		tv_one.setText(Tools.bytes2kb(WwanReceive));
		tv_two.setText(Tools.bytes2kb(wwanSend));
		tv_three.setText(Tools.bytes2kb(wifiReceive));
		tv_four.setText(Tools.bytes2kb(wifiSend));
		tv_five.setText(Tools.bytes2kb(flowtotal));
		
    }
    
}
