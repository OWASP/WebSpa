package net.seleucus.wsp;

import java.io.File;
import java.io.FileNotFoundException;

public class Config {

	public Config(String configPath) throws FileNotFoundException {
		
		File configFile = new File(configPath);
		if(!configFile.exists()) {
			throw new FileNotFoundException("Config File Not Found: " + configPath);
		}
	}

}
