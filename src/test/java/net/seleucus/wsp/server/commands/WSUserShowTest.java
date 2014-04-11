package net.seleucus.wsp.server.commands;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import net.seleucus.wsp.main.WebSpa;
import net.seleucus.wsp.server.WSServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSUserShowTest {


	private ByteArrayOutputStream outContent; // = new ByteArrayOutputStream();
	private ByteArrayOutputStream errContent; // = new ByteArrayOutputStream();

	private WSServer wsServer;
	
	@Before
	public void setUpStreams() throws Exception {
	
		outContent = new ByteArrayOutputStream();
		errContent = new ByteArrayOutputStream();
		
		System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));

	    wsServer = new WSServer(new WebSpa(System.console()));
	}

	@After
	public void cleanUpStreams() throws Exception {
		
		wsServer.shutdown();
		
		final String DB_PATH = "WebSpa-db";
		
		final String[] extensions = { ".properties", ".script", ".log", 
				".data", ".backup" };

		for (String extension : extensions) {

			File dbFile = new File(DB_PATH + extension);
			if (dbFile.exists()) {
				dbFile.delete();
			}

		}	// for loop
		
		final File configFile = new File("WebSpa-config.properties");
		if(configFile.exists()) {
			configFile.delete();
		}
		
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testIsValidShouldReturnTrueIfCommandIsServiceStopIgnoreCase() {
		
		WSUserShow myUserShow = new WSUserShow(wsServer);
		assertTrue(myUserShow.isValid("UseR sHow"));
		
	}
	
	@Test
	public void testIsValidShouldReturnFalseIfCommandIsServiceWithAnyOtherTextAfterIt() {
		
		WSUserShow myUserShow = new WSUserShow(wsServer);
		assertFalse(myUserShow.isValid("Anything"));
		
	}
	


}
