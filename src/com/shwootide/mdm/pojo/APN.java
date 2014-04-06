package com.shwootide.mdm.pojo;

public class APN {
    //{"configType":"APN","configName":"jackapn","apn":"asdg","username":"sdf","password":"sdf","proxy":"sdf","proxyPort":"sdf"}
    private String configType=null;
    private String configName=null;
    private String proxy=null;
    private String password=null;
    private String username=null;
    private String apn=null;
    private String proxyPort=null;
    public String getConfigType() {
        return configType;
    }
    public void setConfigType(String configType) {
        this.configType = configType;
    }
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public String getProxy() {
        return proxy;
    }
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getApn() {
        return apn;
    }
    public void setApn(String apn) {
        this.apn = apn;
    }
    public String getProxyPort() {
        return proxyPort;
    }
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
    
}
