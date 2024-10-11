package com.utomate.utomateTools;

import java.util.ArrayList;

import com.utomate.utomateTools.structs.Driver;
import com.utomate.utomateTools.structs.Step;
import com.utomate.utomateTools.structs.StepType;

public class AutomationAttributes {
	private String name;
	private boolean headless;
	private Driver webdriver;
	private ArrayList<Step> steps;
	
	// Default getters & setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public boolean isHeadless() { return headless; }
	public void setHeadless(boolean headless) { this.headless = headless; }
	public Driver getWebdriver() { return webdriver; }
	public void setWebdriver(Driver webdriver) { this.webdriver = webdriver; }
	public ArrayList<Step> getSteps() { return steps; }
	public void setSteps(ArrayList<Step> steps) { this.steps = steps; }
	
	// Custom methods
	public ArrayList<StepType> getAllUniqueStepTypes() {
		ArrayList<StepType> uniques = new ArrayList<>();
		for (Step step : steps) {
			boolean found = false;
			for (StepType u : uniques) {
				if (u.equals(step.type)) {
					found = true;
					break;
				}
			}
			if (!found) {
				uniques.add(step.type);
			}
		}
		return uniques;
	}
}
