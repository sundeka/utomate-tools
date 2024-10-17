package com.utomate.utomateTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.utomate.utomateTools.structs.DriverType;
import com.utomate.utomateTools.structs.Step;
import com.utomate.utomateTools.structs.StepType;

public class JavaFactory {
	private ArrayList<String> imports = new ArrayList<String>();
	private ArrayList<String> initializations = new ArrayList<String>();
	private ArrayList<String> logic = new ArrayList<String>();
	
	public File[] createProjectFiles(File folder, AutomationAttributes attributes) throws IOException {
		File[] javaArtifacts = {
				writeInstructionsFile(folder, attributes.getName()),
				writeCodeFile(folder, attributes)
		};
		return javaArtifacts;
	}
	
	public File writeInstructionsFile(File folder, String fileName) throws IOException {
		String template = String.format("HOW TO RUN:\n\n"
				+ "1. Double-click the file called \"run\" in the project folder. \n"
				+ "\nTroubleshooting\n\n"
				+ "- Make sure you have the latest version of Java installed on your machine.\n"
				+ "- Make sure Java is configured in your system's PATH variables (see: https://stackoverflow.com/a/44406470)\n", fileName);
		File instructionsFile = new File(folder + "/" + "instructions.txt");
		FileWriter writer = new FileWriter(instructionsFile);
		writer.write(template);
		writer.close();
		return instructionsFile;
	}
	
	public File writeCodeFile(File folder, AutomationAttributes attributes) throws IOException {		
		// Generate code pieces
		generateImports(attributes);
		generateInitializations(attributes);
		generateLogic(attributes);
		
		// String code pieces together
		StringBuilder codeBuffer = new StringBuilder();
		
		// Imports
		for (String imp : imports) {
			codeBuffer.append("import " + imp + ";\n");
		}
		
		// Main class & method declarations
		codeBuffer.append("\npublic class Main {\n\tpublic static void main(String[] args) {\n");
		
		// Initializations
		codeBuffer.append("\t\t// Initialization\n");
		for (String init : initializations) {
			codeBuffer.append("\t\t" + init + ";\n");
		}
		codeBuffer.append("\n");
		
		// Logic
		codeBuffer.append("\t\t// Logic\n");
		for (String step : logic) {
			codeBuffer.append("\t\t" + step + ";\n");
		}
		codeBuffer.append("\n");
		
		// Ending brackets
		codeBuffer.append("\t}\n}");
		
		// Create the file
		File codeFile = new File(folder + "/" + attributes.getName() + ".java");
		FileWriter writer = new FileWriter(codeFile);
		writer.write(codeBuffer.toString());
		writer.close();
		return codeFile;
	}
	
	private void generateImports(AutomationAttributes attrs) {
		// Defaults
		imports.add("java.time.Duration");
		imports.add("org.openqa.selenium.WebDriver");
		imports.add("org.openqa.selenium.support.ui.WebDriverWait");
		
		// WebDriver import
		switch (attrs.getWebdriver().type) {
			case DriverType.CHROME: {
				imports.add("org.openqa.selenium.chrome.ChromeDriver");
			}
			case DriverType.FIREFOX: {};
			default: break;
		}
		
		// Get all needed imports depending how many different step types there are
		for (StepType step : attrs.getAllUniqueStepTypes()) {
			if (step==StepType.FIND) { 
				imports.add("org.openqa.selenium.By");
				imports.add("org.openqa.selenium.support.ui.ExpectedConditions");
			};
			if (step==StepType.CLICK) {}
			if (step==StepType.OPEN) {}
			if (step==StepType.LOOP) {}
		}
	}
	
	private void generateInitializations(AutomationAttributes attrs) {
		switch (attrs.getWebdriver().type) {
			case DriverType.CHROME: {
				initializations.add("WebDriver driver = new ChromeDriver()");
			}
			case DriverType.FIREFOX: {
				initializations.add("WebDriver driver = new FirefoxDriver()");
			}
		};
		initializations.add("Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10))");
		// For future reference: other advanced settings (e.g. options) go here
	};
	
	private void generateLogic(AutomationAttributes attrs) {
		for (Step step : attrs.getSteps()) {
			switch (step.type) {
				case StepType.FIND: {
					logic.add("wait.until(ExpectedConditions."+ step.until + "(By." + step.strategy + "(\"" + step.locator.replace("\"", "'") + "\")))");
				}
				case StepType.CLICK: {}
				case StepType.OPEN: {}
				case StepType.LOOP: {}
			}
		}
	};
}
