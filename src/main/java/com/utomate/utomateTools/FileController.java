package com.utomate.utomateTools;

import java.io.IOException;
import java.io.InputStream;

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
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/generate")
	public String generateFile(@RequestBody AutomationAttributes attributes) {
		return "/api/download/e393149e-774a-4ba3-a8c5-b2e4fe69f383";
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/download/{id}")
	public byte[] getFile(@PathVariable("id") String id) throws IOException {
		InputStream in = getClass().getResourceAsStream(id + ".txt");
		return IOUtils.toByteArray(in);
	}
}