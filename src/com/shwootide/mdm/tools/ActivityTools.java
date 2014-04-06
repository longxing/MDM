package com.shwootide.mdm.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shwootide.mdm.R;

public class ActivityTools {
	
//	private Context context;
//	private Activity activity;
//	
//	
//	public Context getContext() {
//		return context;
//	}
//
//
//	public void setContext(Context context) {
//		this.context = context;
//	}
//
//
//	public Activity getActivity() {
//		return activity;
//	}
//
//
//	public void setActivity(Activity activity) {
//		this.activity = activity;
//	}


	public static void headView(final Activity activity,String title)
	{
		Button btn_back = (Button)activity.findViewById(R.id.btn_back);
		
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activity.onBackPressed();
			}
		});
		TextView tv_title = (TextView)activity.findViewById(R.id.tv_title);
		tv_title.setText(title);
	}

}
