package net.seleucus.wsp;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.junit.Test;

public class ConfigTest {

	@Test
	public void shouldCreateANewConfigWhenTheConfigFileExists() throws Exception {
		String configPath = "src/test/resources/config.xml";
	    assertNotNull(new Config(configPath));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAFileNotFoundExceptionIfConfigFileIsMissing() throws Exception {
		String configPath = "DoesNotExist.xml";
		new Config(configPath);
	}
	
	
}
