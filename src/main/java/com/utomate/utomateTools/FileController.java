package com.utomate.utomateTools;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/generate")
	public String generateFile(@RequestBody AutomationAttributes attributes) {
		// Initialize clients
		JavaFactory java = new JavaFactory();
		ZipFactory zipper = new ZipFactory();
		AzureController azure = new AzureController();
		
		// File operations
		Path folder = zipper.initFolder();
		Path zipFile = zipper.createZip(folder);
		
		// Stream to Azure
		azure.upload(zipFile);
		
		// Delete temporary local files
		zipper.deleteZip(zipFile);
		zipper.deleteDir(folder.toFile());
		
		return "/api/download/" + zipper.uuid;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/download/{id}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable("id") String id) {
		AzureController azure = new AzureController();
		ByteArrayOutputStream stream = azure.download(id + ".zip");
	    ByteArrayResource resource = new ByteArrayResource(stream.toByteArray());
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".zip\"")
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .contentLength(stream.size())
	            .body(resource);
	}
}