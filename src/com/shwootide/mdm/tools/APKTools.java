package com.shwootide.mdm.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class APKTools {


	public static boolean isAppInstalled(Context context,String uri) {
		PackageManager pm = context.getPackageManager();
		boolean installed =false;
		try {
			pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
			installed =true;
		} catch(PackageManager.NameNotFoundException e) {
			installed =false;
		}
		return installed;
	}
	
	/** 
     * uninstall apk file 
     * @param packageName  
     */  
    public static void uninstallAPK(Context context,String packageName){
        Uri uri=Uri.parse("package:"+packageName); 
        Intent intent=new Intent(Intent.ACTION_DELETE,uri);  
        context.startActivity(intent);
    }  
}
