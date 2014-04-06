package com.shwootide.mdm.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.shwootide.mdm.pojo.MyFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApkSearchUtils {
	
	private static int INSTALLED = 0; // 表示已经安装，且跟现在这个apk文件是一个版本
	    private static int UNINSTALLED = 1; // 表示未安装
	    private static int INSTALLED_UPDATE =2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新
	 
	    private Context context;
	    public static List<MyFile> myFiles = new ArrayList<MyFile>();
	 
	    public List<MyFile> getMyFiles() {
	        return myFiles;
	    }
	 
	    public void setMyFiles(List<MyFile> myFiles) {
	        this.myFiles = myFiles;
	    }
	 
	    public ApkSearchUtils(Context context) {
	        super();
	        this.context = context;
	    }
	    
	    /**
	         * @param args
	         *            运用递归的思想，递归去找每个目录下面的apk文件
	         */
	        public void FindAllAPKFile(File file) {
	     
	            // 手机上的文件,目前只判断SD卡上的APK文件
	            // file = Environment.getDataDirectory();
	            // SD卡上的文件目录
	            if (file.isFile()) {
	                String name_s = file.getName();
	                MyFile myFile = new MyFile();
	                String apk_path = null;
	                // MimeTypeMap.getSingleton()
	                if (name_s.toLowerCase().endsWith(".apk")) {
	                    apk_path = file.getAbsolutePath();// apk文件的绝对路劲,我们就定义为SD卡
	                    // System.out.println("----" + file.getAbsolutePath() + "" +
	                    // name_s);
	                    PackageManager pm = context.getPackageManager();
	                    PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
	                    ApplicationInfo appInfo = packageInfo.applicationInfo;
	     
	                     
	                     /**获取apk的图标 */
	                    appInfo.sourceDir = apk_path;
	                    appInfo.publicSourceDir = apk_path;
	                    Drawable apk_icon = appInfo.loadIcon(pm);
	                    myFile.setApk_icon(apk_icon);
	                    /** 得到包名 */
	                    String packageName = packageInfo.packageName;
	                    myFile.setPackageName(packageName);
	                    /** apk的绝对路劲 */
	                    myFile.setFilePath(file.getAbsolutePath());
	                    /** apk的版本名称 String */
	                    String versionName = packageInfo.versionName;
	                    myFile.setVersionName(versionName);
	                    /** apk的版本号码 int */
	                    int versionCode = packageInfo.versionCode;
	                    myFile.setVersionCode(versionCode);
	                    /**安装处理类型*/
	                    int type = doType(pm, packageName, versionCode);
	                    myFile.setInstalled(type);
	                     
	                    Log.i("ok", "处理类型:"+String.valueOf(type)+"\n" + "------------------我是纯洁的分割线-------------------");
	                    myFiles.add(myFile);
	                }
	                // String apk_app = name_s.substring(name_s.lastIndexOf("."));
	            } else {
	                File[] files = file.listFiles();
	                if (files != null && files.length > 0) {
	                    for (File file_str : files) {
	                        FindAllAPKFile(file_str);
	                    }
	                }
	            }
	        }
	     
	        /*
	         * 判断该应用是否在手机上已经安装过，有以下集中情况出现
	         * 1.未安装，这个时候按钮应该是“安装”点击按钮进行安装
	         * 2.已安装，按钮显示“已安装” 可以卸载该应用
	         * 3.已安装，但是版本有更新，按钮显示“更新” 点击按钮就安装应用
	         */
	         
	        /**
	         * 判断该应用在手机中的安装情况
	         * @param pm                   PackageManager 
	         * @param packageName  要判断应用的包名
	         * @param versionCode     要判断应用的版本号
	         */
	        public int doType(PackageManager pm, String packageName, int versionCode) {
	            List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
	            for (PackageInfo pi : pakageinfos) {
	                String pi_packageName = pi.packageName;
	                int pi_versionCode = pi.versionCode;
	                //如果这个包名在系统已经安装过的应用中存在
	                if(packageName.endsWith(pi_packageName)){
	                    //Log.i("test","此应用安装过了");
	                    if(versionCode==pi_versionCode){
	                        Log.i("test","已经安装，不用更新，可以卸载该应用");
	                        return INSTALLED;
	                    }else if(versionCode>pi_versionCode){
	                        Log.i("test","已经安装，有更新");  
	                        return INSTALLED_UPDATE;
	                    }
	                }
	            }
	            Log.i("test","未安装该应用，可以安装");   
	            return UNINSTALLED;
	        }



}
