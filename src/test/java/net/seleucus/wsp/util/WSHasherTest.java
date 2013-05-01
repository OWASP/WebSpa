package net.seleucus.wsp.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class WSHasherTest {

	private static final CharSequence PASS_PHRASE = "!QAZ@WSX£EDC123-Û";
	private static final int ACTION_NUMBER = 10;
	// System.currentTimeMillis() / (60 * 1000) on the 26th of April 2013
	private static final long MINUTES = 22782223; 
	
	private static final byte [] PASS_KNOCK = { 
		2, 16, 50, -114, -30, 64, 87, 106, 124, -7, -78, -3, 121, -21, 88, 72, -38, 36, 
	   61, -126, -46, -124, 90, -108, 115, 25, 6, -91, 16, -118, 68, 54, 47, 123, -111,
	   31, -83, -1, 49, 1, 23, -60, 115, -9, -76, 9, 124, -43, 67, 20, -95, 115, -98, 
	  -62, -116, -125, 110, 80, 123, 85, -58, -62, 27, 18
	}; 

	private static final byte [] ACTION_KNOCK = { 91, 26, -30};
	
	@Test
	public final void shouldBeAbleToInstantiateAWSHasher() {
		new WSHasher(PASS_PHRASE, ACTION_NUMBER, MINUTES);
	}

	
	@Test
	public final void shouldHaveAStaticGetMethodForObtainingTheHashedPassPhrase() {
		WSHasher.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES);
	}
	
	@Test
	public final void shouldReturn_64_BytesAsAnArrayWhenGetHashedPassPhraseIsCalled() {
		assertEquals(64, WSHasher.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES).length);
	}
	
	@Test
	public final void testGetHashedPassPhraseInTime() {

		assertArrayEquals(WSHasher.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES), PASS_KNOCK);
		// fail(Arrays.toString(WSHasher.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES)));
	}
	
	@Test
	public final void testSortByteArray() {
		
		byte[] data = {Byte.MAX_VALUE, Byte.MIN_VALUE, 33, -80, 80};
		Arrays.sort(data);
		byte[] expected = {Byte.MIN_VALUE, -80, 33, 80, Byte.MAX_VALUE};
		
		assertArrayEquals(expected, data );
	}
	
	@Test
	public final void shouldHaveAStaticGetMethodForObtainingTheHashedActionNumber() {
		WSHasher.getHashedActionNumberInTime(PASS_PHRASE, ACTION_NUMBER, MINUTES);
	}
	
	@Test
	public final void shouldReturn_3_BytesAsAnArrayWhenGetActionNumberIsCalled() {
		assertEquals(3, WSHasher.getHashedActionNumberInTime(PASS_PHRASE, ACTION_NUMBER, MINUTES).length);
	}
	
	@Test
	public final void testGetHashedActionNumberInTime() {
				
		assertArrayEquals(WSHasher.getHashedActionNumberInTime(PASS_PHRASE, ACTION_NUMBER, MINUTES), ACTION_KNOCK);
	}
	
	@Test
	public final void methodEncodeshouldReturnAStringOf_100_Characters() {
		
		WSHasher myHasher = new WSHasher(PASS_PHRASE, ACTION_NUMBER, MINUTES);
		
		assertEquals(myHasher.encode().length(), 100);		
	}
	
	@Test
	public final void testEncode() {

		WSHasher myHasher = new WSHasher(PASS_PHRASE, ACTION_NUMBER, MINUTES);
		String hashedString = myHasher.encode().substring(0, 89);

		byte[] expectedBytes = new byte[PASS_KNOCK.length + ACTION_KNOCK.length];
		System.arraycopy(PASS_KNOCK, 0, expectedBytes, 0, PASS_KNOCK.length);
		System.arraycopy(ACTION_KNOCK, 0, expectedBytes, PASS_KNOCK.length, ACTION_KNOCK.length);
		
		String expectedString = Base64.encodeBase64URLSafeString(expectedBytes).substring(0,89);
		assertEquals(hashedString, expectedString);
	}
	
	@Test
	public final void testMatches() {
		WSHasher myHasher = new WSHasher(PASS_PHRASE, ACTION_NUMBER, MINUTES);
		
		byte[] expectedBytes = new byte[PASS_KNOCK.length + ACTION_KNOCK.length];
		System.arraycopy(PASS_KNOCK, 0, expectedBytes, 0, PASS_KNOCK.length);
		System.arraycopy(ACTION_KNOCK, 0, expectedBytes, PASS_KNOCK.length, ACTION_KNOCK.length);
		
		String expectedString = Base64.encodeBase64URLSafeString(expectedBytes).substring(0,89) + "1qazxsw23ed";
		
		
		assertTrue(myHasher.matches(expectedString));
	}
	
}
