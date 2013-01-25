package net.seleucus.wsp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class ConfigTest {

	@Test
	public void shouldCreateANewConfigWhenThePropertiesFileExistsAndCanBeRead() throws Exception {
		String configPath = "src/test/resources/config/web-spa-properties.config";
	    assertNotNull(new Config(configPath));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfConfigDirectoryIsNotCalledConfig() throws Exception {
		String configPath = "src/test/resources/not-config/web-spa-properties.config";
		new Config(configPath);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfConfigDirectoryIsMissing() throws Exception {
		String configPath = "web-spa-properties.config";
		new Config(configPath);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfPropertiesFileHasTheWrongName() throws Exception {
		String configPath = "src/test/resources/config/not-web-spa-properties.config";
		new Config(configPath);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfPropertiesFileIsNull() throws Exception {
		new Config(null);
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfFileCannotBeRead() throws Exception {
		String configPath = "src/test/resources/config/web-spa-properties.config";
		assertFalse(new File(configPath).canRead()); 
	}
	
	
}
