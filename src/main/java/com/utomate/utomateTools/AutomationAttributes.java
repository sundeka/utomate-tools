package com.utomate.utomateTools;

import java.util.HashMap;
import java.util.Map;

public class AutomationAttributes {
	private String name;

	public Map<String, Object> getJson() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name", this.getName());
		return m;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
