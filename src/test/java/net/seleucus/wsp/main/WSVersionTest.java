package net.seleucus.wsp.main;

import static org.junit.Assert.*;

import org.junit.Test;

public class WSVersionTest {

	private static int VERSION_MIN = 0;
	private static int VERSION_MAX = 10;
	
	@Test
	public final void testGetMajor() {
		int majorVersion = WSVersion.getMajor();
		
		assertTrue(
			"Major version is between [0,10): " + majorVersion,
			( (majorVersion >= VERSION_MIN) && (majorVersion < VERSION_MAX) )	
		);
	}

	@Test
	public final void testGetMinor() {
		int minorVersion = WSVersion.getMinor();
		
		assertTrue(
			"Major version is between [0,10): " + minorVersion,
			( (minorVersion >= VERSION_MIN) && (minorVersion < VERSION_MAX) )	
		);	}

}
