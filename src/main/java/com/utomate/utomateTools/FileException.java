package com.utomate.utomateTools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Unknown error handling file.")
public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String id) {
        super(id);
    }
}