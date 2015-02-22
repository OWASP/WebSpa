package net.seleucus.wsp.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WSVersion extends WSGestalt {

    private static final Logger logger = LoggerFactory.getLogger(WSVersion.class);

    public static final String VERSION_PROPERTIES_FILE = "/config/version.properties";
    public static final String MAJOR_VERSION_KEY = "major.version";
    public static final String MINOR_VERSION_KEY = "minor.version";


    private static int majorVersion;
    private static int minorVersion;

    static {
        try (final InputStream inputStream = WSVersion.class.getResourceAsStream(VERSION_PROPERTIES_FILE)) {
            final Properties properties = new Properties();
            properties.load(inputStream);
            majorVersion = Integer.valueOf(properties.getProperty(MAJOR_VERSION_KEY, "0"));
            minorVersion = Integer.valueOf(properties.getProperty(MINOR_VERSION_KEY, "0"));

        } catch (IOException ioe){
            majorVersion = 0;
            minorVersion = 0;
        }
    }

    public WSVersion(WebSpa myWebSpa) {
        super(myWebSpa);
    }

    public static int getMajor() {
        return majorVersion;
    }
	public static int getMinor() {
		return minorVersion;
	}

	public static String getValue() {
		return majorVersion + "." + minorVersion;
	}

	public static boolean isCurrentVersion(final String versionString) {
		return WSVersion.getValue().equalsIgnoreCase(versionString);
	}

	@Override
	public void exitConsole() {
		// Nothing to add here...
	}

	@Override
	public void runConsole() {
		myConsole.println(WSVersion.getValue());
	}
}
