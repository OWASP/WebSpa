package net.seleucus.wsp.config;

import net.seleucus.wsp.crypto.ActionNumberCrypto;
import net.seleucus.wsp.util.WSConstants;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Properties;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

public class WSConfigLoaderTest {

    private static final String CONFIG_PATH = "config/web-spa-server-properties.conf";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

	@Test // (expected=InvocationTargetException.class)
	public final void shouldThrowAnUnsupportedOperationExceptionIfInstantiated() throws Exception {
		Constructor<WSConfigLoader> c = WSConfigLoader.class.getDeclaredConstructor();
		c.setAccessible(true);
		c.newInstance();
	}
	
	@Test
	public final void testGetConfigurationWithEmptyPath() throws Exception {
		Properties propStandardConfigPath = WSConfigLoader.getConfiguration(CONFIG_PATH);
		Properties propEmptyConfigPath = WSConfigLoader.getConfiguration("");
		
		assertEquals(propEmptyConfigPath, propStandardConfigPath);
	}

    @Test
    public final void testGetAccessLogFileLocation() throws Exception {
        assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));

        Properties myProperties = WSConfigLoader.getConfiguration(CONFIG_PATH);
        assertEquals("/var/log/apache2/access.log", myProperties.getProperty("access-log-file-location", ""));
    }

    @Test
    public final void testGetLoggingRegexForEachRequest() throws Exception {
        assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));

        Properties myProperties = WSConfigLoader.getConfiguration(CONFIG_PATH);
        assertEquals("\\[(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} .....)\\] \"GET /(\\S*) HTTP\\/1\\.",
                myProperties.getProperty("logging-regex-for-each-request", ""));
    }

}
