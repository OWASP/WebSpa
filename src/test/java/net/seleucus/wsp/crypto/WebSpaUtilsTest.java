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
	
	@Test
	public final void testEqualsShouldReturnFalseIfBothAreNull() {
		
		assertFalse(WebSpaUtils.equals(null, null));
		
	}
	
	@Test
	public final void testEqualsShouldReturnFalseIfFirstIsNull() {
		
		byte[] first = null;
		byte[] second = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		
		assertFalse(WebSpaUtils.equals(first, second));
		
	}
	
	@Test
	public final void testEqualsShouldReturnFalseIfSecondIsNull() {
		
		byte[] first = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		byte[] second = null;
		
		assertFalse(WebSpaUtils.equals(first, second));
		
	}
	
	@Test
	public final void testEqualsShouldReturnTrueIfTwoArraysAreTheSame() {
		
		byte[] first = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		byte[] second = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		
		assertTrue(WebSpaUtils.equals(first, second));
		
	}
	
	@Test
	public final void testEqualsShouldReturnFalseIfTwoArraysAreNotTheSame() {
		
		byte[] first = {Byte.MIN_VALUE, -123, -1, 1, 1, 123, Byte.MAX_VALUE};
		byte[] second = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		
		assertFalse(WebSpaUtils.equals(first, second));
		
	}
	
	@Test
	public final void testEqualsShouldReturnFalseIfTwoArraysAreNotEqualInLength() {
		
		byte[] first = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE, 1, 2, 3};
		byte[] second = {Byte.MIN_VALUE, -123, -1, 0, 1, 123, Byte.MAX_VALUE};
		
		assertFalse(WebSpaUtils.equals(first, second));
		
	}
	
}
