package com.utomate.utomateTools;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

public class AzureController {
	private final BlobContainerClient blobContainerClient;
	
	public AzureController() {
		TokenCredential creds = new DefaultAzureCredentialBuilder().build();
		this.blobContainerClient = new BlobContainerClientBuilder()
			    .endpoint("https://utomatefiles.blob.core.windows.net/utomatefiles")
			    .credential(creds)
			    .containerName("utomate-blob-container")
			    .buildClient();
	}
	
	public void upload(File zipFile) {
		BlobClient blobClient = blobContainerClient.getBlobClient(zipFile.getName());
		blobClient.uploadFromFile(zipFile.toString());
		System.out.println("Uploaded \"" + zipFile.toString() + "\" as a blob to Azure!");
	}
	
	public ByteArrayOutputStream download(String blobName) {
		BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    blobClient.downloadStream(outputStream);
	    return outputStream;
	}
}
