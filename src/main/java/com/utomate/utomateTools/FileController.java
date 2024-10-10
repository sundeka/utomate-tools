package com.utomate.utomateTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FileController {
	
	private static final String path = "src/main/resources/files/";
	private static final String fileFormat = ".jar";
	
	private void createFile(String file, AutomationAttributes attributes) throws IOException {
		System.out.println("Creating the following file: " + file.toString());
		JavaFactory java = new JavaFactory();
		FileWriter writer = new FileWriter(file);
		writer.write(java.getCode());
		writer.close();
		System.out.println("File created!");
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/generate")
	public String generateFile(@RequestBody AutomationAttributes attributes) {
		String uuid = UUID.randomUUID().toString();
		new File(path + uuid).mkdirs();
		String file = path + uuid + "/" + attributes.getName() + fileFormat;
		try {
			createFile(file, attributes);
		} catch (IOException e) {
			throw new FileException(e.getMessage());
		}
		return "/api/download/" + uuid;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/download/{id}")
	public byte[] getFile(@PathVariable("id") String id) throws IOException {
		File folder = new File(path + id);
		File[] files = folder.listFiles();
		if (files.length!=1) { 
			throw new FileException(id); 
		}
		try (InputStream in = new FileInputStream(files[0])) {
	        byte[] byteArray = IOUtils.toByteArray(in);
	        return byteArray;
	    } catch (FileNotFoundException e) {
	        throw new FileException(id);
	    } catch (IOException e) {
	        throw new IOException("Error reading file: " + files[0].getAbsolutePath(), e);
	    }
	}
}