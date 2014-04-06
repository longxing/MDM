package com.shwootide.mdm.pojo;

//Password@{
//"configType":"Password",
//"configName":"pass_test_android",
//"allowSimple":true,
//"requireAlphanumeric":true,
//"minLength":2,
//"minComplexChars":3,
//"maxPINAgeInDays":3,
//"maxInactivity":15,
//"pinHistory":4,
//"maxGracePeriod":null,
//"maxFailedAttempts":9}

public class Password {
	String configType=null;
	String configName=null;
	String allowSimple=null;
	String requireAlphanumeric=null;
	String minLength=null;
	String minComplexChars=null;
	String maxPINAgeInDays=null;
	String maxInactivity=null;
	String pinHistory=null;
	String maxGracePeriod=null;
	String maxFailedAttempts=null;
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
	public String getAllowSimple() {
		return allowSimple;
	}
	public void setAllowSimple(String allowSimple) {
		this.allowSimple = allowSimple;
	}
	public String getRequireAlphanumeric() {
		return requireAlphanumeric;
	}
	public void setRequireAlphanumeric(String requireAlphanumeric) {
		this.requireAlphanumeric = requireAlphanumeric;
	}
	public String getMinLength() {
		return minLength;
	}
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}
	public String getMinComplexChars() {
		return minComplexChars;
	}
	public void setMinComplexChars(String minComplexChars) {
		this.minComplexChars = minComplexChars;
	}
	public String getMaxPINAgeInDays() {
		return maxPINAgeInDays;
	}
	public void setMaxPINAgeInDays(String maxPINAgeInDays) {
		this.maxPINAgeInDays = maxPINAgeInDays;
	}
	public String getMaxInactivity() {
		return maxInactivity;
	}
	public void setMaxInactivity(String maxInactivity) {
		this.maxInactivity = maxInactivity;
	}
	public String getPinHistory() {
		return pinHistory;
	}
	public void setPinHistory(String pinHistory) {
		this.pinHistory = pinHistory;
	}
	public String getMaxGracePeriod() {
		return maxGracePeriod;
	}
	public void setMaxGracePeriod(String maxGracePeriod) {
		this.maxGracePeriod = maxGracePeriod;
	}
	public String getMaxFailedAttempts() {
		return maxFailedAttempts;
	}
	public void setMaxFailedAttempts(String maxFailedAttempts) {
		this.maxFailedAttempts = maxFailedAttempts;
	}
	

}
