package com.shwootide.mdm.pojo;

public class FlowrRecord {
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    private long WwanReceive=0;
    private long WwanSend=0;
    private long WifiReceive=0;
    private long WifiSend=0;
    private String StartTime=null;
    private String EndTime="000000";
    public long getWwanReceive() {
        return WwanReceive;
    }
    public void setWwanReceive(long wwanReceive) {
        WwanReceive = wwanReceive;
    }
    public long getWwanSend() {
        return WwanSend;
    }
    public void setWwanSend(long wwanSend) {
        WwanSend = wwanSend;
    }
    public long getWifiReceive() {
        return WifiReceive;
    }
    public void setWifiReceive(long wifiReceive) {
        WifiReceive = wifiReceive;
    }
    public long getWifiSend() {
        return WifiSend;
    }
    public void setWifiSend(long wifiSend) {
        WifiSend = wifiSend;
    }
    public String getStartTime() {
        return StartTime;
    }
    public void setStartTime(String startTime) {
        StartTime = startTime;
    }
    public String getEndTime() {
        return EndTime;
    }
    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
    
}
