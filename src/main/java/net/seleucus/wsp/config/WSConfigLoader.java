package net.seleucus.wsp.config;

import net.seleucus.wsp.util.WSConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class WSConfigLoader {

	private static Properties configProperties = new Properties();
	
	private static String DEFAULT_PATH = "config/web-spa-server-properties.conf";

	private WSConfigLoader() {
		// Standard to avoid instantiation 'accidents'
	}

	public static Properties getConfiguration(String configPath)
			throws IOException, InvalidPropertyFileException {
		
		if (StringUtils.isBlank(configPath)) {
			configPath = DEFAULT_PATH;
		}

		if (configProperties.size() == 0) {
			URL rootLocation = ClassLoader.getSystemResource(configPath);
			File configFile = new File(rootLocation.getFile());
			
			FileInputStream in = new FileInputStream(configFile);
			configProperties.load(in);
			in.close();

			if (!configProperties
					.containsKey(WSConstants.ACCESS_LOG_FILE_LOCATION)) {
				throw new InvalidPropertyFileException(
						"Access Log File Location Property Does Not Exist");
			}

			if (!configProperties
					.containsKey(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST)) {
				throw new InvalidPropertyFileException(
						"Logging Regex For Each Request Does Not Exist");
			}
		}
		return configProperties;
	}

}
