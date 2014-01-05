package net.seleucus.wsp.server;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import net.seleucus.wsp.main.WebSpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSServerTest {

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
	public void testServerStatus() throws Exception {
		
		WSServer myServer = new WSServer(new WebSpa(System.console()));
		myServer.serverStatus();
		
		assertTrue(outContent.toString().endsWith("Web-Spa is Stopped." + '\n'));
		
		myServer.shutdownAndDeleteAllFiles();
		
	}

}
