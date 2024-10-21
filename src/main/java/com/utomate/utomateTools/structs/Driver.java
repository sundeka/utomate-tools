package com.utomate.utomateTools.structs;

public class Driver {
	public DriverType type;
	
	public Driver(String type) {
		switch (type) {
			case "chrome": {
				this.type = DriverType.CHROME;
				return;
			}
			case "firefox": {
				this.type = DriverType.FIREFOX;
				return;
			}
		}
	}
}