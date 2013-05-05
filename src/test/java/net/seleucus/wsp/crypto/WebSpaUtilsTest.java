package net.seleucus.wsp.crypto;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebSpaUtilsTest {

	@Test
	public final void testDigest() {
		byte[] inputByteArray = {0, Byte.MIN_VALUE, Byte.MAX_VALUE, 0};
		byte[] expectedByteArray = {
			84, 59, 87, 56, -96, 41, -1, -74, 
			74, -15, -18, -18, 44, 4, 56, 89, 
		   -28, -101, -51, 61, 89, -4, 75, 112, 
		    -6, -87, 41, 42, 58, 86, -11, 61, -7, 
		    86, 115, 112, 27, -122, -56, -90, 7, 
		  -111, 124, -62, -97, 116, -121, -90, 12, 
		    98, -96, 17, 23, -80, 26, 74, -62, 37, 
		  -108, -60, -105, 58, -121, 32	
		};
		
		assertArrayEquals(expectedByteArray, WebSpaUtils.digest(inputByteArray) );
	}

	@Test
	public final void testXor() {
		//   {10111000, 10000000, 00100001}
		// ^ {10111000, 10111000, 10111000}
		//---------------------------------
		//   {00000000, 00111000, 10011001}
		byte [] inputByteArray = {-72, Byte.MIN_VALUE, 33};
		byte timeByte = -72;
		byte [] outputByteArray = {0, 56, -103};
		assertArrayEquals(outputByteArray, WebSpaUtils.xor(inputByteArray, timeByte));
	}
	
}
