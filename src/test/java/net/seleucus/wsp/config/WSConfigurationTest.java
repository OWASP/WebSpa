package net.seleucus.wsp.config;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import net.seleucus.wsp.util.WSConstants;

import org.junit.Test;

public class WSConfigurationTest {

	@Test
	public void testWSConfigurationShouldAlwaysLeaveAConfigFileBehind() throws IOException {
		
		new WSConfiguration();
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		
		assertTrue(configFile.exists());
		
		configFile.delete();
		
	}
	
	@Test
	public void testGetAccesLogFileLocation() throws IOException, URISyntaxException {
		
		WSConfiguration myConfig = new WSConfiguration();
		
		URL bundledConfigLocation = ClassLoader
				.getSystemResource("config/bundled-web-spa-config.properties");
		
		FileInputStream in = new FileInputStream(new File(bundledConfigLocation.toURI()));
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertEquals(myConfig.getAccesLogFileLocation(), configProperties.getProperty(WSConstants.ACCESS_LOG_FILE_LOCATION));
		
		File configFile = new File(WSConfiguration.CONFIG_PATH);
		if(configFile.exists()) {
			configFile.delete();
		}
		
	}
	
	@Test
	public void testGetLoginRegexForEachRequest() throws IOException, URISyntaxException {
		
		WSConfiguration myConfig = new WSConfiguration();
		
		URL bundledConfigLocation = ClassLoader
				.getSystemResource("config/bundled-web-spa-config.properties");
		
		FileInputStream in = new FileInputStream(new File(bundledConfigLocation.toURI()));
		Properties configProperties = new Properties();
		configProperties.load(in);
		in.close();
		
		assertEquals(myConfig.getLoginRegexForEachRequest(), configProperties.getProperty(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST) );

		File configFile = new File(WSConfiguration.CONFIG_PATH);
		if(configFile.exists()) {
			configFile.delete();
		}
		
	}
	
}
