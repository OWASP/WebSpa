package net.seleucus.wsp.server.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import net.seleucus.wsp.main.WebSpa;
import net.seleucus.wsp.server.WSServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSHelpOptionsTest {

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
		
		final String DB_PATH = "webspa-db";
		
		final String[] extensions = { ".properties", ".script", ".log", 
				".data", ".backup" };

		for (String extension : extensions) {

			File dbFile = new File(DB_PATH + extension);
			if (dbFile.exists()) {
				dbFile.delete();
			}

		}	// for loop
		
		final File configFile = new File("webspa-config.properties");
		if(configFile.exists()) {
			configFile.delete();
		}
		
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testExecutePrintsTheDefaultHelpFile() {

		WSHelpOptions myOptions = new WSHelpOptions(wsServer);
		myOptions.execute();
		//TODO Perform the comparison
	}
	
	/*
	@Test
	public void testHandle() {
		fail("Not yet implemented");
	}
	*/
	
	@Test
	public void testIsValidShouldReturnTrueIfCommandIsHelpIgnoreCase() {
		
		WSHelpOptions myOptions = new WSHelpOptions(wsServer);
		assertTrue(myOptions.isValid("heLP"));
		
	}
	
	@Test
	public void testIsValidShouldReturnTrueIfCommandIsQuestionMark() {
		
		WSHelpOptions myOptions = new WSHelpOptions(wsServer);
		assertTrue(myOptions.isValid("?"));
		
	}
	
	@Test
	public void testIsValidShouldReturnTrueIfCommandIsHelpWithAValidOption() {
		
		WSHelpOptions myOptions = new WSHelpOptions(wsServer);
		assertTrue(myOptions.isValid("help action"));
		
	}
	
	@Test
	public void testIsValidShouldReturnFalseIfCommandIsNotKnown() {
		
		WSHelpOptions myOptions = new WSHelpOptions(wsServer);
		assertFalse(myOptions.isValid("help not-known"));
		
	}
	/*
	@Test
	public void testWSHelpOptions() {
				
	}
	
	@Test
	public void testWSCommandOption() {
		fail("Not yet implemented");
	}
	*/
}
