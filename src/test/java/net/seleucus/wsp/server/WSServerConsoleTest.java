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

	@Before
	public void setUpStreams() {

		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

	}

	@After
	public void cleanUpStreams() {

		System.setOut(null);
		System.setErr(null);

	}

	@Test
	public void testExecuteCommandPrintsNothingIfNoInputIsGiven() throws Exception {

		WSServer myServer = new WSServer( new WebSpa(System.console()) );
		WSServerConsole myConsole = new WSServerConsole( myServer );

		myConsole.executeCommand("");
		assertTrue( outContent.toString().equals("") );

		myServer.shutdownAndDeleteAllFiles();

	}

	@Test
	public void testExecuteCommandPrintsDefaultUnknownCmdMessageIfCommandNotFound() throws Exception {

		WSServer myServer = new WSServer( new WebSpa(System.console()) );
		WSServerConsole myConsole = new WSServerConsole( myServer );

		myConsole.executeCommand("<user_typed_non_existant_command>");
		assertTrue( outContent.toString().contains(WSServerConsole.UNKNOWN_CMD_MESSAGE) );

		myServer.shutdownAndDeleteAllFiles();

	}

}
