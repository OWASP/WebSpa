package net.seleucus.wsp.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WSConfigLoaderTest {

	private static final String CONFIG_PATH = "config/web-spa-server-properties.conf";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void shouldBeAbleToInstantiateAWSConfigLoaderIfTheFileExists() throws Exception {
		new WSConfigLoader(CONFIG_PATH);
	}

	@Test (expected=FileNotFoundException.class)
	public void shouldThrowAnExceptionIfTheFileDoesNotExist() throws Exception {
		new WSConfigLoader("config/doesnotexist.properties");
	}

	@Test(expected=IOException.class)
	public void shouldThrowAnExceptionIfTheFileIsUnreadable() throws Exception {
		assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));
		String pathToUnreadable = "config/unreadable.properties";
		URL rootLocation = ClassLoader.getSystemResource(pathToUnreadable);
		File unreadable = new File(rootLocation.getFile());
		unreadable.setReadable(false);
		new WSConfigLoader(pathToUnreadable);
	}
	
	@Test
	public void shouldThrowInvalidPropertyFileExceptionWhenNoAccessLogFilePropertyExists() throws Exception {
		thrown.expect(InvalidPropertyFileException.class);
		thrown.expectMessage("Access Log File Location Property Does Not Exist");
		new WSConfigLoader("config/no-access-log-file-location-missing.conf");
	}
	
	@Test
	public void shoudlThrowInvalidPropertyFileExceptionWhenNoLoggingRegexPropertyExists() throws Exception {
		thrown.expect(InvalidPropertyFileException.class);
		thrown.expectMessage("Logging Regex For Each Request Does Not Exist");
		new WSConfigLoader("config/no-logging-regex-for-each-request.conf");
	}
	
	@Test 
	public final void testGetAccessLogFileLocation() throws Exception {
		WSConfigLoader ws = new WSConfigLoader(CONFIG_PATH);
		assertEquals("/var/log/apache2/access.log", ws.getAccessLogFileLocation());
	}

	@Test
	@Ignore
	public final void testGetLoggingRegexForEachRequest() throws Exception {
		WSConfigLoader ws = new WSConfigLoader(CONFIG_PATH);
		assertEquals("\\[(\\d{2}/\\w{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} .....)\\] \"GET /(\\S*) HTTP\\/1\\.", ws.getAccessLogFileLocation());	}

}
