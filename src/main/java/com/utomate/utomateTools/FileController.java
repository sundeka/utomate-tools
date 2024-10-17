package com.utomate.utomateTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

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
	public String generateFile(@RequestBody AutomationAttributes attributes) throws IOException {
		// Init variables relevant to try-block
		File folder = null;
		File zipFile = null;
		
		// Init clients
		JavaFactory java = new JavaFactory();
		DirTool dirTool = new DirTool();
		AzureController azure = new AzureController();
		
		try {
			// Create local temporary files
			folder = dirTool.initFolder();
			zipFile = dirTool.initZip(folder);
			File[] javaArtifacts = java.createProjectFiles(folder, attributes);
			dirTool.zipFiles(zipFile, javaArtifacts);
			
			// Upload files to Azure as a blob
			azure.upload(zipFile);
		} catch (IOException e) {
			throw e;
		} finally {
			if (folder != null) {
				dirTool.deleteDir(folder);
			}
			if (zipFile != null) {
				dirTool.deleteZip(zipFile);
			}
		}
		return "/api/download/" + dirTool.uuid;
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