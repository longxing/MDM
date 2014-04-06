package com.shwootide.mdm.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kobjects.base64.Base64;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shwootide.mdm.R;
import com.shwootide.mdm.common.Configuration;

public class CommonUtil {
	public static String versionName = "1.0";
	public static boolean initFlag = false;
	static String TAG = "CommonUtil";
	private static String timeFormat = "yyyy-MM-dd HH:mm:ss";

	/*��ȡ��ǰ������IP��ַ*/
	public static String getLocalIpAddress() {   
	    try {   
	        for (Enumeration<NetworkInterface> en = NetworkInterface   
	                .getNetworkInterfaces(); en.hasMoreElements();) {   
	            NetworkInterface intf = en.nextElement();   
	            for (Enumeration<InetAddress> enumIpAddr = intf   
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {   
	                InetAddress inetAddress = enumIpAddr.nextElement();   
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();   
	                    
	                }   
	            }   
	        }   
	    } catch (SocketException ex) {   
	        Log.e("WifiPreference IpAddress", ex.toString());   
	    }   
	    return null;   
	}   
	//��֤���������Ƿ���ȷ 
	public static boolean isEmail(String strEmail)
	{
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);   
		Matcher m = p.matcher(strEmail);   
		return m.matches();  
	}
	 
	//��֤�ֻ���Ƿ���ȷ 
	public static boolean isMobileNo(String strMobile)
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        Matcher m = p.matcher(strMobile);
        return m.matches();
	}
	/** 
	* ֱ��ͨ��HTTPЭ���ύ��ݵ�������,ʵ�ֱ?�ύ���� 
	* @param actionUrl �ϴ�·�� 
	* @param params ������� keyΪ������,valueΪ����ֵ 
	* @param filename �ϴ��ļ� 
	* @param username �û��� 
	* @param password ���� 
	 * @return 
	*/ 
	public static int postImg(String actionUrl,Map<String, String> params, String  filename) { 
	int retCode = 0;
		try { 
	String BOUNDARY = "--------------et567z"; //��ݷָ��� 
	String MULTIPART_FORM_DATA = "Multipart/form-data";  

	URL url = new URL(actionUrl); 
	HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 

	conn.setDoInput(true);//�������� 
	conn.setDoOutput(true);//������� 
	conn.setUseCaches(false);//��ʹ��Cache 
	conn.setRequestMethod("POST"); 
	conn.setRequestProperty("Connection", "Keep-Alive"); 
	conn.setRequestProperty("Charset", "UTF-8"); 
	conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY); 
	//String usernamePassword=username+":"+password; 
	//conn.setRequestProperty("Authorization","Basic "+new String(Base64Coder.encode(usernamePassword.getBytes()))); 

	StringBuilder sb = new StringBuilder();  

	//�ϴ��ı?����֣���ʽ��ο����� 
	for (Map.Entry<String, String> entry : params.entrySet()) {//�����?�ֶ����� 
	sb.append("--"); 
	sb.append(BOUNDARY); 
	sb.append("\r\n"); 
	sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n"); 
	sb.append(entry.getValue()); 
	sb.append("\r\n"); 
	} 
//	            System.out.println(sb.toString()); 
	DataOutputStream outStream = new DataOutputStream(conn.getOutputStream()); 
	outStream.write(sb.toString().getBytes());//���ͱ?�ֶ���� 
	byte[] content = readFileImage(filename); 
	//�ϴ����ļ����֣���ʽ��ο����� 
	//System.out.println("content:"+content.toString()); 
	StringBuilder split = new StringBuilder(); 
	split.append("--"); 
	split.append(BOUNDARY); 
	split.append("\r\n"); 
	split.append("Content-Disposition: form-data;name=\"pic\";filename=\"temp.jpg\"\r\n"); 
	split.append("Content-Type: image/jpg\r\n\r\n"); 
	System.out.println(split.toString()); 
	outStream.write(split.toString().getBytes()); 
	outStream.write(content, 0, content.length); 
	outStream.write("\r\n".getBytes());  

	byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();//��ݽ����־ 
	outStream.write(end_data); 
	outStream.flush(); 
	int cah = conn.getResponseCode(); 
//	            if (cah != 200) throw new RuntimeException("����urlʧ��:"+cah); 
	retCode = cah;
	if(cah == 200)//����ɹ�����ʾ�ɹ� 
	{ 
		//Log.d("CommonUtil getResponseCode", conn.getResponseMessage());
	}else{ 
	throw new RuntimeException("����urlʧ��:"+cah); 
	} 

//	            InputStream is = conn.getInputStream(); 
//	            int ch; 
//	            StringBuilder b = new StringBuilder(); 
//	            while( (ch = is.read()) != -1 ){ 
//	                b.append((char)ch); 
//	            } 
	outStream.close(); 
	conn.disconnect(); 
	} 
	catch (IOException e) 
	{ 
	e.printStackTrace(); 

	} 
	catch (Exception e) 
	{ 
	e.printStackTrace();  

	}  
		return retCode;

	} 
	  public static byte[] readFileImage(String filename) throws IOException { 
		  BufferedInputStream bufferedInputStream = new BufferedInputStream( 
		  new FileInputStream(filename)); 
		  int len = bufferedInputStream.available(); 
		  byte[] bytes = new byte[len]; 
		  int r = bufferedInputStream.read(bytes); 
		  if (len != r) { 
		  bytes = null; 
		  throw new IOException("��ȡ�ļ�����ȷ"); 
		  } 
		  bufferedInputStream.close(); 
		  return bytes; 
		  } 
	  //�����ļ�����·��
	  public static boolean CreateFile(String filePath)
	  {
		  File f;
		  try{
			  f = new File(filePath);
			  if(!f.exists())
			  {
				  f.createNewFile();
			  }
		  }catch(Exception e)
		  {
			  return false;
		  }
		  return true;
	  }
	  
	  public static Bitmap getPicFromBytes(byte[] bytes,

				BitmapFactory.Options opts) {


				if (bytes != null)

				if (opts != null)

				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,

				opts);

				else

				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

				return null;

				}
		
		public static byte[] getBytesFromInputStream(InputStream is, int bufsiz)
				throws IOException {

				int total = 0;

				byte[] bytes = new byte[4096];

				ByteBuffer bb = ByteBuffer.allocate(bufsiz);


				while (true) {

				int read = is.read(bytes);

				if (read == -1)

				break;

				bb.put(bytes, 0, read);

				total += read;

				}


				byte[] content = new byte[total];

				bb.flip();

				bb.get(content, 0, total);


				return content;

				}
		/**
		 * ��ʹ�����ٲ��õ�ʱ��Ҳ�ܼ���ͼƬ. �����Ϊһ��ͨ�õ�
		 * ��չ:��Ϊ����Ƚ���,Ϊ���û����Ѻ�,����ʹ���߳�Handle,�����
		 * @param c
		 * @param url
		 * @return
		 */
		public static synchronized Bitmap getBitMap(Context c, String url) {
			URL myFileUrl = null;
			Bitmap bitmap = null;
			try {
				myFileUrl = new URL(url);
			} catch (MalformedURLException e) {
				bitmap = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.default_pic);  //�����������쳣��,���Ĭ��ͼƬ
				return bitmap;
			}
			try {
				//����������
				HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();     //�ѵõ�������ת������
				int length = (int) conn.getContentLength();   //��ȡ�ļ��ĳ���
				if (length != -1) {
					byte[] imgData = new byte[length];          
					byte[] temp = new byte[512];
					int readLen = 0;
					int destPos = 0;
					while ((readLen = is.read(temp)) > 0) {
						System.arraycopy(temp, 0, imgData, destPos, readLen);
						destPos += readLen;
					}

					bitmap = BitmapFactory.decodeByteArray(imgData, 0,
							imgData.length);    
				}

			} catch (IOException e) {
				bitmap = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.default_pic);
				return bitmap;
			}

			return bitmap;
		}
		
		
		//��·���õ�ͼƬ
		public Bitmap GetBitmap(String url)
		{
			URL tempFileUrl = null;
			Bitmap bitmap = null;
			try
			{
				tempFileUrl = new URL(url);
			}catch(MalformedURLException e)
			{
				e.printStackTrace();
			}
			try{
				HttpURLConnection conn = (HttpURLConnection)tempFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return bitmap;
		}
		

		/**@�ж��Ƿ���ڸ����͵�ͼƬ
		 * @param fileName
		 * @return boolean
		 */
		private static boolean validate(String fileName) {
			int idx = fileName.indexOf(".");
			String subfix = fileName.substring(idx + 1);
			if (fileName.equals("")) {
				return false;
			}
			// subfix.equals()||subfix.equals(".png")||subfix.equals(".jpeg")
			if ("jpg".equals(subfix)||"png".equals(subfix)||"gif".equals(subfix)) {
				return true;
			} else {
				return false;
			}
		}
		/*
		 * �����ֵΪ�գ��ͷ���δ֪�����ֵ��Ϊ�գ��ͷ���ԭ����ֵ
		 */
		public static String getValue(String Value)
		{
//			return (Value == null||Value.isEmpty())?"δ֪":Value;
			return (Value == null||CommonUtil.isEmpty(Value))?"δ֪":Value;
//			String Import = "δ֪";
//			if(Value!=null)
//			{
//				Import = (String.valueOf(edi.getImport())=="0")?"���":"���";
//			}
		}
		//��ȡʱ��֮�������
		public static double getSubDays(String fromDatetime,String toDatetime)
		{
			double result = 0;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				 result = (df.parse(toDatetime).getTime() - df.parse(fromDatetime).getTime())/3600000/24;
				 BigDecimal   b   =   new   BigDecimal(result);  
				 result = b.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();  
			}
			catch(ParseException e)
			{
				result = 0;
				e.printStackTrace();
			}
			return result;
		}
		
		public static String ParseTimeFromMilliSeconds(String format,String value)
		{
			
			//value = value.substring(value.indexOf("(")+1,value.indexOf("+")); //"yyyy-MM-dd HH:MM:SS"
//			SimpleDateFormat formatter = new SimpleDateFormat(format);
//			Calendar c = Calendar.getInstance();
//			Long now=new Long(Long.parseLong(value));
//			c.setTimeInMillis(now);
//			value = formatter.format(c.getTime());
//			return value;
//			SimpleDateFormat formatter = new SimpleDateFormat(format);
//			Calendar c = Calendar.getInstance();
			
//			Long now=new Long(Long.parseLong(value));
//			c.setTimeInMillis(now);
			value = value.toLowerCase();
			if(value.split("t").length>1)
			{
				String value1 = value.split("t")[0]+" ";
				String value2 = value.split("t")[1];
				if(format.equals("yyyy-MM-dd"))
					return value1;
				if(format.equals("yyyy-MM-dd hh:mm")&&value2.contains("."))
					return value1+value2.substring(0,value2.lastIndexOf(":"));
				if(value2.contains("."))
					value2 = value2.substring(0,value2.indexOf("."));
				return value1+value2;
			}
			else
				return value;
			//return value;
		}
		
		//��ȡ����İ汾��
		public static int getVerCode(Context context)
		{
			int verCode = -1;
			try{
				verCode = context.getPackageManager().getPackageInfo("com.mypatroller",0).versionCode;
				versionName = context.getPackageManager().getPackageInfo("com.mypatroller",0).versionName;
			}catch(NameNotFoundException e)
			{
				e.printStackTrace();
			}
			return verCode;
		}
		//��ȡ����汾������
		public static String getVerName(Context context)
		{
			String verName = "";
			try{
				verName = context.getPackageManager().getPackageInfo(  
	                    "com.mypatroller", 0).versionName;  
			}catch(NameNotFoundException e)
			{
				e.printStackTrace();
			}
			return verName;
		}
		//����apk�����
		public static String getAppName(Context context)
		{
			String verName = context.getResources().getText(R.string.app_name).toString();
			return verName;
		}
		/**
		 * ��ȡ��ǰʱ��
		 * @return
		 */
//		public static String getCurrentDateTime()
//		{
//			SimpleDateFormat   sDateFormat2   =   new   SimpleDateFormat(Configuration.DATETIMEFORMAT);   
//			String   currenttime   =   sDateFormat2.format(new   java.util.Date());
//			return currenttime;
//		}
		/**
		 *  * @param begin ��ʼʱ��
         * @param end  ��ǰʱ��
         * @param dateFormate ʱ���ʽ
         * @return  ����ʱ���֮��Ĳ��λСʱ
		 */
		//����ʱ��֮��
		public static long getDifferDays(String begin,String end)
		{
			long hours = 0 ;
			SimpleDateFormat dateFormate =   new   SimpleDateFormat(Configuration.DATETIMEFORMAT); 
			try{
				Date date1 = dateFormate.parse(begin);
				Date date2 = dateFormate.parse(end);
				long diff = date2.getTime() - date1.getTime();
				hours = diff/(1000*60*60);
			}catch(ParseException e)
			{
				e.printStackTrace();
			}
			Log.d("differ hours", String.valueOf(hours));
			return hours;
		}
		/**
		 * ��ȡ�ļ�·���������ļ�
		 * @param path �ļ�·��
		 * @param fileList �ļ��б���Ϣ
		 */
		public static void getList(File path,List<String> fileList)
		{
			try{
			if(path.isDirectory())
			{
				File[] files = path.listFiles();
				if(null == files)
					return;
				for(int i =0;i<files.length;i++)
					getList(files[i],fileList);
			}else{
				String filePath = path.getAbsolutePath();
				fileList.add(filePath);
//				String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
//				fileList.put(fileName, filePath);
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * ����ͼƬ��·��ɾ��ͼƬ
		 * @param filePath
		 */
		public static void deleteFile(String filePath)
		{
			try{
				File delFile = new File(filePath);
				if(delFile.exists())
					delFile.delete();
			}
			catch(Exception e){
				e.printStackTrace();
				
			}
		}
		/*
		 * �ж��ַ��Ƿ�Ϊ�� �������Ƿ�Ϊnull
		 */
		public static boolean isEmpty(String str)
		{
			return str.trim().equals("");
		}
		private static long lastClickTime;  
	    public static boolean isFastDoubleClick() {  
	        long time = System.currentTimeMillis();  
	        long timeD = time - lastClickTime;  
	        if ( 0 < timeD && timeD < 5*1000) {   //10S  
	            return true;     
	        }     
	        lastClickTime = time;     
	        return false;     
	    }  
	    public static void myToast(Context context,String str)
	    {
	    	Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	    }
	    /**
	     * ��ô�ж��Ƿ��������ݣ���BaseDao�м���һ�������жϱ�Ҫ����ݱ��Ƿ���Ϊ�յģ����Ϊ�յĻ�����˵���ϴ�û�����سɹ�����ȥ�������أ����Ϊ�գ��Ͳ���ִ�����غ���
	     * ��ôȥ��֤������سɹ������ɹ�����ʹ�û�����ʹ�ã����ǣ�
	     * ����������ȡ������ݣ��͵�����ȡ���
	     */
	    public static void DownLoadData(Context c)
	    {
	    	/**
			 * ���޺ͱ�������״̬��һֱ����仯�����Բ���Ҫ�����ر��棬����Ļ�������û�а취���ˡ��ڱ�Ҫ��ʱ�������ر��棬��д�ô��룬������ʵ�������£���Ҫ���߲�������Ƚ϶��ʱ�򣬲Ż�����ʵʩ������
	    	//����ͼ��޹����Ȳ����� "F0904","F0905",
	    	 * String[] arrTableNames= new String[]{"F0904,F0905","F090103,F090104,F090501,F090502","F090401,F090406,F0902","F090202,F0101","F000601,F0006","F0201,F0901"};
	    	 * */    	
//	    	SharedPreferences sp = c.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	String companyname = sp.getString(Configuration.ECNAME, "");
//	    	String employeenuber = sp.getString(Configuration.USERID, "");
//	    	String[] arrTNs = new String[]{"F090103","F090104","F090501","F090502","F090401","F090406","F0902","F090202","F0101","F000601","F0006","F0201","F0901","F090106","F990101","F990201","F9901"};
////	    	boolean isCompleteDownLoad = true;
////	    	BaseDao<F0101Class> bdDownLoad = new BaseDao<F0101Class>(c);
////	    	isCompleteDownLoad = bdDownLoad.isCompleteDownLoad(arrTNs);
////	    	bdDownLoad.close();
////	    	if(!isCompleteDownLoad)
////	    	{
//		    	String[] arrTableNames= new String[]{"F090103","F090104","F090501,F090502","F090401,F090406,F0902","F090202,F0101","F000601,F0006","F0901","F0201,F090106","F990101,F990201,F9901"};
//		//    	String tablenames = "F0902,F0101";
//		    	for(String tablenames:arrTableNames)
//		    	{
//		    	HashMap<String,String> hmParam = new HashMap<String,String>();
//		    	hmParam.put("companyname", companyname);
//		    	hmParam.put("tablenames", tablenames);
//		    	hmParam.put("userid", employeenuber);
//		    	String ServiceName = "SyncService";
//		    	String Json = WebService.getRemoteInfo(c,"FirstLoadLocalTables", ServiceName, hmParam);
//		    	Gson gson = new Gson();
//		    	ContentValues cvs = new ContentValues();
//		    	String[] arrItemDatas =Json.split("\\$"); 
//		    	for(String strItemData:arrItemDatas)
//		    	{
//		    		BaseDao<F0101Class> bd = null;
//		    		try {
//		    			 bd = new BaseDao<F0101Class>(c);
//		    			bd.beginTransaction();
//			    		String tablename = strItemData.substring(0, strItemData.indexOf(":["));
//			    		String content = strItemData.substring(strItemData.indexOf(":[")+1);
//			    		if(content.indexOf("]")-content.indexOf("[")>1)
//			    		{
//			    			bd.delete(tablename, "", new String[]{}); //��ɾ����ݣ������ظ������
//			    			if(tablename.equals("F0101"))
//			    			{
//			    				List<F0101Class> lstF0101 = gson.fromJson(content,new TypeToken<List<F0101Class>>(){}.getType());
//			    				for(F0101Class f0101c:lstF0101)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0101c.getInnerId());
//			    					cvs.put("EmployeeNumber", f0101c.getEmployeeNumber());
//			    					cvs.put("EmployeeName", f0101c.getEmployeeName());
//			    					cvs.put("MobilePhone", f0101c.getMobilePhone());
//			    					cvs.put("Status", f0101c.getStatus());
//			    					bd.insert("F0101", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090103"))
//			    			{
//			    				List<F090103Class> lstF090103 = gson.fromJson(content,new TypeToken<List<F090103Class>>(){}.getType());
//			    				for(F090103Class f090103c:lstF090103)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090103c.getInnerId());
//			    					cvs.put("ElevatorType", f090103c.getElevatorType());
//			    					cvs.put("ElevatorTypeNumber", f090103c.getElevatorTypeNumber());
//			    					cvs.put("Note", f090103c.getNote());
//			    					bd.insert("F090103", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090104"))
//			    			{
//			    				List<F090104Class> lstF090104 = gson.fromJson(content,new TypeToken<List<F090104Class>>(){}.getType());
//			    				for(F090104Class f090104c:lstF090104)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090104c.getInnerId());
//			    					cvs.put("FaultReasonNumber", f090104c.getFaultReasonNumber());
//			    					cvs.put("FaultReason", f090104c.getFaultReason());
//			    					cvs.put("Note", f090104c.getNote());
//			    					cvs.put("ElevatorTypeNumber",f090104c.getElevatorTypeNumber());
//			    					bd.insert("F090104", cvs);
//			    				}    				   	    	
//			    			}
//			    			
//			    			if(tablename.equals("F090501"))
//			    			{
//			    				List<F090501Class> lstF090501 = gson.fromJson(content,new TypeToken<List<F090501Class>>(){}.getType());
//			    				for(F090501Class f090501c:lstF090501)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090501c.getInnerId());
//			    					cvs.put("MaintainTypeNumber", f090501c.getMaintainTypeNumber());
//			    					cvs.put("TypeName", f090501c.getTypeName());
//			    					cvs.put("Duration", f090501c.getDuration());
//			    					bd.insert("F090501", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090502"))
//			    			{
//			    				List<F090502Class> lstF090502 = gson.fromJson(content,new TypeToken<List<F090502Class>>(){}.getType());
//			    				for(F090502Class f090502c:lstF090502)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090502c.getInnerId());
//			    					cvs.put("ItemNumber", f090502c.getItemNumber());
//			    					cvs.put("ItemName", f090502c.getItemName());
//			    					cvs.put("ItemDetail", f090502c.getItemDetail());
//			    					cvs.put("MaintainTypeNumber", f090502c.getMaintainTypeNumber());
//			    					bd.insert("F090502", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090401"))
//			    			{
//			    				List<F090401Class> lstF090401 = gson.fromJson(content,new TypeToken<List<F090401Class>>(){}.getType());
//			    				for(F090401Class f090401c:lstF090401)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090401c.getInnerId());
//			    					cvs.put("FailureClassNumber", f090401c.getFailureClassNumber());
//			    					cvs.put("FailureClassName", f090401c.getFailureClassName());
//			    					bd.insert("F090401", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090406"))
//			    			{
//			    				List<F090406Class> lstF090406 = gson.fromJson(content,new TypeToken<List<F090406Class>>(){}.getType());
//			    				for(F090406Class f090406c:lstF090406)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090406c.getInnerId());
//			    					cvs.put("RefuseNumber", f090406c.getRefuseNumber());
//			    					cvs.put("RefuseReason", f090406c.getRefuseReason());
//			    					cvs.put("Note", f090406c.getNote());
//			    					bd.insert("F090406", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F0902"))
//			    			{
//			    				List<F0902Class> lstF0902 = gson.fromJson(content,new TypeToken<List<F0902Class>>(){}.getType());
//			    				for(F0902Class f0902c:lstF0902)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0902c.getInnerId());
//			    					cvs.put("ContractNumber", f0902c.getContractNumber());
//			    					cvs.put("ContractType", f0902c.getContractType());
//			    					cvs.put("CustomerNumber", f0902c.getCustomerNumber());
//			    					cvs.put("ContractMoney", f0902c.getContractMoney());
//			    					String signdate = CommonUtil.getValue(f0902c.getSignDate());
//			    					if(!signdate.equals("δ֪")){
//			    						signdate = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,signdate);
//			    					}
//			    					cvs.put("SignDate", signdate);
//			    					String startdate = CommonUtil.getValue(f0902c.getStartDate());
//			    					if(!startdate.equals("δ֪")){
//			    						startdate = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,startdate);
//			    					}
//			    					cvs.put("StartDate", startdate);
//			    					String enddate = CommonUtil.getValue(f0902c.getEndDate());
//			    					if(!enddate.equals("δ֪")){
//			    						enddate = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,enddate);
//			    					}
//			    					cvs.put("EndDate", enddate);
//			    					cvs.put("Principal", f0902c.getPrincipal());
//			    					cvs.put("ContractWay", f0902c.getContractWay());
//			    					cvs.put("SettleWay", f0902c.getSettleWay());
//			    					cvs.put("RecordPersonNumber", f0902c.getRecordPersonNumber());
//			    					String recorddate = CommonUtil.getValue(f0902c.getRecordDate());
//			    					if(!recorddate.equals("δ֪")){
//			    						recorddate = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,recorddate);
//			    					}
//			    					cvs.put("RecordDate", recorddate);
//			    					cvs.put("ContractAddress", f0902c.getContractAddress());
//			    					cvs.put("Note", f0902c.getNote());
//			    					bd.insert("F0902", cvs);
//			    				}    	
//			    			}
//			    			if(tablename.equals("F090202"))
//			    			{
//			    				List<F090202Class> lstF090202 = gson.fromJson(content,new TypeToken<List<F090202Class>>(){}.getType());
//			    				for(F090202Class f090202c:lstF090202)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090202c.getInnerId());
//			    					cvs.put("ContractNumber", f090202c.getContractNumber());
//			    					cvs.put("ElevatorNumber", f090202c.getElevatorNumber());
//			    					cvs.put("PriceDetail", f090202c.getPriceDetail());
//			    					cvs.put("RecordDate", f090202c.getRecordDate());
//			    					bd.insert("F090202", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F000601"))
//			    			{
//			    				List<F000601Class> lstF000601 = gson.fromJson(content,new TypeToken<List<F000601Class>>(){}.getType());
//			    				for(F000601Class f000601c:lstF000601)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f000601c.getInnerId());
//			    					cvs.put("TeamCode", f000601c.getTeamCode());
//			    					cvs.put("EmployeeNumber", f000601c.getEmployeeNumber());
//			    					String recordtime = CommonUtil.getValue(f000601c.getRecordTime());
//			    					if(!recordtime.equals("δ֪")){
//			    						recordtime = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,recordtime);
//			    					}
//			    					cvs.put("RecordTime", recordtime);
//			    					bd.insert("F000601", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F0006"))
//			    			{
//			    				List<F0006Class> lstF0006 = gson.fromJson(content,new TypeToken<List<F0006Class>>(){}.getType());
//			    				for(F0006Class f0006c:lstF0006)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0006c.getInnerId());
//			    					cvs.put("Code", f0006c.getCode());
//			    					cvs.put("Name", f0006c.getName());
//			    					cvs.put("Monitor", f0006c.getMonitor());
//			    					cvs.put("ViceMonitor", f0006c.getViceMonitor());
//			    					bd.insert("F0006", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F0201"))
//			    			{
//			    				List<F0201Class> lstF0201 = gson.fromJson(content,new TypeToken<List<F0201Class>>(){}.getType());
//			    				for(F0201Class f0201c:lstF0201)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0201c.getInnerId());
//			    					cvs.put("CustomerNumber", f0201c.getCustomerNumber());
//			    					cvs.put("ShortName", f0201c.getShortName());
//			    					cvs.put("FullName", f0201c.getFullName());
//			    					cvs.put("ContactPerson", f0201c.getContactPerson());
//			    					cvs.put("Telephone", f0201c.getTelephone());
//			    					cvs.put("Mobilephone", f0201c.getMobilephone());
//			    					cvs.put("Address", f0201c.getAddress());
//			    					bd.insert("F0201", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F0901"))
//			    			{
//			    				List<F0901Class> lstF0901 = gson.fromJson(content,new TypeToken<List<F0901Class>>(){}.getType());
//			    				for(F0901Class f0901c:lstF0901)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0901c.getInnerId());
//			    					cvs.put("ElevatorNumber", f0901c.getElevatorNumber());
//			    					cvs.put("ElevatorName", f0901c.getElevatorName());
//			    					cvs.put("Type", f0901c.getType());
//			    					cvs.put("UseAddress", f0901c.getUseAddress());
//			    					cvs.put("AddMaintainFlag", f0901c.getAddMaintainFlag());
//			    					cvs.put("TeamCode", f0901c.getTeamCode());
//			    					cvs.put("NewestContractNumber", f0901c.getNewestContractNumber());
//			    					cvs.put("Latitude", f0901c.getLatitude());
//			    					cvs.put("Longitude", f0901c.getLongitude());
//			    					cvs.put("MaintenanceState", f0901c.getMaintenanceState());
//			    					String newestcontractenddate = CommonUtil.getValue(f0901c.getNewestContractEndDate());
//			    					if(!newestcontractenddate.equals("δ֪")){
//			    						newestcontractenddate = CommonUtil.ParseTimeFromMilliSeconds(timeFormat,newestcontractenddate);
//			    					}
//			    					cvs.put("NewestContractEndDate", newestcontractenddate);
//			    					bd.insert("F0901", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090106"))
//			    			{
//			    				List<F090106Class> lstF090106 = gson.fromJson(content, new TypeToken<List<F090106Class>>(){}.getType());
//			    				for(F090106Class f090106c:lstF090106)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090106c.getInnerId());
//			    					cvs.put("EmployeeNumber", f090106c.getEmployeeNumber());
//			    					cvs.put("ElevatorNumber", f090106c.getElevatorNumber());
//			    					cvs.put("TeamCode", f090106c.getTeamCode());
//			    					bd.insert("F090106", cvs);
//			    				}
//			    			}
//			    			if(tablename.equals("F990201"))
//			    			{
//			    				List<F990201Class> lst = gson.fromJson(content,new TypeToken<List<F990201Class>>(){}.getType());
//			    				for(F990201Class item:lst)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", item.getInnerId());
//			    					cvs.put("RoleCode", item.getRoleCode());//����С���� ������
//			    					cvs.put("ModuleCode", item.getModuleCode());
//			    					cvs.put("ModuleName", item.getModuleName());
//			    					cvs.put("ActionCode", item.getActionCode());
//			    					cvs.put("ActionName", item.getActionName());
//			    					bd.insert("F990201", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F990101"))
//			    			{
//			    				List<F990101Class> lst = gson.fromJson(content,new TypeToken<List<F990101Class>>(){}.getType());
//			    				for(F990101Class item:lst)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", item.getInnerId());
//			    					cvs.put("UserName", item.getUserName());//����С���� ������
//			    					cvs.put("RoleCode", item.getRoleCode());
//			    					cvs.put("DataPermissionExpression", item.getDataPermissionExpression());
//			    					bd.insert("F990101", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F9901"))
//			    			{
//			    				List<F9901Class> lst = gson.fromJson(content,new TypeToken<List<F9901Class>>(){}.getType());
//			    				for(F9901Class item:lst)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", item.getInnerId());
//			    					cvs.put("UserName", item.getUserName());//����С���� ������
//			    					cvs.put("DisplayName", item.getDisplayName());
//			    					cvs.put("EmployeeNumber", item.getEmployeeNumber());
//			    					cvs.put("IsSuperAdmin", item.getIsSuperAdmin());
//			    					bd.insert("F9901", cvs);
//			    				}  
//			    			}
//			    		}
//			    		//���²���ʱ��
//			    		String curTime = getCurrentDateTime();
//			    		bd.execute("update F0000 set updatetime='"+curTime+"';");
//			    		bd.commit();
//			    		 
//					} catch (Exception e) {
//						// TODO: handle exception
//						bd.rollback();
//					}finally
//					{
//						if(bd!=null)
//						{
//							bd.close(); 
//							bd = null;
//						}
//						
//					}
//		    		  	
//		    	   }
//		    	}
////	    	}
	    	
	    } 
	    /**
	     * ��ô�ж��Ƿ��������ݣ���BaseDao�м���һ�������жϱ�Ҫ����ݱ��Ƿ���Ϊ�յģ����Ϊ�յĻ�����˵���ϴ�û�����سɹ�����ȥ�������أ����Ϊ�գ��Ͳ���ִ�����غ���
	     * ��ôȥ��֤������سɹ������ɹ�����ʹ�û�����ʹ�ã����ǣ�
	     * ����������ȡ������ݣ��͵�����ȡ���
	     */
	    public static void DownLoadRoleData(Context c)
	    {
//	    	/**
//			 * ���޺ͱ�������״̬��һֱ����仯�����Բ���Ҫ�����ر��棬����Ļ�������û�а취���ˡ��ڱ�Ҫ��ʱ�������ر��棬��д�ô��룬������ʵ�������£���Ҫ���߲�������Ƚ϶��ʱ�򣬲Ż�����ʵʩ������
//	    	//����ͼ��޹����Ȳ����� "F0904","F0905",
//	    	 * String[] arrTableNames= new String[]{"F0904,F0905","F090103,F090104,F090501,F090502","F090401,F090406,F0902","F090202,F0101","F000601,F0006","F0201,F0901"};
//	    	 * */    	
//	    	SharedPreferences sp = c.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	String companyname = sp.getString(Configuration.ECNAME, "");
//	    	String employeenuber = sp.getString(Configuration.USERID, "");
//	    	String employeename = sp.getString(Configuration.USERNAME, "");
//	    	String[] arrTNs = new String[]{"F990101","F990201","F9901"};
////	    	boolean isCompleteDownLoad = true;
////	    	BaseDao<F0101Class> bdDownLoad = new BaseDao<F0101Class>(c);
////	    	isCompleteDownLoad = bdDownLoad.isCompleteDownLoad(arrTNs);
////	    	bdDownLoad.close();
////	    	if(!isCompleteDownLoad)
////	    	{
//		    	String[] arrTableNames= new String[]{"F990101,F990201,F9901"};
//		//    	String tablenames = "F0902,F0101";
//		    	for(String tablenames:arrTableNames)
//		    	{
//		    	HashMap<String,String> hmParam = new HashMap<String,String>();
//		    	hmParam.put("companyname", companyname);
//		    	hmParam.put("tablenames", tablenames);
//		    	hmParam.put("username", employeename);
//		    	String ServiceName = "SyncService";
//		    	String Json = WebService.getRemoteInfo(c,"DownLoadNewRoleTables", ServiceName, hmParam);
//		    	Gson gson = new Gson();
//		    	ContentValues cvs = new ContentValues();
//		    	String[] arrItemDatas =Json.split("\\$"); 
//		    	for(String strItemData:arrItemDatas)
//		    	{
//		    		BaseDao<F0101Class> bd = null;
//		    		Cursor cf990201 = null;
//		    		Cursor cf990101 = null;
//		    		Cursor cf9901 = null;
//		    		try {
//		    			 bd = new BaseDao<F0101Class>(c);
//			    		
//		    			bd.beginTransaction();
//			    		String tablename = strItemData.substring(0, strItemData.indexOf(":["));
//			    		String content = strItemData.substring(strItemData.indexOf(":[")+1);
//			    		if(content.indexOf("]")-content.indexOf("[")>1)
//			    		{
//			    			//bd.delete(tablename, "", new String[]{}); //��ɾ����ݣ������ظ������		    			
//			    			if(tablename.equals("F990201"))
//			    			{
//			    				List<F990201Class> lst = gson.fromJson(content,new TypeToken<List<F990201Class>>(){}.getType());
//			    				for(F990201Class item:lst)
//			    				{
//			    					boolean isExist = false;
//			    					  cf990201 = bd.rawQuery("select InnerId from F990201 where InnerId='"+item.getInnerId()+"'", null);
//			    					if(cf990201.getCount()>0)
//			    						isExist = true;
//			    					cvs.clear();		    					
//			    					cvs.put("RoleCode", item.getRoleCode());//����С���� ������
//			    					cvs.put("ModuleCode", item.getModuleCode());
//			    					cvs.put("ModuleName", item.getModuleName());
//			    					cvs.put("ActionCode", item.getActionCode());
//			    					cvs.put("ActionName", item.getActionName());
//			    					if(isExist)
//			    					{
//			    						bd.update("F990201", cvs, "InnerId='"+item.getInnerId()+"'", null);
//			    					}else
//			    					{
//			    						cvs.put("InnerId", item.getInnerId());
//			    						bd.insert("F990201", cvs);
//			    					}
//			    					//bd.insert("F990201", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F990101"))
//			    			{
//			    				List<F990101Class> lst = gson.fromJson(content,new TypeToken<List<F990101Class>>(){}.getType());
//			    				for(F990101Class item:lst)
//			    				{
//			    					boolean isExist = false;
//			    					  cf990101 = bd.rawQuery("select InnerId from F990101 where InnerId='"+item.getInnerId()+"'", null);
//			    					if(cf990101.getCount()>0)
//			    						isExist = true;
//			    					cvs.clear();
//			    					//cvs.put("InnerId", item.getInnerId());
//			    					cvs.put("UserName", item.getUserName());//����С���� ������
//			    					cvs.put("RoleCode", item.getRoleCode());
//			    					cvs.put("DataPermissionExpression", item.getDataPermissionExpression());
//			    					if(isExist)
//			    					{
//			    						bd.update("F990101", cvs, "InnerId='"+item.getInnerId()+"'", null);
//			    					}else
//			    					{
//			    						cvs.put("InnerId", item.getInnerId());
//			    						bd.insert("F990101", cvs);
//			    					}
//			    					
//			    				}  
//			    			}
//			    			if(tablename.equals("F9901"))
//			    			{
//			    				List<F9901Class> lst = gson.fromJson(content,new TypeToken<List<F9901Class>>(){}.getType());
//			    				for(F9901Class item:lst)
//			    				{
//			    					boolean isExist = false;
//			    					  cf9901 = bd.rawQuery("select InnerId from F9901 where InnerId='"+item.getInnerId()+"'", null);
//			    					if(cf9901.getCount()>0)
//			    						isExist = true;
//			    					cvs.clear();
//			    					//cvs.put("InnerId", item.getInnerId());
//			    					cvs.put("UserName", item.getUserName());//����С���� ������
//			    					cvs.put("DisplayName", item.getDisplayName());
//			    					cvs.put("EmployeeNumber", item.getEmployeeNumber());
//			    					cvs.put("IsSuperAdmin", item.getIsSuperAdmin());
//			    					//bd.insert("F9901", cvs);
//			    					if(isExist)
//			    					{
//			    						bd.update("F9901", cvs, "InnerId='"+item.getInnerId()+"'", null);
//			    					}else
//			    					{
//			    						cvs.put("InnerId", item.getInnerId());
//			    						bd.insert("F9901", cvs);
//			    					}
//			    				}  
//			    			}
//			    		}
//			    		bd.commit();
//			    		
//						
//					} catch (Exception e) {
//						// TODO: handle exception
//						bd.rollback();
//					}finally
//					{
//						
//			    		if(cf990201!=null)
//						{
//			    			cf990201.close();  
//			    			cf990201 = null;
//						}
//			    		if(cf990101!=null)
//						{
//			    			cf990101.close();  
//			    			cf990101 = null;
//						}
//			    		if(cf9901!=null)
//						{
//			    			cf9901.close();  
//			    			cf9901 = null;
//						}
//			    		
//						if(bd!=null)
//						{
//							bd.close();  
//							bd = null;
//						}
//					}
//		    		  	
//		    	   }
//		    	}
////	    	}
	    	
	    } 
	    /*
	     * ��ˢ����ݲ���,ͨ��������������Խ����еĹ���ˢ�º͹������ӵĲ���ȫ��д������
	     *
	    public static int  RefreshNewUrgentMaintainData(Context c,String ordertype,String orderNumber,String orderstatus)
	    {
	    	if(com.mypatroller.common.Configuration.debugIsOn)
	    	Log.d(TAG, "RefreshNewUrgentMaintainData");
	    	int downloadcount = 0;
	    	Context mcontext = c;
	    	String strJson = "";
	    	String companyname = "";
	    	String employeenuber = "";
	    	String serviceName = "SaveInPhoneService";
	    	SharedPreferences sp = mcontext.getSharedPreferences(Configuration.SETTING_INFOS,0);
	    	companyname = sp.getString(Configuration.ECNAME, "");
	    	employeenuber = sp.getString(Configuration.USERID, "");
	    	//���Ϊ�գ���ɾ���������´���
	    	if(!orderNumber.trim().equals("")&&orderstatus.equals(""))
    		{
    			DeleteConfirmedUrgentMaintainDataByOrderNumber(c,orderNumber,ordertype);
    		}
	    	Gson gson = new Gson();
	    	HashMap<String,String> hmParam = new HashMap<String,String>();
	    	hmParam.put("companyname", companyname);	    	
	    	if(ordertype.equals("URGENT"))
	    	{
	    		
	    		if(!orderstatus.equals(""))
	    		{
	    			hmParam.put("employeenumber", employeenuber);
	    			hmParam.put("maxordernumber", orderNumber);
	    			hmParam.put("orderstatus", orderstatus);
	    			strJson = WebService.getRemoteInfo(mcontext, "LoadUrgentOrderByUserIdAndMaxOrderNumber", serviceName, hmParam);
	    		}else
	    		{
	    			hmParam.put("ordernumber", orderNumber);
	    			strJson = WebService.getRemoteInfo(mcontext, "LoadUrgentOrderByOrderNumber", serviceName, hmParam);
	    		}
	    	}
	    	if(ordertype.equals("MAINTAIN"))
	    	{
	    		if(!orderstatus.equals(""))
	    		{
	    			hmParam.put("employeenumber", employeenuber);
	    			hmParam.put("maxordernumber", orderNumber);
	    			hmParam.put("orderstatus", orderstatus);
	    			strJson = WebService.getRemoteInfo(mcontext, "LoadMaintainOrderByUserIdAndMaxOrderNumber", serviceName, hmParam);
	    		}else
	    		{
	    			hmParam.put("employeenumber", employeenuber);
	    			hmParam.put("ordernumber", orderNumber);
	    			strJson = WebService.getRemoteInfo(mcontext, "LoadNewMaintainOrderByOrderNumber", serviceName, hmParam);
	    		}
	    	}
	    	//����ݱ��浽������ݿ�
	    	if(!strJson.equals("")&&!strJson.equals("-99"))
	    	{
	    		ContentValues cvs = new ContentValues();
	    		String[] arrItemDatas =strJson.split("\\$"); 
//	    		downloadcount = arrItemDatas.length;
	    		for(String strItemData:arrItemDatas)
		    	{
	    			BaseDao<F0101Class> bd = new BaseDao<F0101Class>(mcontext);
		    		bd.beginTransaction();
		    		String tablename = strItemData.substring(0, strItemData.indexOf(":["));
		    		String content = strItemData.substring(strItemData.indexOf(":[")+1);
		    		if(content.indexOf("]")-content.indexOf("[")>1)
		    		{
		    			if(tablename.equals("F0904"))
		    			{
		    				List<F0904Class> lstF0904 = gson.fromJson(content,new TypeToken<List<F0904Class>>(){}.getType());
		    				for(F0904Class f0904c:lstF0904)
		    				{
		    					downloadcount++;
		    					cvs.clear();
		    					cvs.put("InnerId", f0904c.getInnerId());
		    					cvs.put("OrderNumber", f0904c.getOrderNumber());
		    					cvs.put("ElevatorNumber", f0904c.getElevatorNumber());
		    					cvs.put("OrderStatus", f0904c.getOrderStatus());
		    					cvs.put("DealEmployeeNumber", f0904c.getDealEmployeeNumber());
		    					cvs.put("HearTime", f0904c.getHearTime());
		    					cvs.put("OrderTime", f0904c.getOrderTime());
		    					cvs.put("DealTime", f0904c.getDealTime());
		    					cvs.put("FailureCaption", f0904c.getFailureCaption());
		    					cvs.put("FailureReason", f0904c.getFailureReason());
		    					cvs.put("FailureClassNumber", f0904c.getFailureClassNumber());
		    					cvs.put("Solution", f0904c.getSolution());
		    					cvs.put("Note", f0904c.getNote());
		    					cvs.put("Scan1", f0904c.getScan1());
		    					cvs.put("Scan2", f0904c.getScan2());
		    					cvs.put("Scan3", f0904c.getScan3());
		    					cvs.put("ConfirmTime", f0904c.getConfirmTime());
		    					cvs.put("Location", f0904c.getLocation());
		    					cvs.put("CompleteTime", f0904c.getCompleteTime());
		    					cvs.put("OrderStatusTime", f0904c.getOrderStatusTime());
		    					cvs.put("ConfirmNote", f0904c.getConfirmNote());
		    					cvs.put("PlanArrivedTime", f0904c.getPlanArrivedTime());
		    					cvs.put("RepairPerson", f0904c.getRepairPerson());
		    					cvs.put("RepairPersonContact", f0904c.getRepairPersonContact());
		    					cvs.put("UserName", f0904c.getUserName());
		    					bd.insert("F0904", cvs);
		    				}    				   	    	
		    			}
		    			if(tablename.equals("F090404"))
		    			{
		    				List<F090404Class> lstF090404 = gson.fromJson(content,new TypeToken<List<F090404Class>>(){}.getType());
		    				for(F090404Class f090404c:lstF090404)
		    				{
		    					cvs.clear();
		    					cvs.put("InnerId", f090404c.getInnerId());
		    					cvs.put("OrderNumber", f090404c.getOrderNumber());
		    					cvs.put("Note", f090404c.getNote());
		    					cvs.put("AttachmentPath", f090404c.getAttachmentPath());
		    					cvs.put("Attachment", f090404c.getAttachment());
		    					bd.insert("F090404", cvs);
		    					//��Ҫ��ͼƬpictureurl_090404 �����»��߲�����²���
		    					//��Ҫ��ͼƬpictureurl_0905x4 �����»��߲�����²���
		    					ContentValues cvx4 = new ContentValues();
		    					cvx4.put("innerid", f090404c.getInnerId());
		    					cvx4.put("note", f090404c.getNote());
		    					//�ȸ��²����������²��ɹ�����ֱ�Ӳ������
		    					int ret = bd.update("pictureurl_090404", cvx4, "ordernumber='"+f090404c.getOrderNumber()+"'",null);
		    					if(ret==0)
		    					{
		    						cvx4.put("ordernumber", f090404c.getOrderNumber());
		    						bd.insert("pictureurl_090404", cvx4);
		    					}
		    				}  
		    			}
		    			if(tablename.equals("F090405"))
		    			{
		    				List<F090405Class> lstF090405 = gson.fromJson(content,new TypeToken<List<F090405Class>>(){}.getType());
		    				for(F090405Class f090405c:lstF090405)
		    				{
		    					cvs.clear();
		    					cvs.put("InnerId", f090405c.getInnerId());
		    					cvs.put("OrderNumber", f090405c.getOrderNumber());
		    					cvs.put("DealEmployeeNumber", f090405c.getDealEmployeeNumber());
		    					cvs.put("DealTime", f090405c.getDealTime());
		    					cvs.put("RefuseNumber", f090405c.getRefuseNumber());
		    					cvs.put("ForwardEmployeeNumber", f090405c.getForwardEmployeeNumber());
		    					cvs.put("IsForward", f090405c.getIsForward());
		    					bd.insert("F090405", cvs);
		    				}  
		    			}
		    			//����f0905
		    			if(tablename.equals("F0905"))
		    			{
		    				List<F0905Class> lstF0905 = gson.fromJson(content,new TypeToken<List<F0905Class>>(){}.getType());
		    				for(F0905Class f0905c:lstF0905)
		    				{
		    					downloadcount++;
		    					cvs.clear();
		    					cvs.put("InnerId", f0905c.getInnerId());
		    					cvs.put("WorkOrderNumber", f0905c.getWorkOrderNumber());
		    					cvs.put("ContractNumber", f0905c.getContractNumber());
		    					cvs.put("OrderTypeNumber", f0905c.getOrderTypeNumber());
		    					cvs.put("TeamCode", f0905c.getTeamCode());
		    					cvs.put("OrderTime", f0905c.getOrderTime());
		    					cvs.put("OrderStatus", f0905c.getOrderStatus());
		    					cvs.put("DealEmployee", f0905c.getDealEmployee());
		    					cvs.put("DealTime", f0905c.getDealTime());
		    					cvs.put("CompleteTime", f0905c.getCompleteTime());
		    					cvs.put("ConfirmTime", f0905c.getConfirmTime());
		    					cvs.put("PlanningNumber", f0905c.getPlanningNumber());
		    					cvs.put("OrderStatusTime", f0905c.getOrderStatusTime());
		    					cvs.put("Note", f0905c.getNote());
		    					cvs.put("ConfirmNote", f0905c.getConfirmNote());
		    					cvs.put("NeedCompleteTime", f0905c.getNeedCompleteTime());
		    					cvs.put("Location", f0905c.getLocation());
		    					bd.insert("F0905", cvs);
		    				}    				   	    	
		    			}
		    			if(tablename.equals("F0905x1"))
		    			{
		    				List<F0905x1Class> lstF0905x1 = gson.fromJson(content,new TypeToken<List<F0905x1Class>>(){}.getType());
		    				for(F0905x1Class f0905x1c:lstF0905x1)
		    				{
		    					cvs.clear();
		    					cvs.put("InnerId", f0905x1c.getInnerId());
		    					cvs.put("ItemNumber", f0905x1c.getItemNumber());
		    					cvs.put("WorkOrderNumber", f0905x1c.getWorkOrderNumber());
		    					cvs.put("ElevatorNumber", f0905x1c.getElevatorNumber());
		    					cvs.put("HasDeal", f0905x1c.getHasDeal());
		    					bd.insert("F0905x1", cvs);
		    				}  
		    			}
		    			if(tablename.equals("F0905x2"))
		    			{
		    				List<F0905x2Class> lstF0905x2 = gson.fromJson(content,new TypeToken<List<F0905x2Class>>(){}.getType());
		    				for(F0905x2Class f0905x2c:lstF0905x2)
		    				{
		    					cvs.clear();
		    					cvs.put("InnerId", f0905x2c.getInnerId());
		    					cvs.put("EmployeeNumber", f0905x2c.getEmployeeNumber());
		    					cvs.put("WorkOrderNumber", f0905x2c.getWorkOrderNumber());
		    					cvs.put("HasDeal", f0905x2c.getHasDeal());
		    					bd.insert("F0905x2", cvs);
		    				}  
		    			}
		    			if(tablename.equals("F0905x3"))
		    			{
		    				List<F0905x3Class> lstF0905x3 = gson.fromJson(content,new TypeToken<List<F0905x3Class>>(){}.getType());
		    				for(F0905x3Class f0905x3c:lstF0905x3)
		    				{
		    					cvs.clear();
		    					cvs.put("InnerId", f0905x3c.getInnerId());
		    					cvs.put("ElevatorNumber", f0905x3c.getElevatorNumber());
		    					cvs.put("WorkOrderNumber", f0905x3c.getWorkOrderNumber());
		    					cvs.put("DealTime", f0905x3c.getDealTime());
		    					cvs.put("DealEmployee", f0905x3c.getDealEmployee());
		    					cvs.put("HasDeal", f0905x3c.getHasDeal());
		    					cvs.put("Scan1", f0905x3c.getScan1());
		    					cvs.put("Scan2", f0905x3c.getScan2());
		    					cvs.put("Scan3", f0905x3c.getScan3());
		    					cvs.put("Note", f0905x3c.getNote());
		    					bd.insert("F0905x3", cvs);
		    				}  
		    			}
		    			if(tablename.equals("F0905x4"))
		    			{
		    				List<F0905x4Class> lstF0905x4 = gson.fromJson(content,new TypeToken<List<F0905x4Class>>(){}.getType());
		    				for(F0905x4Class f0905x4c:lstF0905x4)
		    				{
		    					
		    					cvs.clear();
		    					cvs.put("InnerId", f0905x4c.getInnerId());
		    					cvs.put("ElevatorNumber", f0905x4c.getElevatorNumber());
		    					cvs.put("WorkOrderNumber", f0905x4c.getWorkOrderNumber());
		    					cvs.put("Attachment", f0905x4c.getAttachment());
		    					cvs.put("AttachmentPath", f0905x4c.getAttachmentPath());
		    					cvs.put("Note", f0905x4c.getNote());
		    					bd.insert("F0905x4", cvs);
		    					//��Ҫ��ͼƬpictureurl_0905x4 �����»��߲�����²���
		    					ContentValues cvx4 = new ContentValues();
		    					cvx4.put("innerid", f0905x4c.getInnerId());
		    					cvx4.put("note", f0905x4c.getNote());
		    					int ret = bd.update("pictureurl_0905x4", cvx4, "workordernumber='"+f0905x4c.getWorkOrderNumber()+"' and elevatornumber='"+f0905x4c.getElevatorNumber()+"'",null);
		    					if(ret==0)
		    					{
		    						cvx4.put("workordernumber", f0905x4c.getWorkOrderNumber());
		    						cvx4.put("elevatornumber", f0905x4c.getElevatorNumber());
		    						bd.insert("pictureurl_0905x4", cvx4);
		    					}
		    				}  
		    			}
		    		}
		    		bd.commit();
		    		bd.close(); 
		    	}	    		
	    	}
	    	return downloadcount;
	    }*/
	    /*
	     * ��ˢ����ݲ���,ͨ��������������Խ����еĹ���ˢ�º͹������ӵĲ���ȫ��д������
	     */
	    public static int  RefreshNewUrgentMaintainData(Context c,String ordertype,String orderNumber,String orderstatus)
	    {
	    	return 0;
//	    	if(com.mypatroller.common.Configuration.debugIsOn)
//	    	Log.d(TAG, "RefreshNewUrgentMaintainData");
//	    	int downloadcount = 0;
//	    	Context mcontext = c;
//	    	String strJson = "";
//	    	String companyname = "";
//	    	String employeenuber = "";
//	    	String serviceName = "SaveInPhoneService";
//	    	SharedPreferences sp = mcontext.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	companyname = sp.getString(Configuration.ECNAME, "");
//	    	employeenuber = sp.getString(Configuration.USERID, "");
//	    	//���Ϊ�գ���ɾ���������´���
//	    	if(!orderNumber.trim().equals("")&&orderstatus.equals(""))
//    		{
//    			DeleteConfirmedUrgentMaintainDataByOrderNumber(c,orderNumber,ordertype);
//    		}
//	    	Gson gson = new Gson();
//	    	HashMap<String,String> hmParam = new HashMap<String,String>();
//	    	hmParam.put("companyname", companyname);	    	
//	    	if(ordertype.equals("URGENT"))
//	    	{
//	    		
//	    		if(!orderstatus.equals(""))
//	    		{
//	    			hmParam.put("employeenumber", employeenuber);
//	    			hmParam.put("ordernumbers", orderNumber);
//	    			hmParam.put("orderstatus", orderstatus);
//	    			hmParam.put("ordertype", "URGENT");
//	    			strJson = WebService.getRemoteInfo(mcontext, "ReFreshOrdershData", serviceName, hmParam);
//	    		}else
//	    		{
//	    			hmParam.put("ordernumber", orderNumber);
//	    			strJson = WebService.getRemoteInfo(mcontext, "LoadUrgentOrderByOrderNumber", serviceName, hmParam);
//	    		}
//	    	}
//	    	if(ordertype.equals("MAINTAIN"))
//	    	{
//	    		if(!orderstatus.equals(""))
//	    		{
//	    			hmParam.put("employeenumber", employeenuber);
//	    			hmParam.put("ordernumbers", orderNumber);
//	    			hmParam.put("orderstatus", orderstatus);
//	    			hmParam.put("ordertype", "MAINTAIN");
//	    			strJson = WebService.getRemoteInfo(mcontext, "ReFreshOrdershData", serviceName, hmParam);
//	    		}else
//	    		{
//	    			hmParam.put("employeenumber", employeenuber);
//	    			hmParam.put("ordernumber", orderNumber);
//	    			strJson = WebService.getRemoteInfo(mcontext, "LoadNewMaintainOrderByOrderNumber", serviceName, hmParam);
//	    		}
//	    	}
//	    	//����ݱ��浽������ݿ�
//	    	if(!strJson.equals("")&&!strJson.equals("-99"))
//	    	{
//	    		ContentValues cvs = new ContentValues();
//	    		String[] arrItemDatas =strJson.split("\\$"); 
////	    		downloadcount = arrItemDatas.length;
//	    		for(String strItemData:arrItemDatas)
//		    	{
//	    			BaseDao<F0101Class> bd = null;
//	    			try {
//	    				 bd = new BaseDao<F0101Class>(mcontext);
//	    				bd.beginTransaction();
//			    		String tablename = strItemData.substring(0, strItemData.indexOf(":["));
//			    		String content = strItemData.substring(strItemData.indexOf(":[")+1);
//			    		if(content.indexOf("]")-content.indexOf("[")>1)
//			    		{
//			    			if(tablename.equals("F0904"))
//			    			{
//			    				List<F0904Class> lstF0904 = gson.fromJson(content,new TypeToken<List<F0904Class>>(){}.getType());
//			    				for(F0904Class f0904c:lstF0904)
//			    				{
//			    					downloadcount++;
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0904c.getInnerId());
//			    					cvs.put("OrderNumber", f0904c.getOrderNumber());
//			    					cvs.put("ElevatorNumber", f0904c.getElevatorNumber());
//			    					cvs.put("OrderStatus", f0904c.getOrderStatus());
//			    					cvs.put("DealEmployeeNumber", f0904c.getDealEmployeeNumber());
//			    					cvs.put("HearTime", f0904c.getHearTime());
//			    					cvs.put("OrderTime", f0904c.getOrderTime());
//			    					cvs.put("DealTime", f0904c.getDealTime());
//			    					cvs.put("FailureCaption", f0904c.getFailureCaption());
//			    					cvs.put("FailureReason", f0904c.getFailureReason());
//			    					cvs.put("FailureClassNumber", f0904c.getFailureClassNumber());
//			    					cvs.put("Solution", f0904c.getSolution());
//			    					cvs.put("Note", f0904c.getNote());
//			    					cvs.put("Scan1", f0904c.getScan1());
//			    					cvs.put("Scan2", f0904c.getScan2());
//			    					cvs.put("Scan3", f0904c.getScan3());
//			    					cvs.put("ConfirmTime", f0904c.getConfirmTime());
//			    					cvs.put("Location", f0904c.getLocation());
//			    					cvs.put("CompleteTime", f0904c.getCompleteTime());
//			    					cvs.put("OrderStatusTime", f0904c.getOrderStatusTime());
//			    					cvs.put("ConfirmNote", f0904c.getConfirmNote());
//			    					cvs.put("PlanArrivedTime", f0904c.getPlanArrivedTime());
//			    					cvs.put("RepairPerson", f0904c.getRepairPerson());
//			    					cvs.put("RepairPersonContact", f0904c.getRepairPersonContact());
//			    					cvs.put("UserName", f0904c.getUserName());
//			    					bd.insert("F0904", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F090404"))
//			    			{
//			    				List<F090404Class> lstF090404 = gson.fromJson(content,new TypeToken<List<F090404Class>>(){}.getType());
//			    				for(F090404Class f090404c:lstF090404)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090404c.getInnerId());
//			    					cvs.put("OrderNumber", f090404c.getOrderNumber());
//			    					cvs.put("Note", f090404c.getNote());
//			    					cvs.put("AttachmentPath", f090404c.getAttachmentPath());
//			    					cvs.put("Attachment", f090404c.getAttachment());
//			    					bd.insert("F090404", cvs);
//			    					//��Ҫ��ͼƬpictureurl_090404 �����»��߲�����²���
//			    					//��Ҫ��ͼƬpictureurl_0905x4 �����»��߲�����²���
//			    					ContentValues cvx4 = new ContentValues();
//			    					cvx4.put("innerid", f090404c.getInnerId());
//			    					cvx4.put("note", f090404c.getNote());
//			    					//�ȸ��²����������²��ɹ�����ֱ�Ӳ������
//			    					int ret = bd.update("pictureurl_090404", cvx4, "ordernumber='"+f090404c.getOrderNumber()+"'",null);
//			    					if(ret==0)
//			    					{
//			    						cvx4.put("ordernumber", f090404c.getOrderNumber());
//			    						bd.insert("pictureurl_090404", cvx4);
//			    					}
//			    				}  
//			    			}
//			    			if(tablename.equals("F090405"))
//			    			{
//			    				List<F090405Class> lstF090405 = gson.fromJson(content,new TypeToken<List<F090405Class>>(){}.getType());
//			    				for(F090405Class f090405c:lstF090405)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f090405c.getInnerId());
//			    					cvs.put("OrderNumber", f090405c.getOrderNumber());
//			    					cvs.put("DealEmployeeNumber", f090405c.getDealEmployeeNumber());
//			    					cvs.put("DealTime", f090405c.getDealTime());
//			    					cvs.put("RefuseNumber", f090405c.getRefuseNumber());
//			    					cvs.put("ForwardEmployeeNumber", f090405c.getForwardEmployeeNumber());
//			    					cvs.put("IsForward", f090405c.getIsForward());
//			    					bd.insert("F090405", cvs);
//			    				}  
//			    			}
//			    			//����f0905
//			    			if(tablename.equals("F0905"))
//			    			{
//			    				List<F0905Class> lstF0905 = gson.fromJson(content,new TypeToken<List<F0905Class>>(){}.getType());
//			    				for(F0905Class f0905c:lstF0905)
//			    				{
//			    					downloadcount++;
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0905c.getInnerId());
//			    					cvs.put("WorkOrderNumber", f0905c.getWorkOrderNumber());
//			    					cvs.put("ContractNumber", f0905c.getContractNumber());
//			    					cvs.put("OrderTypeNumber", f0905c.getOrderTypeNumber());
//			    					cvs.put("TeamCode", f0905c.getTeamCode());
//			    					cvs.put("OrderTime", f0905c.getOrderTime());
//			    					cvs.put("OrderStatus", f0905c.getOrderStatus());
//			    					cvs.put("DealEmployee", f0905c.getDealEmployee());
//			    					cvs.put("DealTime", f0905c.getDealTime());
//			    					cvs.put("CompleteTime", f0905c.getCompleteTime());
//			    					cvs.put("ConfirmTime", f0905c.getConfirmTime());
//			    					cvs.put("PlanningNumber", f0905c.getPlanningNumber());
//			    					cvs.put("OrderStatusTime", f0905c.getOrderStatusTime());
//			    					cvs.put("Note", f0905c.getNote());
//			    					cvs.put("ConfirmNote", f0905c.getConfirmNote());
//			    					cvs.put("NeedCompleteTime", f0905c.getNeedCompleteTime());
//			    					cvs.put("Location", f0905c.getLocation());
//			    					bd.insert("F0905", cvs);
//			    				}    				   	    	
//			    			}
//			    			if(tablename.equals("F0905x1"))
//			    			{
//			    				List<F0905x1Class> lstF0905x1 = gson.fromJson(content,new TypeToken<List<F0905x1Class>>(){}.getType());
//			    				for(F0905x1Class f0905x1c:lstF0905x1)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0905x1c.getInnerId());
//			    					cvs.put("ItemNumber", f0905x1c.getItemNumber());
//			    					cvs.put("WorkOrderNumber", f0905x1c.getWorkOrderNumber());
//			    					cvs.put("ElevatorNumber", f0905x1c.getElevatorNumber());
//			    					cvs.put("HasDeal", f0905x1c.getHasDeal());
//			    					bd.insert("F0905x1", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F0905x2"))
//			    			{
//			    				List<F0905x2Class> lstF0905x2 = gson.fromJson(content,new TypeToken<List<F0905x2Class>>(){}.getType());
//			    				for(F0905x2Class f0905x2c:lstF0905x2)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0905x2c.getInnerId());
//			    					cvs.put("EmployeeNumber", f0905x2c.getEmployeeNumber());
//			    					cvs.put("WorkOrderNumber", f0905x2c.getWorkOrderNumber());
//			    					cvs.put("HasDeal", f0905x2c.getHasDeal());
//			    					bd.insert("F0905x2", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F0905x3"))
//			    			{
//			    				List<F0905x3Class> lstF0905x3 = gson.fromJson(content,new TypeToken<List<F0905x3Class>>(){}.getType());
//			    				for(F0905x3Class f0905x3c:lstF0905x3)
//			    				{
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0905x3c.getInnerId());
//			    					cvs.put("ElevatorNumber", f0905x3c.getElevatorNumber());
//			    					cvs.put("WorkOrderNumber", f0905x3c.getWorkOrderNumber());
//			    					cvs.put("DealTime", f0905x3c.getDealTime());
//			    					cvs.put("DealEmployee", f0905x3c.getDealEmployee());
//			    					cvs.put("HasDeal", f0905x3c.getHasDeal());
//			    					cvs.put("Scan1", f0905x3c.getScan1());
//			    					cvs.put("Scan2", f0905x3c.getScan2());
//			    					cvs.put("Scan3", f0905x3c.getScan3());
//			    					cvs.put("Note", f0905x3c.getNote());
//			    					bd.insert("F0905x3", cvs);
//			    				}  
//			    			}
//			    			if(tablename.equals("F0905x4"))
//			    			{
//			    				List<F0905x4Class> lstF0905x4 = gson.fromJson(content,new TypeToken<List<F0905x4Class>>(){}.getType());
//			    				for(F0905x4Class f0905x4c:lstF0905x4)
//			    				{
//			    					
//			    					cvs.clear();
//			    					cvs.put("InnerId", f0905x4c.getInnerId());
//			    					cvs.put("ElevatorNumber", f0905x4c.getElevatorNumber());
//			    					cvs.put("WorkOrderNumber", f0905x4c.getWorkOrderNumber());
//			    					cvs.put("Attachment", f0905x4c.getAttachment());
//			    					cvs.put("AttachmentPath", f0905x4c.getAttachmentPath());
//			    					cvs.put("Note", f0905x4c.getNote());
//			    					bd.insert("F0905x4", cvs);
//			    					//��Ҫ��ͼƬpictureurl_0905x4 �����»��߲�����²���
//			    					ContentValues cvx4 = new ContentValues();
//			    					cvx4.put("innerid", f0905x4c.getInnerId());
//			    					cvx4.put("note", f0905x4c.getNote());
//			    					int ret = bd.update("pictureurl_0905x4", cvx4, "workordernumber='"+f0905x4c.getWorkOrderNumber()+"' and elevatornumber='"+f0905x4c.getElevatorNumber()+"'",null);
//			    					if(ret==0)
//			    					{
//			    						cvx4.put("workordernumber", f0905x4c.getWorkOrderNumber());
//			    						cvx4.put("elevatornumber", f0905x4c.getElevatorNumber());
//			    						bd.insert("pictureurl_0905x4", cvx4);
//			    					}
//			    				}  
//			    			}
//			    		}
//			    		bd.commit();
//			    		
//					} catch (Exception e) {
//						// TODO: handle exception
//						bd.rollback();
//					}finally
//					{
//						if(bd!=null)
//						{
//							bd.close();
//							bd = null;
//						}
//						
//					}
//	    			
//	    			 
//		    	}	    		
//	    	}
//	    	return downloadcount;
	    }
	    /*
	     * ���������ͺͱ��ɾ��
	     */
	    public static int  DeleteConfirmedUrgentMaintainDataByOrderNumber(Context c,String ordernumber,String ordertype)
	    {
	    	return 1;
//	    	int deleteresult = 0;
//	    	BaseDao<F0901Class> bd2 = null;
//	    	
//	    	try{	
//	    		bd2 = new BaseDao<F0901Class>(c);
//		    	bd2.beginTransaction();
//	    		if(ordertype.equals("MAINTAIN"))
//	    		{
//	    			bd2.execute("delete from F0905x1  where [workordernumber] ='"+ordernumber+"'");
//	    			bd2.execute("delete from F0905x2  where [workordernumber] ='"+ordernumber+"'");
//	    			bd2.execute("delete from F0905x3  where [workordernumber] ='"+ordernumber+"'");
//	    			//ɾ����Ƭ
////	    			Cursor curgent = bd2.rawQuery("select [localurl] from pictureurl_0905x4 where [workordernumber] ='"+ordernumber+"'", null);
////	    			while(curgent.moveToNext())
////	    			{
////	    				String picurl = curgent.getString(curgent.getColumnIndex("localurl"));
////	    				if(picurl != null&&!isEmpty(picurl))
////	    				{
////	    					deleteFile(picurl);
////	    				}
////	    			}
////	    			curgent.close();
//	    			bd2.execute("delete from F0905x4  where [workordernumber] ='"+ordernumber+"'");	    			
//	    			bd2.execute("delete from  F0905 where [workordernumber] ='"+ordernumber+"'");
//	    			
//	    		}else if(ordertype.equals("URGENT"))
//	    		{
//	    			//ɾ����Ƭ
////	    			Cursor curgent = bd2.rawQuery("select [localurl] from pictureurl_090404 where [ordernumber] ='"+ordernumber+"'", null);
////	    			while(curgent.moveToNext())
////	    			{
////	    				String picurl = curgent.getString(curgent.getColumnIndex("localurl"));
////	    				if(picurl != null&&!isEmpty(picurl))
////	    				{
////	    					deleteFile(picurl);
////	    				}
////	    			}
//	    			bd2.execute("delete from F090404  where [OrderNumber] ='"+ordernumber+"'");	    			
//	    			bd2.execute("delete from F090405 where [OrderNumber] ='"+ordernumber+"'");
//	    			bd2.execute("delete from  F0904 where [ordernumber] ='"+ordernumber+"'");
//	    		}
//	    		bd2.commit();
//	    	}
//	    	catch(Exception e){
//	    		deleteresult = -1;
//	    		bd2.rollback();
//	    	}finally{
//	    		if(bd2!=null)
//	    		{
//	    			bd2.close();
//	    			bd2 = null;
//	    		}
//	    		
//	    	}
//	    	return deleteresult;
	    }
	    /*
	     * ���չ�������ɾ���100��֮���ȷ�Ϻ�ı���
	     */
	    public static int  DeleteConfirmedUrgentMaintainData(Context c,String ordertype)
	    {
	    	return 0;
//	    	int deleteresult = 0;
//	    	BaseDao<F0901Class> bd2 = null;
//	    	Cursor curgent = null;
//	    	try{	
//	    		  bd2 = new BaseDao<F0901Class>(c);
//		    	if(com.mypatroller.common.Configuration.debugIsOn)
//		    	Log.d(TAG, "DeleteConfirmedUrgentMaintainData");
//		    	bd2.beginTransaction();
//	    		if(ordertype.equals("MAINTAIN"))
//	    		{
//	    			bd2.execute("delete from F0905x1  where [workordernumber] in(select m.[WorkOrderNumber]  from F0905 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			bd2.execute("delete from F0905x2  where [workordernumber] in(select m.[WorkOrderNumber]  from F0905 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			bd2.execute("delete from F0905x3  where [workordernumber] in(select m.[WorkOrderNumber]  from F0905 as m where m.[orderstatus] ='40'  order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			//ɾ����Ƭ
//	    			  curgent = bd2.rawQuery("select [localurl] from pictureurl_0905x4 where [workordernumber] in(select m.[WorkOrderNumber]  from F0905 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)", null);
//	    			while(curgent.moveToNext())
//	    			{
//	    				String picurl = curgent.getString(curgent.getColumnIndex("localurl"));
//	    				if(picurl != null&&!isEmpty(picurl))
//	    				{
//	    					deleteFile(picurl);
//	    				}
//	    			}
//	    			bd2.execute("delete from F0905x4  where [workordernumber] in(select m.[WorkOrderNumber]  from F0905 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			bd2.execute("delete from  F0905 where [workordernumber] in (select workordernumber from F0905  where [orderstatus] ='40' order by [confirmtime] desc limit 10000,100)");
//	    			
//	    		}else if(ordertype.equals("URGENT"))
//	    		{
//	    			//ɾ����Ƭ
//	    			  curgent = bd2.rawQuery("select [localurl] from pictureurl_090404 where [ordernumber] in(select m.[OrderNumber]  from F0904 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)", null);
//	    			while(curgent.moveToNext())
//	    			{
//	    				String picurl = curgent.getString(curgent.getColumnIndex("localurl"));
//	    				if(picurl != null&&!isEmpty(picurl))
//	    				{
//	    					deleteFile(picurl);
//	    				}
//	    			}
//	    			bd2.execute("delete from F090404  where [OrderNumber] in(select m.[OrderNumber]  from F0904 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			bd2.execute("delete from F090405 where [OrderNumber] in(select m.[OrderNumber]  from F0904 as m where m.[orderstatus] ='40' order by m.[confirmtime] desc limit 10000 offset 100)");
//	    			bd2.execute("delete from  F0904 where [ordernumber] in (select ordernumber from F0904  where [orderstatus] ='40' order by [confirmtime] desc limit 10000 offset 100)");
//	    		}
//	    		bd2.commit();
//	    	}
//	    	catch(Exception e){
//	    		deleteresult = -1;
//	    		bd2.rollback();
//	    	}finally{
//	    		if(curgent!=null)
//	    		{
//	    			curgent.close();
//	    			curgent =null;
//	    		}
//	    		if(bd2!=null)
//	    		{
//	    			bd2.close();
//		    		bd2 =null;
//	    		}
//	    		
//	    	}
//	    	return deleteresult;
	    }
	    /**
	     * ���ݹ������ ���ϴ������ĸ���
	     * @param mcontext
	     * @param companyname
	     * @param ordernumber���������
	     * @param elevatornumber�����ݱ��
	     * @param ordertype����������:URGENT,MAINTANCE
	     * @return
	     */
	    public static int UpLoadOrderAttanchments(Context mcontext,String companyname,String ordernumber,String elevatornumber,String ordertype)
	    {
	    	return 0;
//	    	
//	    	if(com.mypatroller.common.Configuration.debugIsOn)
//	    	Log.d(TAG, "UpLoadOrderAttanchments");
//	    	int ret = 0;
//	    	BitmapFactory.Options bitmapoptions = new BitmapFactory.Options();
//	    	
//	    	
//	    	BaseDao<F0904Class> bd3 = null;
//	    	Cursor c3 = null;
//	         try {
//	        	 bd3 = new BaseDao<F0904Class>(mcontext);
//	 	    	bitmapoptions.inJustDecodeBounds = false;
//	 	         String serviceName = "CommonMethods";	    
//	 	         ContentValues cv3 = new ContentValues();
//	 	           c3 = null;
//	        	 if(ordertype.equals("URGENT"))
//		         {
//		        	 String sql3 = "select id,note,localurl from pictureurl_090404 where ordernumber = '"+ordernumber+"'";
//		        	 c3 = bd3.rawQuery(sql3,null);
//		        	 while(c3.moveToNext())
//		        	 {
//		        		 String id = c3.getString(c3.getColumnIndex("id"));
//		        		 String note = c3.getString(c3.getColumnIndex("note"));
//		        		 String localPath = c3.getString(c3.getColumnIndex("localurl"));
//		        		 Bitmap bitmap = BitmapFactory.decodeFile(localPath, bitmapoptions);
//		        		 File ft = new File(localPath);
//		        		 if(ft.exists())
//		        		 {
//			    	        ByteArrayOutputStream out=new ByteArrayOutputStream();
//			   	     	 	bitmap.compress(CompressFormat.JPEG, 100, out);//100 �޸�Ϊ50������Ƭ��������һ��
//			   				//100KB ��С����ѹ��
//			   				int options = 100;  
//			   				while ( out.toByteArray().length / 1024>50) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
//			   					out.reset();//����baos�����baos  
//			   					bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);//����ѹ��options%����ѹ�������ݴ�ŵ�out��  
//			   					options -= 10;//ÿ�ζ�����5���ٷֱ�  
//			   				}  
//			   				byte []bytes=out.toByteArray();
//				   	     	 String uploadBuffer=new String(Base64.encode(bytes)); 
//				   			String fileName = localPath.substring(localPath.lastIndexOf("/")+1);
//			               HashMap<String,String> hmParam = new HashMap<String,String>();
//			               String methodName = "UploadOrderNewAttachment";  
//			               String filetype = "image/jpeg";
//			               hmParam.put("companyname", companyname);
//			               hmParam.put("workordernumber", ordernumber);
//			               hmParam.put("note", note);
//			               hmParam.put("elevatornumber", elevatornumber); 
//			               hmParam.put("ordertype", ordertype);
//				   			hmParam.put("filename", fileName);
//				   			hmParam.put("image", uploadBuffer);
//				   			hmParam.put("filetype", filetype);
//			               //�����ｫͼƬ�ŵ���ݿ��еı���
//			               String strjson = WebService.getRemoteInfo(mcontext,methodName,serviceName, hmParam);	 //������ϴ��Ľ��
//			               if(com.mypatroller.common.Configuration.debugIsOn)
//			               Log.d(TAG, strjson);
//			   	            if(!strjson.equals("[]")&&!strjson.equals("-99"))
//			   	            {
//			   	            	cv3.clear();
//			   	            	cv3.put("innerid",strjson);		   	            	
//			   	            	bd3.update("pictureurl_090404",cv3,"id = '"+id+"'",null);
//			   	            }
//		        		 }else
//		        		 {
//		        			 if(com.mypatroller.common.Configuration.debugIsOn)
//		        			 Log.d(TAG, "bitmap is null");
//		        		 }
//		        	 }
//		         }
//		         if(ordertype.equals("MAINTAIN"))
//		         {
//		        	 String sql3 = "select id,note,localurl from pictureurl_0905x4 where workordernumber = '"+ordernumber+"' and elevatornumber ='"+elevatornumber+"'";
//		        	 c3 = bd3.rawQuery(sql3,null);
//		        	 while(c3.moveToNext())
//		        	 {
//		        		 String id = c3.getString(c3.getColumnIndex("id"));
//		        		 String note = c3.getString(c3.getColumnIndex("note"));
//		        		 String localPath = c3.getString(c3.getColumnIndex("localurl"));
//		        		 Bitmap bitmap = BitmapFactory.decodeFile(localPath, bitmapoptions);
//		        		 File ft = new File(localPath);
//		        		 if(ft.exists())
//		        		 {
//			    	        ByteArrayOutputStream out=new ByteArrayOutputStream();
//			   	     	 	bitmap.compress(CompressFormat.JPEG, 100, out);//100 �޸�Ϊ50������Ƭ��������һ��
//			   				//100KB ��С����ѹ��
//			   				int options = 100;  
//			   				while ( out.toByteArray().length / 1024>50) {  //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��         
//			   					out.reset();//����baos�����baos  
//			   					bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);//����ѹ��options%����ѹ�������ݴ�ŵ�out��  
//			   					options -= 10;//ÿ�ζ�����5���ٷֱ�  
//			   				}  
//			   				byte []bytes=out.toByteArray();
//				   	     	 String uploadBuffer=new String(Base64.encode(bytes)); 
//				   			String fileName = localPath.substring(localPath.lastIndexOf("/")+1);
//			               HashMap<String,String> hmParam = new HashMap<String,String>();
//			               String methodName = "UploadOrderNewAttachment";  
//			               String filetype = "image/jpeg";
//			               hmParam.put("companyname", companyname);
//			               hmParam.put("workordernumber", ordernumber);
//			               hmParam.put("note", note);
//			               hmParam.put("elevatornumber", elevatornumber); 
//			               hmParam.put("ordertype", "MAINTANCE");
//				   			hmParam.put("filename", fileName);
//				   			hmParam.put("image", uploadBuffer);
//				   			hmParam.put("filetype", filetype);
//			               //�����ｫͼƬ�ŵ���ݿ��еı���
//			               String strjson = WebService.getRemoteInfo(mcontext,methodName,serviceName, hmParam);	 //������ϴ��Ľ��
//			   	            if(!strjson.equals("[]")&&!strjson.equals("-99"))
//			   	            {
//			   	            	cv3.clear();
//			   	            	cv3.put("innerid",strjson);		   	            	
//			   	            	bd3.update("pictureurl_0905x4",cv3,"id = '"+id+"'",null);
////			   	            	File f = new File(localPath);
////			   	            	if(f.exists())
////			   	            	{
////			   	            		f.renameTo(localPath.substring(0,localPath.lastIndexOf("/")+1)+".jpg");
////			   	            	}
//			   	            }	
//		        		 }
//		        	 }
//		         }
//		         
//			} catch (Exception e) {
//				// TODO: handle exception
//				 
//			}finally{
//				if (c3!=null)
//				{
//					c3.close();
//					c3 = null;
//				}
//				if(bd3!=null)
//				{
//					bd3.close();
//					bd3 = null;
//				}
//				
//			}
//	         
//	    	return ret;
	    }
	    public static String getLocalRoleActions(Context context,String modulecodes)
	    {
	    	return "";
//	    	
//	    	SharedPreferences sp = context.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	String employeenuber = sp.getString(Configuration.USERID, "");
//	    	String username = sp.getString(Configuration.USERNAME, "");
//	    	String strJson = "[";
//	    	String[] lstmodules = modulecodes.split(",");
//	    	
//	    	
//	    	BaseDao<F9901Class> bd1 = null;
//	    	Cursor c1 = null;
//	    	Cursor c2 = null;
//	    	
//try {
//	 bd1 = new BaseDao<F9901Class>(context);
// 	String sql1 = "select IsSuperAdmin from F9901 where employeenumber ='"+employeenuber+"'";
// 	if(com.mypatroller.common.Configuration.debugIsOn)
// 	Log.d(TAG, sql1);
// 	  c1 = bd1.rawQuery(sql1, null);
// 	while(c1.moveToNext())
// 	{
// 		String superuser = c1.getString(c1.getColumnIndex("IsSuperAdmin"));
// 		if(superuser.equals("true"))
// 		{
// 			for(String s:lstmodules)
// 				strJson += "{\"modulecode\":\"" + s + "\",\"role\":"+1+"},";
// 			strJson = strJson.substring(0,strJson.length()-1);
// 			strJson += "]";
// 			return strJson;
// 		}else
// 		{
// 			for(String s:lstmodules)
// 			{
// 				String sql2 = "select distinct ur.[InnerId] from F990101 as ur,F990201 as ra where ur.[RoleCode] = ra.[RoleCode] and ra.[ModuleCode] like '%"+s+"%' and lower(ur.[UserName]) = '"+ username.toLowerCase()+"'";
// 				if(com.mypatroller.common.Configuration.debugIsOn)
// 				Log.d(TAG, sql2);
// 				  c2 = bd1.rawQuery(sql2, null);
//					if(c2.getCount()>0)
//					{
//						strJson += "{\"modulecode\":\"" + s + "\",\"role\":" + 1 + "},"; 
//					}else
//						strJson += "{\"modulecode\":\"" + s + "\",\"role\":" + 0 + "},"; 
// 			}
// 			strJson = strJson.substring(0,strJson.length()-1);
// 			strJson += "]";	    			
// 		}
// 	}
//				
//			} catch (Exception e) {
//				// TODO: handle exception
//			}finally{
//
//				if(c1!=null)
//				{
//					c1.close();
//					c1 = null;
//				}
//				if(c2!=null)
//				{
//					c2.close();
//					c2 = null;
//				}
//				if(bd1!=null)
//				{
//					bd1.close();
//					bd1 = null;
//				}
//				
//			}
//
//	    	
//	    	
//	    	 
//	    	return strJson;
	    }
	    public static String getRoleActionDetail(String modulecode,String actioncode,Context mcontext)
	    {
	    	return "";
//	    	SharedPreferences sp = mcontext.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	String employeenuber = sp.getString(Configuration.USERID, "");
//	    	String username = sp.getString(Configuration.USERNAME, "");
//	    	String strJson = "";
//	    	BaseDao<F9901Class> bd1 = new BaseDao<F9901Class>(mcontext);
//	    	String sql1 = "select IsSuperAdmin from F9901 where employeenumber ='"+employeenuber+"'";
//	    	Cursor c1 = null;
//	    	Cursor c2 = null;
//	    	try {
//		    	 c1 = bd1.rawQuery(sql1, null);
//		    	while(c1.moveToNext())
//		    	{
//		    		String superuser = c1.getString(c1.getColumnIndex("IsSuperAdmin"));
//		    		if(superuser.equals("true"))
//		    		{
//		    			strJson = "1";
//		    			return strJson;
//		    		}else
//		    		{
//		    				String sql2 = "select distinct ur.[InnerId] from F990101 as ur,F990201 as ra where ur.[RoleCode] = ra.[RoleCode] and ra.[ModuleCode] like '%"+modulecode+"%' and ra.ActionCode like '%"+actioncode+"%' and lower(ur.[UserName]) = '"+username.toLowerCase()+"'";
//		    				if(com.mypatroller.common.Configuration.debugIsOn)
//		    				Log.d(TAG, sql2);
//		    				 c2 = bd1.rawQuery(sql2, null);
//	    					if(c2.getCount()>0)
//	    					{
//	    						strJson = "1";
//	    					}else
//	    						strJson = "0";
//		    		}
//		    	}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}finally{
//				if(c1!=null){c1.close();c1=null;}
//				if(c2!=null){c2.close();c2=null;}
//				if(bd1!=null){bd1.close();bd1=null;}
//				
//			}
//	    		    			
//	    	return strJson;
	    }	  
	    public static String getRoleAction(String modulecode,Context mcontext)
	    {
	    	return "";
//	    	SharedPreferences sp = mcontext.getSharedPreferences(Configuration.SETTING_INFOS,0);
//	    	String employeenuber = sp.getString(Configuration.USERID, "");
//	    	String username = sp.getString(Configuration.USERNAME, "");
//	    	String strJson = "";
//	    	BaseDao<F9901Class> bd1 = null;
//	    	
//	    	String sql1 = "select IsSuperAdmin from F9901 where employeenumber ='"+employeenuber+"'";
//	    	Cursor c1 = null;
//	    	Cursor c2 = null;
//	    	try {
//	    		bd1 = new BaseDao<F9901Class>(mcontext);
//	    	    
//	    		c1 = bd1.rawQuery(sql1, null);
//		    	while(c1.moveToNext())
//		    	{
//		    		String superuser = c1.getString(c1.getColumnIndex("IsSuperAdmin"));
//		    		if(superuser.equals("true"))
//		    		{
//		    			strJson = "1";
//		    			return strJson;
//		    		}else
//		    		{
//		    				String sql2 = "select distinct ur.[InnerId] from F990101 as ur,F990201 as ra where ur.[RoleCode] = ra.[RoleCode] and ra.[ModuleCode] like '%"+modulecode+"%' and lower(ur.[UserName]) = '"+username.toLowerCase()+"'";
//		    				c2 = bd1.rawQuery(sql2, null);
//	    					if(c2.getCount()>0)
//	    					{
//	    						strJson = "1";
//	    					}else
//	    						strJson = "0";
//		    		}
//		    	}
//		    	
//			} catch (Exception e) {
//				// TODO: handle exception
//			}finally
//			{
//				if(c1!=null){c1.close();c1=null;};
//				if(c2!=null){c2.close();c2=null;};
//				if(bd1!=null){bd1.close();bd1=null;};
//				
//			}
//	    		    			
//	    	return strJson;
	    }	  
	    public static String NetType(Context context) {
	        try {
	            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	            NetworkInfo info = cm.getActiveNetworkInfo();
	            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
	            if (typeName.equalsIgnoreCase("wifi")) {
	            } else {
	                typeName = info.getExtraInfo().toLowerCase();
	                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
	            }
	            return typeName;
	        } catch (Exception e) {
	            return null;
	        }
	    }
	    
	    /**
	     * 获取当前时间
	     * @return
	     */
	    public static String getCurrentDateTime()
	    {
	        SimpleDateFormat   sDateFormat2   =   new   SimpleDateFormat(Configuration.DATETIMEFORMAT);
	        String   currenttime   =   sDateFormat2.format(new   java.util.Date());
	        return currenttime;
	    }
	    /**
	     * 获取当前时间
	     * @return
	     */
	    public static String getCurrentDate()
	    {
	        SimpleDateFormat   sDateFormat2   =   new   SimpleDateFormat("yyyy-MM-dd");
	        String   currenttime   =   sDateFormat2.format(new   java.util.Date());
	        return currenttime;
	    }
	    //按一定的格式转换时间,传入的时间格式 为 2013-10-24 13:44
	    public static String getFormateDateStr(String formatTime,String strDate)
	    {
	    	String strFormatDate = strDate.trim();
	    	strFormatDate = strFormatDate.length()>19?strFormatDate.substring(0,19):strFormatDate;
	    	SimpleDateFormat formatter = new SimpleDateFormat(formatTime);
	    	SimpleDateFormat formatterorg = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    ParsePosition pos = new ParsePosition(0);
		    Date strtodate = formatterorg.parse(strFormatDate, pos);
		    strFormatDate = formatter.format(strtodate);
	    	return strFormatDate ;
	    }
	  
}
