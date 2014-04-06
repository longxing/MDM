package com.shwootide.mdm.pojo;

public class Log {
	private int id;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String LogId=null;
    private String LogName=null;
    private String RecordTime=null;
    private boolean IsSuccess;
    public String getLogId() {
        return LogId;
    }
    public void setLogId(String logId) {
        LogId = logId;
    }
    public String getLogName() {
        return LogName;
    }
    public void setLogName(String logName) {
        LogName = logName;
    }
    public String getRecordTime() {
        return RecordTime;
    }
    public void setRecordTime(String recordTime) {
        RecordTime = recordTime;
    }
    public boolean isIsSuccess() {
        return IsSuccess;
    }
    public void setIsSuccess(boolean isSuccess) {
        IsSuccess = isSuccess;
    }
  
   
}
