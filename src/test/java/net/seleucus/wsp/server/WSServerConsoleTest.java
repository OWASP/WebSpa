package net.seleucus.wsp.server;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import net.seleucus.wsp.main.WebSpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSServerConsoleTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	private WSServer wsServer;
	
	@Before
	public void setUpStreams() throws Exception {
	
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
	public void testExecuteCommandPrintsNothingIfNoInputIsGiven() throws Exception {

		WSServerConsole myConsole = new WSServerConsole( wsServer );
		myConsole.executeCommand("");
		assertTrue( outContent.toString().equals("") );

	}

	@Test
	public void testExecuteCommandPrintsDefaultUnknownCmdMessageIfCommandNotFound() throws Exception {

		WSServerConsole myConsole = new WSServerConsole( wsServer );

		myConsole.executeCommand("<user_typed_non_existant_command>");
		assertTrue( outContent.toString().contains(WSServerConsole.UNKNOWN_CMD_MESSAGE) );

	}

}
