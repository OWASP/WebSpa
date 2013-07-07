package net.seleucus.wsp.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
		);	
	}

	@Test
	public final void testGetValue() {
		assertEquals(WSVersion.getValue(), WSVersion.getMajor() + "." + WSVersion.getMinor());
	}
	
	@Test
	public final void shouldBe3Characters() {
		String version = WSVersion.getValue();
		assertEquals(3, version.length());
	}
	
	@Test
	public final void testIsValidTrue() {
		assertTrue(WSVersion.isCurrentVersion("0.5"));
	}
	
	@Test
	public final void testIsValidFalse() {
		assertFalse(WSVersion.isCurrentVersion("0.4"));
	}
	
}
