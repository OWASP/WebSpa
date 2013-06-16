package net.seleucus.wsp.db;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class WSDatabaseAdaptorTest {

	@Test
	public final void shouldCreateADatabaseIfAFileDoesNotExist01() throws Exception {
		
		DateFormat dateFormat = new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		
		final String dbName = "src/test/resources/data/01" + dateFormat.format(date);
		final WSDatabaseAdaptor myDB = new WSDatabaseAdaptor(dbName);
		
		File dbDataFile = new File(dbName + ".properties");
		final boolean fileExists = dbDataFile.exists();
		
		myDB.deleteAllDatabaseFiles();
		
		assertTrue(fileExists);
		
	}

}
