package com.utomate.utomateTools;

import java.io.ByteArrayInputStream;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;

public class AzureController {
	private final BlobContainerClient blobContainerClient;
	
	public AzureController() {
		TokenCredential creds = new DefaultAzureCredentialBuilder().build();
		this.blobContainerClient = new BlobContainerClientBuilder()
			    .endpoint("https://utomatefiles.blob.core.windows.net/utomatefiles")
			    .credential(creds)
			    .containerName("utomate-blob-container")
			    .buildClient();
		System.out.println("AzureController: Successfully created BlobContainerClient");
	}
	
	public AzureBlob initDataObject() {
		return new AzureBlob();
	}
	
	public void upload(ByteArrayInputStream stream, String fileName, int size) {
		BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();
		System.out.println("AzureController: BlockBlobClient initialized.");
		blockBlobClient.upload(stream, size);
		System.out.println("AzureController: Blob uploaded!");
	}
}
