package com.utomate.utomateTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.utomate.utomateTools.structs.DriverType;
import com.utomate.utomateTools.structs.Step;
import com.utomate.utomateTools.structs.StepType;

public class JavaFactory {
	private String mainClass;
	private String instructionsFile;
	private String dir;
	private ArrayList<String> imports = new ArrayList<String>();
	private ArrayList<String> initializations = new ArrayList<String>();
	private ArrayList<String> logic = new ArrayList<String>();
	
	public JavaFactory(String dir) {
		this.dir = dir;
	}

	public void createProject(String fileName) {
		this.mainClass = this.dir + "/" + fileName + ".java";
	}
	
	public void createZip(String name) throws IOException {
        String zipFile = dir + "/" + name + ".zip";
		File[] filesToZip = {
        		new File(mainClass),
        		new File(instructionsFile)
        };
		FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);
    	
		for (File f : filesToZip) {
        	System.out.println("Zipping: " + f.getPath());
        	
        	// Stream
            FileInputStream fis = new FileInputStream(f);
            
            // Create file within ZIP
            ZipEntry zipEntry = new ZipEntry(f.getName());
            zos.putNextEntry(zipEntry);
            
            // Write content
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            
            // Release memory
            zos.closeEntry();
            fis.close();
            System.out.println("File zipped: " + f.getPath());
        }
		
        zos.close();
        fos.close();
	}
	
	public void generateInstructions(String fileName) throws IOException {
		String template = "HOW TO RUN:\n\n"
				+ "1. Open command prompt in the project folder\n"
				+ "2. Run the following command: \"java %s.java\"\n"
				+ "\nTroubleshooting\n\n"
				+ "- Make sure you have the latest version of Java installed on your machine.\n"
				+ "- Make sure Java is configured in your system's PATH variables.\n";
		String text = String.format(template, fileName);
		File file = new File(dir + "/instructions.txt");
		FileWriter writer = new FileWriter(file);
		writer.write(text);
		writer.close();
		this.instructionsFile = file.getPath();
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
				initializations.add("WebDriver driver = new ChromeDriver(options)");
			}
			case DriverType.FIREFOX: {
				initializations.add("WebDriver driver = new FirefoxDriver(options)");
			}
		};
		initializations.add("Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10))");
		// For future reference: other advanced settings (e.g. options) go here
	};
	
	private void generateLogic(AutomationAttributes attrs) {
		for (Step step : attrs.getSteps()) {
			switch (step.type) {
				case StepType.FIND: {
					logic.add("wait.until(ExpectedConditions." + step.until + "(By." + step.strategy + "(\"" + step.locator + "\")))");
				}
				case StepType.CLICK: {}
				case StepType.OPEN: {}
				case StepType.LOOP: {}
			}
		}
	};
	
	public void writeCode(AutomationAttributes attributes) throws IOException {		
		// Generate code pieces
		generateImports(attributes);
		generateInitializations(attributes);
		generateLogic(attributes);
		
		// String code pieces together
		FileWriter writer = new FileWriter(mainClass);
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
		
		// Write to file
		writer.write(codeBuffer.toString());
		writer.close();
	}
}
