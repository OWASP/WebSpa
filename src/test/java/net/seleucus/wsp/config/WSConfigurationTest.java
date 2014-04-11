package net.seleucus.wsp.config;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import net.seleucus.wsp.util.WSConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSConfigurationTest {

	@Before
	public void setUp() throws Exception {
		// Deletes the file "webspa-config.properties" if found...
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		if(configFile.exists()) {
			configFile.delete();
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
		// Deletes the file "webspa-config.properties" if found...
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		if(configFile.exists()) {
			configFile.delete();
		}

	}

	@Test
	public void testWSConfigurationShouldAlwaysLeaveAConfigFileBehind() throws IOException {
		
		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		// Running the constructor, creates the file
		// "webspa-config.properties" if one does 
		// not exist
		assertTrue(configFile.exists());
				
	}
	
	@Test
	public void testWSConfigurationShouldNotCreateAFileIfOneExists() throws IOException {
		
		new WSConfiguration();
		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		// Call the constructor twice, thus creating the file
		assertTrue(configFile.exists());

	}
	
	@Test
	public void testWSConfigurationHasTheAccessLogFileLocationProperty() throws IOException {
		
		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);

		FileInputStream in = new FileInputStream(configFile);
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertTrue(configProperties.containsKey(WSConstants.ACCESS_LOG_FILE_LOCATION));
		
	}
	
	@Test
	public void testWSConfigurationHasTheLoggingRegexRequestProperty() throws IOException {
		
		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);

		FileInputStream in = new FileInputStream(configFile);
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertTrue(configProperties.containsKey(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST));
		
	}
	
	@Test
	public void testWSConfigurationHasOnly2Properties() throws IOException {

		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);

		FileInputStream in = new FileInputStream(configFile);
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertEquals(configProperties.size(), 2);
		
	}
	
	@Test
	public void testGetAccesLogFileLocation() throws IOException, URISyntaxException {
		
		WSConfiguration myConfig = new WSConfiguration();
		
		URL bundledConfigLocation = ClassLoader
				.getSystemResource("config/bundled-webspa-config.properties");
		
		FileInputStream in = new FileInputStream(new File(bundledConfigLocation.toURI()));
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertEquals(myConfig.getAccesLogFileLocation(), 
				configProperties.getProperty(WSConstants.ACCESS_LOG_FILE_LOCATION));
		
	}
	
	@Test
	public void testGetLoginRegexForEachRequest() throws IOException, URISyntaxException {
		
		WSConfiguration myConfig = new WSConfiguration();
		
		URL bundledConfigLocation = ClassLoader
				.getSystemResource("config/bundled-webspa-config.properties");
		
		FileInputStream in = new FileInputStream(new File(bundledConfigLocation.toURI()));
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertEquals(myConfig.getLoginRegexForEachRequest(), 
				configProperties.getProperty(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST) );
		
	}
	
}
