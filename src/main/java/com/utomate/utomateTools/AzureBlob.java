package com.utomate.utomateTools;

import java.util.UUID;

public class AzureBlob {
	private String blobName;
	private String fileName;
	private String code;
	private String instructions;
	
	public AzureBlob() {
		this.setBlobName(UUID.randomUUID().toString());
	}
	
	@Override
	public String toString() {
		// TODO: JSON representation goes here!
		return "{\"name\": \"foobaz\"}";
	}
	
	public byte[] getBytes() {
		return this.toString().getBytes();
	}
	
	public int length() {
		return this.toString().length();
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getBlobName() {
		return blobName;
	}

	public void setBlobName(String blobName) {
		this.blobName = blobName;
	}
}
