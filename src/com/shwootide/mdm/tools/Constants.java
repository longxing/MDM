package com.shwootide.mdm.tools;

//���г�����
public class Constants {
	public static final String DBSchemaPath = "schema";
	public static final String DatabaseFile = "shwootide_mdm.db";
	//public static final String ServerHost = "http://mdm.mypatroller.com"; //����Ƿ���Զ�̷�������
	//public static final String ServerHost = "http://192.168.0.194";
	public static final String ServerIP = "211.144.201.245";//����IMͨ�ŵĹ���
	
	public static final String DOWNLOAD_ATTACHMENT_URL = "http://enterprise.mypatroller.com/public/download/";
	public static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
//	public static final String ServerHost = "http://192.168.0.121:8083/";
	public static final String ServerStart = "http://";
	public static final String ServerHost = ".app.mypatroller.com/"; //����Ƿ�������ַ��������Ҫ�ٸĵĲ���
	public static final String UPDATE_SERVER = "http://app.mdm.mypatroller.com/mdmapp/";//����apk���·��
	public static final String UPDATE_VER = "ver.json";
	public static final String UPDATE_APKNAME = "mdm.apk";
	public static final String UPDATE_SAVENAME = "mdm.apk";
	
	public static final String CHANNEL_NAME = "C50a211ee5906b";
	
	public static final String postImageUrl = ServerHost+"/Webservices/AndroidUploadImage.ashx";
	public static final String postHeadUrl = ServerHost+"/Webservices/AndroidUploadHead.ashx"; //�ϴ�����Ƭ��post·��
	public static final String getImageUrl = ServerHost+"/webservices/imagehandler.ashx?url="+ServerHost; 
	public static final String getImageWidth = "&d=";
	public static final String getImageHeighth = "&h=";
	public static final String localImagesPath = "/mypatroller/images/";
	public static final int DatabaseVersion = 1;
	/**�ֺŷָ���*/
	public static final String SemicolonSeparator = ";";
	/**���ŷָ���*/
	public static final String CommaSeparator = ",";
	public static int oldVersion = -1;
	public static final int PAGE_COUNT = 10;
	
	
	public static boolean debugIsOn = true;//�����汾��ʱ���޸�Ϊfalse,ƽʱ�����޸�Ϊtrue
	
	public static String SERVER_IP = "127.0.0.1";
	public static int SERVER_PORT = 8080;
	public static String imei = "imei";
	public static String deviceClientId = "deviceClientId";
	public static String softwareInfo = "softwareInfo";
	public static String appinfo = "appinfo";
	public static String LOGUUID = "LOGUUID";
	

}
