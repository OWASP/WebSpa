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

    private Properties configProperties = new Properties();

    public WSConfigLoader() {
        //TODO do we need you really?

    }

    /**
     * getConfiguration from configPath or default if non provided.
     *
     * @param configPath
     * @return Properties configuration
     * @throws IOException
     * @throws InvalidPropertyFileException
     */
    public Properties getConfiguration(String configPath) throws IOException, InvalidPropertyFileException {
        if (StringUtils.isBlank(configPath)) {
            configPath = "config/web-spa-server-properties.conf";
        }

        if (configProperties == null) {
            URL rootLocation = ClassLoader.getSystemResource(configPath);
            if (rootLocation == null) {
                throw new FileNotFoundException("Web-Spa Config File Not Found: " + configPath);
            }
            File configFile = new File(rootLocation.getFile());

            if (!configFile.canRead()) {
                throw new IOException("Web-Spa Configuration File Cannot Be Read");
            }

            FileInputStream in = new FileInputStream(configFile);
            configProperties.load(in);
            in.close();

            if (!configProperties.containsKey(WSConstants.ACCESS_LOG_FILE_LOCATION)) {
                throw new InvalidPropertyFileException("Access Log File Location Property Does Not Exist");
            }

            if (!configProperties.containsKey(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST)) {
                throw new InvalidPropertyFileException("Logging Regex For Each Request Does Not Exist");
            }
        }
        return configProperties;
    }

}
