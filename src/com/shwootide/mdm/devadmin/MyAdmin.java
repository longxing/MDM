package com.shwootide.mdm.devadmin;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyAdmin extends DeviceAdminReceiver {
	
	  @Override

	    public void onReceive(Context context, Intent intent) {

	        String action = intent.getAction();

	        if (ACTION_PASSWORD_CHANGED.equals(action)) {

	            onPasswordChanged(context, intent);

	        } else if (ACTION_PASSWORD_FAILED.equals(action)) {

	            onPasswordFailed(context, intent);

	        } else if (ACTION_PASSWORD_SUCCEEDED.equals(action)) {

	            onPasswordSucceeded(context, intent);

	        } else if (ACTION_DEVICE_ADMIN_ENABLED.equals(action)) {

	            onEnabled(context, intent);

	        } else if (ACTION_DEVICE_ADMIN_DISABLE_REQUESTED.equals(action)) {

	            CharSequence res = onDisableRequested(context, intent);

	            if (res != null) {

	                Bundle extras = getResultExtras(true);

	                extras.putCharSequence(EXTRA_DISABLE_WARNING, res);

	            }
                                                                                                                       
	        } else if (ACTION_DEVICE_ADMIN_DISABLED.equals(action)) {
	            onDisabled(context, intent); 
	            
	        }
	

}
	 
}
