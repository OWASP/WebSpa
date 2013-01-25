package net.seleucus.wsp.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class FileChecker {
	
	private File currentFile;
	private File parentDirectory;

	public FileChecker(String filePath) throws IOException, FileNotFoundException {
		
		if(filePath == null) {
			throw new FileNotFoundException("File Path Cannot Be Null");
		}
		
		if(filePath.isEmpty()) {
			throw new FileNotFoundException("File Path Cannot Be Empty");
		}

		currentFile = new File(filePath);
		parentDirectory = currentFile.getParentFile();
		
		String fileExtension = FilenameUtils.getExtension(filePath);
		
		if(!fileExtension.equalsIgnoreCase(parentDirectory.getName())) {
			throw new IOException("File Extension is Not Identical to Directory Name");
		}
		
	
	}

	
}
