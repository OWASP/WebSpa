package net.seleucus.wsp.db;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import net.seleucus.wsp.db.WSDatabaseCreator;

import org.junit.Test;

public class WSDatabaseCreatorTest {

	private static final String DB_PATH = "data/web-spa-server-database.db";
	
	@Test
	public final void shouldBeAbleToInstantiateAWSDBLoaderIfTheFileExists() throws Exception {
		WSDatabaseCreator myLoader = new WSDatabaseCreator(DB_PATH);
		myLoader.dropAllObjects();
	}
	
	@Test (expected=FileNotFoundException.class)
	public final void shouldThrowAnExceptionIfTheFileDoesNotExist() throws Exception {
		WSDatabaseCreator myLoader = new WSDatabaseCreator("data/doesnotexist.db");
		myLoader.dropAllObjects();
	}
	
	@Test (expected=IOException.class)
	public final void shouldThrowAnExceptionIfTheFileCannotBeRead() throws Exception {
		assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));
		
		String pathToUnreadable = "data/unreadable.db";
		URL rootLocation = ClassLoader.getSystemResource(pathToUnreadable);
		File unreadable = new File(rootLocation.getFile());
		unreadable.setReadable(false);
		
		WSDatabaseCreator myLoader = new WSDatabaseCreator("data/unreadable.db");
		myLoader.dropAllObjects();
	}
	
	@Test
	public final void shouldLoadAValidDBIfTheFileExists() throws Exception {
		WSDatabaseCreator myLoader = new WSDatabaseCreator(DB_PATH);
		String dbStatus = null; // myLoader.getStatus();
		assertEquals("Table-1", dbStatus);
		myLoader.dropAllObjects();
	}

}
