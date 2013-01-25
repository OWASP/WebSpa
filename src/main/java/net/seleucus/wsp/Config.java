package net.seleucus.wsp;

import java.io.File;
import java.io.FileNotFoundException;

public class Config {

	public Config(String configPath) throws FileNotFoundException {
		
		if(configPath == null) {
			throw new FileNotFoundException("Properties File Cannot Be Null");
		}
		
		File configFile = new File(configPath);
		File parentDirectory = configFile.getParentFile();
		
		if(parentDirectory == null) {
			throw new FileNotFoundException("Config Directory Not Found: " + configPath);
		}
		
		if(!parentDirectory.getName().equalsIgnoreCase("config")) {
			throw new FileNotFoundException("Config Directory Not Called \"config\": " + configPath);
		}
			
		if(!configFile.exists()) {
			throw new FileNotFoundException("Properties File Not Found: " + configPath);
		}
		
		if(!configFile.canRead()) {
			throw new FileNotFoundException("Properties File Cannot Be Read: " + configPath);
		}
		
		if(!configFile.getName().equalsIgnoreCase("web-spa-properties.config")) {
			throw new FileNotFoundException("Properties File Not Called \"web-spa-properties.config\" " + configPath);
		}

		// Check if the file is a symbolic link
	}

}
