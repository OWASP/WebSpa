package net.seleucus.wsp.db;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSDatabaseTest {

	protected static void deleteAllDBFiles() throws Exception {

		final String DB_PATH = "webspa-db";
		
		final String[] extensions = {
				".properties", ".script", 
				".log", ".data", ".backup" };

		for (String extension : extensions) {

			File dbFile = new File(DB_PATH + extension);
			if (dbFile.exists()) {
				dbFile.delete();
			}

		}	// for loop
		
	}
	
	@Before
	public void setUp() throws Exception {
		
		WSDatabaseTest.deleteAllDBFiles();
		
	}
	
	@After
	public void tearDown() throws Exception {
		
		WSDatabaseTest.deleteAllDBFiles();

	}
	
	@Test
	public void testWSDatabaseShouldAlwaysLeaveAPropertiesFileBehind() throws Exception {
		
		new WSDatabase();
		File propertiesFile = new File(WSDatabase.DB_PATH + ".properties");
		
		assertTrue(propertiesFile.exists());
		
	}
	
	@Test
	public void testWSDatabaseShouldAlwaysLeaveAScriptFileBehind() throws Exception {
		
		new WSDatabase();
		File scriptFile = new File(WSDatabase.DB_PATH + ".script");
		
		assertTrue(scriptFile.exists());
		
	}

}
