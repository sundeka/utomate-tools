package com.utomate.utomateTools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/generate")
	public String generateFile(@RequestBody AutomationAttributes attributes) {
		// Initialize clients
		JavaFactory java = new JavaFactory();
		AzureController azure = new AzureController();
		
		// Init blob data
		AzureBlob blobData = azure.initDataObject();
		try {
			blobData.setFileName(attributes.getName());
			blobData.setCode(java.writeCode(attributes));
			blobData.setInstructions(java.writeInstructions(attributes.getName()));
		} catch (IOException e) {
			throw new FileException(e.getMessage());
		}
		System.out.println("AzureBlob created successfully with the following content:");
		System.out.println(blobData.toString());
		
		// Upload blob
		try (ByteArrayInputStream stream = new ByteArrayInputStream(blobData.getBytes())) {
			azure.upload(
					stream, 
					blobData.getBlobName() + ".json", 
					blobData.length()
				);
		} catch (IOException e) {
			throw new FileException(e.getMessage());
		}
		
		return "/api/download/" + blobData.getBlobName();
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/download/{id}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable("id") String id) throws IOException {
		File folder = new File(path + id);
		File zip = null;
		for (File f : folder.listFiles()) {
			if (f.getName().contains(".zip")) {
				zip = f;
				break;
			}
		}
		
		if (zip==null) throw new FileException(id); 
		
		try (InputStream in = new FileInputStream(zip)) {
	        byte[] byteArray = IOUtils.toByteArray(in);
	        ByteArrayResource resource = new ByteArrayResource(byteArray);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zip.getName());
	        headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
	        return ResponseEntity.ok().headers(headers).contentLength(byteArray.length).body(resource);
	    } catch (IOException e) {
	        throw new IOException("Error reading zip file", e);
	    }
	}
}