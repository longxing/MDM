package com.shwootide.mdm.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.util.Log;

/**
 * Java实现文件复制、剪切、删除操作
 * 文件指文件或文件夹
 * 文件分割符统一用"\\"
 */

public class FileUtils {
	private static final  String TAG = "FileUtils";
	
	 /**
	     * 复制文件或文件夹
	     * @param srcPath 源文件或源文件夹的路径
	     * @param destDir 目标文件所在的目录
	     * @return
	     */
	    public static boolean copyGeneralFile(String srcPath, String destDir) {
	        boolean flag = false;
	        File file = new File(srcPath);
	        if(!file.exists()) {    // 源文件或源文件夹不存在
	            return false;
	        }
	         
	        if(file.isFile()) {    // 文件复制
	            flag = copyFile(srcPath, destDir);
	        }
	        else if(file.isDirectory()) {   // 文件夹复制
	            flag = copyDirectory(srcPath, destDir);
	        }
	          
	        return flag;
	    }
	     
	    /**
	     * 默认的复制文件方法，默认会覆盖目标文件夹下的同名文件
	     * @param srcPath
	     *            源文件绝对路径
	     * @param destDir
	     *            目标文件所在目录
	     * @return boolean
	     */
	    public static boolean copyFile(String srcPath, String destDir) {
	        return copyFile(srcPath, destDir, true/**overwriteExistFile*/); // 默认覆盖同名文件
	    }
	     
	    /**
	     * 默认的复制文件夹方法，默认会覆盖目标文件夹下的同名文件夹
	     * @param srcPath    源文件夹路径
	     * @param destPath    目标文件夹所在目录
	     * @return boolean
	     */
	    public static boolean copyDirectory(String srcPath, String destDir) {
	        return copyDirectory(srcPath, destDir, true/**overwriteExistDir*/);
	    }
	     
	    /**
	     * 复制文件到目标目录
	     * @param srcPath
	     *            源文件绝对路径
	     * @param destDir
	     *            目标文件所在目录
	     * @param overwriteExistFile
	     *            是否覆盖目标目录下的同名文件
	     * @return boolean
	     */
	    public static boolean copyFile(String srcPath, String destDir, boolean overwriteExistFile) {
	        boolean flag = false;
	  
	        File srcFile = new File(srcPath);
	        if (!srcFile.exists() || !srcFile.isFile()) { // 源文件不存在
	            return false;
	        }
	         
	        //获取待复制文件的文件名
	        String fileName = srcFile.getName();
	        String destPath = destDir + File.separator +fileName;
	        File destFile = new File(destPath);
	        if (destFile.getAbsolutePath().equals(srcFile.getAbsolutePath())) { // 源文件路径和目标文件路径重复
	            return false;
	        }
	        if(destFile.exists() && !overwriteExistFile) {    // 目标目录下已有同名文件且不允许覆盖
	            return false;
	        }
	         
	        File destFileDir = new File(destDir);
	        if(!destFileDir.exists() && !destFileDir.mkdirs()) {    // 目录不存在并且创建目录失败直接返回
	            return false;
	        }
	         
	        try {
	            FileInputStream fis = new FileInputStream(srcPath);
	            FileOutputStream fos = new FileOutputStream(destFile);
	            byte[] buf = new byte[1024];
	            int c;
	            while ((c = fis.read(buf)) != -1) {
	                fos.write(buf, 0, c);
	            }
	            fos.flush();
	            fis.close();
	            fos.close();
	  
	            flag = true;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	  
	        return flag;
	    }
	      
	    /**
	     *
	     * @param srcPath    源文件夹路径
	     * @param destPath    目标文件夹所在目录
	     * @return
	     */
	    public static boolean copyDirectory(String srcPath, String destDir, boolean overwriteExistDir) {
	        if(destDir.contains(srcPath))
	           return false;
	 
	        boolean flag = false;
	  
	        File srcFile = new File(srcPath);
	        if (!srcFile.exists() || !srcFile.isDirectory()) { // 源文件夹不存在
	            return false;
	        }
	         
	        //获得待复制的文件夹的名字，比如待复制的文件夹为"E:\\dir\\"则获取的名字为"dir"
	        String dirName = srcFile.getName();
	         
	        //目标文件夹的完整路径
	        String destDirPath = destDir + File.separator + dirName + File.separator;
	        File destDirFile = new File(destDirPath);
	        if(destDirFile.getAbsolutePath().equals(srcFile.getAbsolutePath())) {
	            return false;
	        }
	        if(destDirFile.exists() && destDirFile.isDirectory() && !overwriteExistDir) {    // 目标位置有一个同名文件夹且不允许覆盖同名文件夹，则直接返回false
	            return false;
	        }
	         
	        if(!destDirFile.exists() && !destDirFile.mkdirs()) {  // 如果目标目录不存在并且创建目录失败
	            return false;
	        }
	         
	        File[] fileList = srcFile.listFiles();    //获取源文件夹下的子文件和子文件夹
	        if(fileList.length==0) {    // 如果源文件夹为空目录则直接设置flag为true，这一步非常隐蔽，debug了很久
	            flag = true;
	        }
	        else {
	            for(File temp: fileList) {
	                if(temp.isFile()) {    // 文件
	                    flag = copyFile(temp.getAbsolutePath(), destDirPath, overwriteExistDir);        // 递归复制时也继承覆盖属性
	                }
	                else if(temp.isDirectory()) {    // 文件夹
	                    flag = copyDirectory(temp.getAbsolutePath(), destDirPath, overwriteExistDir);   // 递归复制时也继承覆盖属性
	                }
	                 
	                if(!flag) {
	                    break;
	                }
	            }
	        }
	          
	        return flag;
	    }
	  
	    /**
	     * 删除文件或文件夹
	     * @param path
	     *            待删除的文件的绝对路径
	     * @return boolean
	     */
	    public static boolean deleteFile(String path) {
	        boolean flag = false;
	  
	        File file = new File(path);
	        if (!file.exists()) { // 文件不存在直接返回
	            return flag;
	        }
	 
	        flag = file.delete();
	  
	        return flag;
	    }
	  
	     
	    /**
	     * 由上面方法延伸出剪切方法：复制+删除
	     * @param  destDir 同上
	     */
	    public static boolean cutGeneralFile(String srcPath, String destDir) {
	        boolean flag = false;
	        if(copyGeneralFile(srcPath, destDir) && deleteFile(srcPath)) { // 复制和删除都成功
	            flag = true;
	        }
	         
	        return flag;
	    }
	  
	    public static void main(String[] args) {
	        /** 测试复制文件 */
	        System.out.println(copyGeneralFile("d://test/test.html", "d://test/test/"));  // 一般正常场景
	        System.out.println(copyGeneralFile("d://notexistfile", "d://test/"));        // 复制不存在的文件或文件夹
	        System.out.println(copyGeneralFile("d://test/test.html", "d://test/"));      // 待复制文件与目标文件在同一目录下
	        System.out.println(copyGeneralFile("d://test/test.html", "d://test/test/"));  // 覆盖目标目录下的同名文件
	        System.out.println(copyFile("d://test/", "d://test2", false));  // 不覆盖目标目录下的同名文件
	        System.out.println(copyGeneralFile("d://test/test.html", "notexist://noexistdir/")); // 复制文件到一个不可能存在也不可能创建的目录下
	         
	        System.out.println("---------");
	         
	        /** 测试复制文件夹 */
	        System.out.println(copyGeneralFile("d://test/", "d://test2/"));
	         
	        System.out.println("---------");
	         
	        /** 测试删除文件 */
	        System.out.println(deleteFile("d://a/"));
	    }
	    
	    
	    /**
	         * @param path
	         * @param fileList
	         * 注意的是并不是所有的文件夹都可以进行读取的，权限问题
	         */
	        private static void getApkFileList(File path, HashMap<String, String> fileList){
	            //如果是文件夹的话
	            if(path.isDirectory()){
	                //返回文件夹中有的数据
	                File[] files = path.listFiles();
	                //先判断下有没有权限，如果没有权限的话，就不执行了
	                if(null == files)
	                    return;
	                 
	                for(int i = 0; i < files.length; i++){
	                	getApkFileList(files[i], fileList);
	                }
	            }
	            //如果是文件的话直接加入
	            else{
	                Log.i(TAG, path.getAbsolutePath());
	                //进行文件的处理
	                String filePath = path.getAbsolutePath();
	                if(filePath.endsWith(".apk"))
	                {
	                	//文件名
		                String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
		                //添加
		                fileList.put(fileName, filePath);
	                }
	               
	            }
	        }
	        
	        public static HashMap<String, String> getApkListOnSDcard(File file) {
	        	        //从根目录开始扫描
	        	        Log.i(TAG, file.getPath());
	        	        HashMap<String, String> fileList = new HashMap<String, String>();
	        	        getApkFileList(file, fileList);
	        	        return fileList;
	        	    }




}
