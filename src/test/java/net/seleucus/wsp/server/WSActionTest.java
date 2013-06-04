package net.seleucus.wsp.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class WSActionTest {

	@Test
	public final void testGetCommand() {
		
		final String command = "ping www.google.com";
		WSAction action = new WSAction(command);
		
		assertEquals(command, action.getCommand());	
	}

	@Test
	public final void testGetHasExecuted() {
		
		WSAction action = new WSAction("uname");
		action.run();
		
		assertTrue(action.getHasExecuted());
	}

}
