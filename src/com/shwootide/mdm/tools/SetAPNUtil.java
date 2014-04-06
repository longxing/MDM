package com.shwootide.mdm.tools;

import com.shwootide.mdm.pojo.APN;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;



public class SetAPNUtil {
    private Context context;
    private APN apn;
    	    public SetAPNUtil(Context context,APN apn) {
    			this.context=context;
    			this.apn=apn;
	// TODO Auto-generated constructor stub
    }
    	    public void setAPN(){
    		 //checkAPN();
    	        SetAPN(addAPN()); 
    	    }
	    private static final String TAG = "MainActivity"; 
    	 
    	    public static final Uri APN_URI = Uri.parse("content://telephony/carriers"); 
    	 
    	    // public static final Uri PREFERRED_APN_URI
    	    // =Uri.parse("PREFERRED_APN_URI  PREFERRED_APN_URI ");  
    	    public static final Uri CURRENT_APN_URI = Uri 
    	            .parse("content://telephony/carriers/preferapn"); 
    	 
    	 
    	 
    	    public void checkAPN() {
    	        // 检查当前连接的APN  
    	        Cursor cr = context.getContentResolver().query(APN_URI, null, null, null, null); 
    	 
    	      //  Log.d(TAG, "cr" + cr); 
    	        while (cr != null && cr.moveToNext()) {
    	 
    	            // if(cr.getString(cr.getColumnIndex("_id")))  
    	 
    	            // APN id  
    	            String id = cr.getString(cr.getColumnIndex("_id")); 
    	             
    	           // Log.d(TAG, "id" + id); 
    	             
//    	          String apn_id= cr.getString(cr.getColumnIndex("apn_id"));  
    	//  
//    	          Log.d(TAG, "apn_id" + apn_id);  
    	            // APN name  
    	            String apn = cr.getString(cr.getColumnIndex("apn")); 
    	 
    	            
    	            if(apn.equals("cmnet")){
    	        	Log.d(TAG, apn);
    	            }
    	            // Toast.makeText(getApplicationContext(),  
    	            // "当前 id:" + id + " apn:" + apn, Toast.LENGTH_LONG).show();  
    	        } 
    	 
    	    } 
    	 
    	    // 新增一个cmnet接入点  
    	    public int addAPN() { 
    	        int id = -1; 
    	 
    	        Log.d(TAG, "添加一个新的apn"); 
    	 
    	        String NUMERIC = getSIMInfo(); 
    	 
    	        Log.d(TAG, "NUMERIC" + NUMERIC); 
    	 
    	        if (NUMERIC == null) { 
    	 
    	            return -1; 
    	        } 
    	 
    	        ContentResolver resolver = context.getContentResolver(); 
    	        ContentValues values = new ContentValues(); 
    	 
    	        //SIMCardInfo siminfo = new SIMCardInfo(context); 
    	        // String user = siminfo.getNativePhoneNumber().substring(start);  
    	 
    	        values.put("name", "后台设置APN"); // apn中文描述  
    	        values.put("apn", apn.getApn()); // apn名称  
    	        values.put("type", "default,supl"); 
    	        values.put("numeric", NUMERIC); 
    	        values.put("mcc", NUMERIC.substring(0, 3)); 
    	        values.put("mnc", NUMERIC.substring(3, NUMERIC.length())); 
    	        values.put("proxy",apn.getProxy()); 
    	        values.put("port",apn.getProxyPort()); 
    	        values.put("mmsproxy", ""); 
    	        values.put("mmsport", ""); 
    	        values.put("user",apn.getUsername()); 
    	        values.put("server",""); 
    	        values.put("password",apn.getPassword()); 
    	        values.put("mmsc", ""); 
    	 
    	        Cursor c = null; 
    	        Uri newRow = resolver.insert(APN_URI, values); 
    	        if (newRow != null) { 
    	            c = resolver.query(newRow, null, null, null, null); 
    	            int idIndex = c.getColumnIndex("_id"); 
    	            c.moveToFirst(); 
    	            id = c.getShort(idIndex); 
    	        } 
    	        if (c != null) 
    	            c.close(); 
    	        return id; 
    	    } 
    	 
    	    protected String getSIMInfo() { 
    	        TelephonyManager iPhoneManager = (TelephonyManager) context 
    	                .getSystemService(Context.TELEPHONY_SERVICE); 
    	        return iPhoneManager.getSimOperator(); 
    	    } 
    	 
    	    // 设置接入点  
    	    public void SetAPN(int id) {
    	        ContentResolver resolver = context.getContentResolver(); 
    	        ContentValues values = new ContentValues(); 
    	        values.put("apn_id", id); 
    	        resolver.update(CURRENT_APN_URI, values, null, null);
    	 
    	        // resolver.delete(url, where, selectionArgs)  
    	    } 
    	 
}
