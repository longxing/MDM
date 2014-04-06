package com.shwootide.mdm.pojo;

public class Attachments {
	private int id;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String Name;
    private String CreateTime;
    private String DownloadUrl;
    private String Size;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getCreateTime() {
        return CreateTime;
    }
    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
    public String getDownloadUrl() {
        return DownloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }
    public String getSize() {
        return Size;
    }
    public void setSize(String size) {
        Size = size;
    }
    
}
