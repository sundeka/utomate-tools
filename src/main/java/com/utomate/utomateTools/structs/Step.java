package com.utomate.utomateTools.structs;

public class Step {
	public int id;
	public StepType type;
	public Step details;
	public String strategy;
	public String locator;
	public String until;
	
	// Find
	public Step(int id, String type, String strategy, String locator, String until) {
		this.id = id;
		this.type = StepType.FIND;
		this.strategy = strategy;
		this.locator = locator;
		switch (until) {
			case "presence": this.until = "presenceOfElementLocated";
			case "visibility": this.until = "visibilityOfElementLocated";
		}
	}
}
