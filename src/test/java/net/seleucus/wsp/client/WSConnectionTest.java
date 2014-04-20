package net.seleucus.wsp.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class WSConnectionTest {

	@Test
	public void testActionToBeTakenMalformedURL() {
		// "Malformed URL: No action will be taken"
		WSConnection myConnection = new WSConnection("MalformedURL%&^*(");
		assertEquals(WSConnection.ACTION_CAN_BE_TAKEN[2], myConnection.getActionToBeTaken());
	}
	
	/*
	@Test
	public void testActionToBeTakenMalformedURI() {
		// "Malformed URI: No action will be taken"
		WSConnection myConnection = new WSConnection("http://^[[Aweb.spa.seleucus.net");
		assertEquals(WSConnection.ACTION_CAN_BE_TAKEN[5], myConnection.getActionToBeTaken());
	}
	*/

}
