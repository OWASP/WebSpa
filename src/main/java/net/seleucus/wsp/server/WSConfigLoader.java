package net.seleucus.wsp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class WSConfigLoader {

	
	private static final String LOGGING_REGEX_FOR_EACH_REQUEST = "logging-regex-for-each-request";
	private static final String ACCESS_LOG_FILE_LOCATION = "access-log-file-location";
	private final Properties configProperties = new Properties();
	
	public WSConfigLoader(final String configPath) throws IOException, InvalidPropertyFileException {
		
		URL rootLocation = ClassLoader.getSystemResource(configPath);
		if(rootLocation == null) {
			throw new FileNotFoundException("Config File not Found: " + configPath);
		}
		String workingDirectory = rootLocation.getFile();
		System.out.println(workingDirectory);
		File configFile = new File(workingDirectory);
		
		if(!configFile.canRead()) {
			throw new IOException("Web-Spa Configuration File Cannot Be Read");
		}
		
		FileInputStream in = new FileInputStream(configFile);
		configProperties.load(in);
		in.close();
		
		if(!configProperties.containsKey(ACCESS_LOG_FILE_LOCATION)){
			throw new InvalidPropertyFileException("Access Log File Location Property Does Not Exist");
		}
		
		if(!configProperties.containsKey(LOGGING_REGEX_FOR_EACH_REQUEST)){
			throw new InvalidPropertyFileException("Logging Regex For Each Request Does Not Exist");
		}
		
	}
	
	public String getAccessLogFileLocation() {
		return configProperties.getProperty(ACCESS_LOG_FILE_LOCATION);
	}
	
	public String getLoggingRegexForEachRequest() {
		return configProperties.getProperty(LOGGING_REGEX_FOR_EACH_REQUEST);
	}
}
