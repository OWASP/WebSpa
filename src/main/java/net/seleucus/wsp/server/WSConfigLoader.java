package net.seleucus.wsp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WSConfigLoader {

	private static final String CONFIG_PATH = "config/web-spa-server-properties.config";
	
	private Properties configProperties;
	
	public WSConfigLoader() throws IOException {
		
		File configFile = new File(CONFIG_PATH);
		
		if(!configFile.canRead()) {
			throw new IOException("Web-Spa Configuration File Cannot Be Read");
		}
		
		FileInputStream in = new FileInputStream(CONFIG_PATH);
		configProperties.load(in);
		in.close();
		
	}
	
	public String getAccessLogFileLocation() {
		return configProperties.getProperty("access-log-file");
	}
	
	public String getLoggingRegexForEachRequest() {
		return configProperties.getProperty("logging-regex-for-each-request");
	}
}
