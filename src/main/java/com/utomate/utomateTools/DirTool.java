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
	
	public DirTool() {
		this.uuid = UUID.randomUUID().toString();
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
	
	/**
	 * Recursively zip a directory.
	 * @param dir
	 * @param out
	 * @throws IOException
	 */
	private void zipDir(String rootFolderName, File dir, ZipOutputStream out) throws IOException {
		for (File f : dir.listFiles()) {
			if (!f.isDirectory()) {
				ZipEntry zipEntry;
				if (f.getName().endsWith((".jar"))) {
					zipEntry = new ZipEntry(rootFolderName + "/" + "lib" + "/" + f.getName());
				} else {
					zipEntry = new ZipEntry(rootFolderName + "/" + f.getName());
				}
			    out.putNextEntry(zipEntry);
			    FileInputStream fis = new FileInputStream(f);
			    byte[] bytes = new byte[1024];
			    int length;
			    while ((length = fis.read(bytes)) >= 0) {
			        out.write(bytes, 0, length);
			    }
			    fis.close();
			} else {
				zipDir(rootFolderName, f, out);
			}
		}
		
	}
	
	/**
	 * Zip all project-related files together. 
	 * @param zipFile
	 * @param filesToZip
	 * @throws IOException
	 */
	public void zipFiles(String robotName, File zipFile, File[] filesToZip) throws IOException {
		// Open streams
		final FileOutputStream fos = new FileOutputStream(zipFile);
	    ZipOutputStream out = new ZipOutputStream(fos);
	    
	    // Set up a root folder with the robot's name (ALL files go here)
	    // The zip file itself will be a UUID for the sake of Azure blob storage
	    ZipEntry rootFolder = new ZipEntry(robotName + "/");
	    out.putNextEntry(rootFolder);
	    
	    // Static template files
	    File rpaProjectTemplate = new File("src/main/resources/templates/rpa-project");
	    zipDir(robotName, rpaProjectTemplate, out);
	    System.out.println("Added RPA project template to the ZIP!");
	    
	    // User-generated files
	    for (File artifact : filesToZip) {
	    	FileInputStream fis = new FileInputStream(artifact);
		    ZipEntry zipEntry = new ZipEntry(robotName + "/" + artifact.getName());
		    out.putNextEntry(zipEntry);
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = fis.read(buffer)) > 0) {
		        out.write(buffer, 0, length);
		    }
		    out.closeEntry();
		    fis.close();
	    }
	    System.out.println("Added Java code to the ZIP!");
	    
	    // Close streams
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
