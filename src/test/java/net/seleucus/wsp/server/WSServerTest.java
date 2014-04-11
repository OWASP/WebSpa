package net.seleucus.wsp.server;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import net.seleucus.wsp.main.WebSpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSServerTest {

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
	public void testServerStatusWhenServiceHasNotStarted() throws Exception {
		
		wsServer.serverStatus();	
		assertTrue(outContent.toString().endsWith("WebSpa is Stopped." + '\n'));
				
	}

}
