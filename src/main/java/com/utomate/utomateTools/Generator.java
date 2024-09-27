package com.utomate.utomateTools;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Generator {

	@PostMapping("/api/generate")
	public String generateJar() {
		return "JAR generated!";
	}

}