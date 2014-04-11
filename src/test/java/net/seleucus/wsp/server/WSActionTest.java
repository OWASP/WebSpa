package net.seleucus.wsp.server;

import static org.junit.Assert.*;

import java.io.File;

import net.seleucus.wsp.main.WebSpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSActionTest {

	private WSServer wsServer;
	
	@Before
	public void setUpStreams() throws Exception {

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
		
	}

	@Test
	public void testWSActionSetsExecutionAndSuccessToFalse() throws Exception {
	
		WSAction wsAction = new WSAction(wsServer, 0, 0, "127.0.0.1");
		assertFalse(wsAction.getHasExecuted());
		assertFalse(wsAction.getWasSuccessful());

	}

}
