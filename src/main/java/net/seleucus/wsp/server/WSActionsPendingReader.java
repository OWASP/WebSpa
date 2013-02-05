package net.seleucus.wsp.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class WSActionsPendingReader {

	public WSActionsPendingReader(final String actionsPendingPath) throws IOException {
		
		URL rootLocation = ClassLoader.getSystemResource(actionsPendingPath);
		if(rootLocation == null) {
			throw new FileNotFoundException("Web-Spa Actions Pending File Not Found: " + actionsPendingPath);
		}
		
		File actionFile = new File(rootLocation.getFile());
		if(!actionFile.canRead()) {
			throw new IOException("Web-Spa Actions Pending File Cannot Be Read");
		}
	}
}
