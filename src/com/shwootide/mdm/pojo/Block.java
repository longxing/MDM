package com.shwootide.mdm.pojo;

public class Block {
	private String name;
	private long blockSize  ;
	private long blockCount  ;
	private long availCount  ;
	
	//总大小
	private String totalCount;
	//剩余大小
	private String remainCount;
	
	
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getRemainCount() {
		return remainCount;
	}
	public void setRemainCount(String remainCount) {
		this.remainCount = remainCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(long blockSize) {
		this.blockSize = blockSize;
	}
	public long getBlockCount() {
		return blockCount;
	}
	public void setBlockCount(long blockCount) {
		this.blockCount = blockCount;
	}
	public long getAvailCount() {
		return availCount;
	}
	public void setAvailCount(long availCount) {
		this.availCount = availCount;
	}
	
	

}
