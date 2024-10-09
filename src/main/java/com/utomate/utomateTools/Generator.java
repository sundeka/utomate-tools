package com.utomate.utomateTools;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Generator {
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/api/generate")
	public Map<String, Object> generateJar(@RequestBody AutomationAttributes attributes) {
		return attributes.getJson();
	}

}