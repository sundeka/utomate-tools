package com.utomate.utomateTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DirTool {
	String uuid;
	Path filesDir = Paths.get("src/main/resources/files");
	File seleniumJavaJar = new File("src/main/resources/java_project_resources/selenium-java-4.21.0.jar");
	File seleniumChromeDriverJar = new File("src/main/resources/java_project_resources/selenium-chrome-driver-4.21.0.jar");
	File javaFile;
	File instructionsFile;
	
	public DirTool() {
		this.uuid = UUID.randomUUID().toString();
		this.javaFile = new File(filesDir.toString() + "/" + this.uuid + "/" + "Main.java");
		this.instructionsFile = new File(filesDir.toString() + "/" + this.uuid + "/" + "instructions.txt");
	}
	
	public File initFolder() {
		Path projectFolder = Paths.get(filesDir.toString() + "/" + this.uuid);
		new File(projectFolder.toString()).mkdir();
		System.out.println("Created new folder at: " + projectFolder.toString());
		return projectFolder.toFile();
	}
	
	/*
	 * Create a .zip file at src/main/resources/files
	 * 
	 * -- Example --
	 * projectFolder = src/main/resources/files/aaaa-bb-cc-dddd
	 * filesFolder = src/main/resources/files
	 * zipName = aaaa-bb-cc-dddd.zip
	 * zipFile = src/main/resources/files/aaaa-bb-cc-dddd.zip
	 */
	public File initZip(File projectFolder) throws IOException {
		Path filesFolder = projectFolder.toPath().getParent();
		String zipName = projectFolder.toPath().getFileName() + ".zip";
		Path zipFile = Paths.get(filesFolder.toString(), zipName);
		FileOutputStream fos = new FileOutputStream(zipFile.toFile());
		System.out.println("Created a new .ZIP file at: " + zipFile.toString());
		fos.close();
		return zipFile.toFile();
	}
	
	public void zipFiles(File zipFile, File[] filesToZip) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
	    ZipOutputStream out = new ZipOutputStream(fos);
	    
	    // TODO:
	    // new File("src/main/resources/templates/rpa-project")
	    // i.e. write the template dir to the zip
	    // then change the below code to append to the existing zip
	    
	    for (File artifact : filesToZip) {
	    	FileInputStream fis = new FileInputStream(artifact);
		    ZipEntry zipEntry = new ZipEntry(artifact.getName());
		    out.putNextEntry(zipEntry);
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = fis.read(buffer)) > 0) {
		        out.write(buffer, 0, length);
		    }
		    out.closeEntry();
		    fis.close();
		    System.out.println("Zipped file: " + artifact.toString());
	    }
	    out.close();
	    fos.close();
	}
	
	public void deleteZip(File zipPath) {
		if (zipPath.exists()) {
			zipPath.delete();
			System.out.println("Deleted .ZIP file: " + zipPath.toString());
		}
	}
	
	public void deleteDir(File file) {
		for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDir(subfile);
            }
            System.out.println("Deleting file: " + subfile.toString());
            subfile.delete();
        }
		System.out.println("Deleting main folder: " + file.toString());
		file.delete();
	}
}
