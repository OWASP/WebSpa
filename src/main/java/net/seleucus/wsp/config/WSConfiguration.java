package net.seleucus.wsp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import net.seleucus.wsp.util.WSConstants;

import org.apache.commons.io.FileUtils;

public class WSConfiguration {

	protected static final String CONFIG_PATH = "webspa-config.properties";

	private Properties configProperties;
	
	public WSConfiguration() throws IOException {
		
		// Check if the configuration properties file is present, if not create
		// it from the bundled template...
		File configFile = new File(CONFIG_PATH);
		if (!configFile.exists()) {
			URL bundledConfigLocation = ClassLoader
					.getSystemResource("config/bundled-webspa-config.properties");
			FileUtils.copyURLToFile(bundledConfigLocation, configFile);
		}
		
		FileInputStream in = new FileInputStream(configFile);
		configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
	}
	
	public String getAccesLogFileLocation() {
		
		return configProperties.getProperty(WSConstants.ACCESS_LOG_FILE_LOCATION);

	}
	
	public String getLoginRegexForEachRequest() {
		
		return configProperties.getProperty(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST);
		
	}

}
