package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang3.CharUtils;

public class WSActionsPendingReader {

	public WSActionsPendingReader(final String actionsPendingPath) throws IOException, InvalidActionsPendingFileException {
		
		URL rootLocation = ClassLoader.getSystemResource(actionsPendingPath);
		if(rootLocation == null) {
			throw new FileNotFoundException("Web-Spa Actions Pending File Not Found: " + actionsPendingPath);
		}
		
		File actionFile = new File(rootLocation.getFile());
		if(!actionFile.canRead()) {
			throw new IOException("Web-Spa Actions Pending File Cannot Be Read");
		}
		
		checkFile(actionFile);
	}

	private void checkFile(File actionFile) throws IOException, InvalidActionsPendingFileException {

		BufferedReader in = new BufferedReader(new FileReader(actionFile));
		final StringBuffer fileContents = new StringBuffer();
		int charCounter = 0;
		int c;
		while (((c = in.read()) > 0) && (charCounter < Character.MAX_VALUE)) {
			// Allow the character only if its printable ASCII or \n
			if ((CharUtils.isAsciiPrintable((char) c)) || (((char) c) == '\n')) {
				fileContents.append((char) c);
			}
			charCounter++;
		}
		in.close();
		
		if(charCounter == Character.MAX_VALUE) {
			throw new InvalidActionsPendingFileException("Web-Spa Actions Pending File Has More Than 65535 Characters");
		}
		
		final String[] fileContentsArray = fileContents.toString().split("\n");
		final int fileNoOfLines = fileContentsArray.length;

		if(fileNoOfLines > 1024) {
			throw new InvalidActionsPendingFileException("Web-Spa Actions Pending File Has More Than 1024 Lines");
		}

	}
}
