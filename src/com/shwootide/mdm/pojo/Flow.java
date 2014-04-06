package com.shwootide.mdm.pojo;

public class Flow {
	private String name;
//	private long tx;//上传流量
//	private long rx;//下载流量
//	private long totalX;//总流量
	
	String tx_;
	String rx_;
	String totalX_;
	
	
	
	
	
	//wifi上传 下载流量
	//非wifi上传 下载流量
	
	
	public String getTotalX_() {
		return totalX_;
	}
	public void setTotalX_(String totalX_) {
		this.totalX_ = totalX_;
	}
	public String getTx_() {
		return tx_;
	}
	public void setTx_(String tx_) {
		this.tx_ = tx_;
	}
	public String getRx_() {
		return rx_;
	}
	public void setRx_(String rx_) {
		this.rx_ = rx_;
	}
//	public long getTotalX() {
//		return totalX;
//	}
//	public void setTotalX(long totalX) {
//		this.totalX = totalX;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public long getTx() {
//		return tx;
//	}
//	public void setTx(long tx) {
//		this.tx = tx;
//	}
//	public long getRx() {
//		return rx;
//	}
//	public void setRx(long rx) {
//		this.rx = rx;
//	}
	

}
