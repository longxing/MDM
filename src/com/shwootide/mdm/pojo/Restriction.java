package com.shwootide.mdm.pojo;

//Restriction@{"configType":"Restriction","configName":"test_restriction_A","allowInstalApp":true,"allowUseCamera":true,"allowBluetooth":true,"allowDataRoam":true}

public class Restriction {
	String configType;
	String configName;
	String allowInstalApp;
	String allowUseCamera;
	String allowBluetooth;
	String allowDataRoam;
	
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
	public String getAllowInstalApp() {
		return allowInstalApp;
	}
	public void setAllowInstalApp(String allowInstalApp) {
		this.allowInstalApp = allowInstalApp;
	}
	public String getAllowUseCamera() {
		return allowUseCamera;
	}
	public void setAllowUseCamera(String allowUseCamera) {
		this.allowUseCamera = allowUseCamera;
	}
	public String getAllowBluetooth() {
		return allowBluetooth;
	}
	public void setAllowBluetooth(String allowBluetooth) {
		this.allowBluetooth = allowBluetooth;
	}
	public String getAllowDataRoam() {
		return allowDataRoam;
	}
	public void setAllowDataRoam(String allowDataRoam) {
		this.allowDataRoam = allowDataRoam;
	}
	
	

}
