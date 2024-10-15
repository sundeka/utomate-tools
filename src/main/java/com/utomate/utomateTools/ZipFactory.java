package com.utomate.utomateTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

public class ZipFactory {
	
	String uuid;
	
	public ZipFactory() {
		this.uuid = UUID.randomUUID().toString();
	}
	
	public Path initFolder() {
		Path zipDir = Paths.get("src/main/resources/files/" + this.uuid);
		new File(zipDir.toString()).mkdir();
		System.out.println("Created new folder at: " + zipDir.toString());
		return zipDir;
	}
	
	/*
	 * Create a .zip file at src/main/resources/files
	 * 
	 * -- Example --
	 * dir = src/main/resources/files/aaaa-bb-cc-dddd
	 * filesFolder = src/main/resources/files
	 * zipName = aaaa-bb-cc-dddd.zip
	 * zipFile = src/main/resources/files/aaaa-bb-cc-dddd.zip
	 */
	public Path createZip(Path dir) {
		Path filesFolder = dir.getParent();
		String zipName = dir.getFileName() + ".zip";
		Path zipFile = Paths.get(filesFolder.toString(), zipName);
		try (FileOutputStream fos = new FileOutputStream(zipFile.toFile())) {
			System.out.println("Created a new .ZIP file at: " + zipFile.toString());
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			zipOut.close();
	        fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        //File fileToZip = new File(sourceFile);
        //zipFile(fileToZip, fileToZip.getName(), zipOut);
        //
		return zipFile;
	}
	
	public void deleteZip(Path zipPath) {
		if (zipPath.toFile().exists()) {
			zipPath.toFile().delete();
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
		System.out.println("Subfiles deleted. Deleting main folder: " + file.toString());
		file.delete();
	}
	
	private void zipFile() {
		
	}
}
