package com.shwootide.mdm.pojo;

public class News {
    	private int id;
    	public int getId() {
	    return id;
	}
	public void setId(int id) {
	    this.id = id;
	}
	private String dt="2012";
    	private String HtmlFile=null;
	private String Icon=null;
	private String Summary=null;
	private String Title=null;
	
	public String getDt() {
	    return dt;
	}
	public void setDt(String dt) {
	    this.dt = dt;
	}
	
	public String getHtmlFile() {
	    return HtmlFile;
	}
	public void setHtmlFile(String htmlFile) {
	    HtmlFile = htmlFile;
	}
	public String getIcon() {
	    return Icon;
	}
	public void setIcon(String icon) {
	    Icon = icon;
	}
	public String getSummary() {
	    return Summary;
	}
	public void setSummary(String summary) {
	    Summary = summary;
	}
	public String getTitle() {
	    return Title;
	}
	public void setTitle(String title) {
	    Title = title;
	}

	
}
