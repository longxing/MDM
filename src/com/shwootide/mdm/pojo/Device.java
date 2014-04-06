package com.shwootide.mdm.pojo;

//此类暂时先不用
public class Device {
	private String deviceName;//名字可以随便叫
	private String deviceType;//设备类型，android、ios、winphone等等，甚至更加详细的一些参数，系统型号等都可以列举进来
	private String cid;//第三方消息ID，这个ID也会唯一，没有用户登录这个说法了，所以唯一了
	private String IMEI;//设备唯一识别表示号，上面那个号是用来发送消息推送用的；
	private String phoneNumber;//当有SIM卡的时候获取手机号或者SIM卡卡号
	//根据IMEI还可以获取到很多手机制造商，本手机相关的一些参数信息
	//http://wenku.baidu.com/link?url=3ap3tRGKqxyLKfaztnEf8IpxT8XXapexoczwZSa3psc-5PVmq8TfnUHC6azBhHok01IgmDbD3IeRXgPPyl9JIgZfX75EW3YoSfEre2jDT5q
	
	
	public void Device()
	{
		//构造子，这里重写了系统构造子，什么也不干
	}
	
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;//设置设备类型
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
	
	
	

}
