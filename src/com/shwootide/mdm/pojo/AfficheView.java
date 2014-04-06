package com.shwootide.mdm.pojo;

import com.shwootide.mdm.pojo.Attachments;

public class AfficheView {
	private String json=null;
    public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private String Headline=null;
    private String AnnounceContent=null;
    private String PublishTime=null;
    private  Attachments[] Attachments=null;
 
    public Attachments[] getAttachments() {
        return Attachments;
    }
    public void setAttachments(Attachments[] attachments) {
        Attachments = attachments;
    }
    private String UserName=null;
   
    public String getHeadline() {
        return Headline;
    }
    public void setHeadline(String headline) {
        Headline = headline;
    }
    public String getAnnounceContent() {
        return AnnounceContent;
    }
    public void setAnnounceContent(String announceContent) {
        AnnounceContent = announceContent;
    }
    public String getPublishTime() {
        return PublishTime;
    }
    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
}
