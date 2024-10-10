package com.utomate.utomateTools;


public class AutomationAttributes {
	private String name;
	private boolean headless;
	private String webDriver;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isHeadless() {
		return headless;
	}
	public void setHeadless(boolean headless) {
		this.headless = headless;
	}
	
	public String getWebDriver() {
		return webDriver;
	}
	public void setWebDriver(String webDriver) {
		this.webDriver = webDriver;
	}
}
