package net.seleucus.wsp.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class WSHasherTest {

	private static final CharSequence PASS_PHRASE = "!QAZ@WSX£EDC123-Û";
	private static final int ACTION_NUMBER = 10;
	// System.currentTimeMillis() / (60 * 1000) on the 26th of April 2013
	private static final long MINUTES = 22782223; 
	
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
		
		byte [] byteData = { 
			-59,  19,   82, -126,  -68,  115, 125, -47,  -30,   60, 106, -56,  -7,  -3, -127, -98,
			 26,  93,  121,  -37,  102,  -93,  66, -95, -110,   23,  10, -50, -76, 119,  -38,  14,
			-42,  93,  -47,  -70, -103,   80, -73, -34,   18,  121, 106,  46, 119,  62,  -30, -86,
			-62,  71,  100,  -36,  -41,  119,  25, 101,  -45, -114, 115, 127, -57,   2,   73, -93				
		}; 
		
		assertArrayEquals(WSHasher.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES), byteData);
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
		
		byte [] byteData = { -117, 30, -45};
		
		assertArrayEquals(WSHasher.getHashedActionNumberInTime(PASS_PHRASE, ACTION_NUMBER, MINUTES), byteData);
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
		byte[] expectedBytes = {
				-59,  19,   82, -126,  -68,  115, 125, -47,  -30,   60, 106, -56,  -7,  -3, -127, -98,
				 26,  93,  121,  -37,  102,  -93,  66, -95, -110,   23,  10, -50, -76, 119,  -38,  14,
				-42,  93,  -47,  -70, -103,   80, -73, -34,   18,  121, 106,  46, 119,  62,  -30, -86,
				-62,  71,  100,  -36,  -41,  119,  25, 101,  -45, -114, 115, 127, -57,   2,   73, -93,
			   -117,  30,  -45
		};
		String expectedString = Base64.encodeBase64URLSafeString(expectedBytes).substring(0,89);
		assertEquals(hashedString, expectedString);
	}
	
	@Test
	public final void testMatches() {
		WSHasher myHasher = new WSHasher(PASS_PHRASE, ACTION_NUMBER, MINUTES);

		byte[] expectedBytes = {
				-59,  19,   82, -126,  -68,  115, 125, -47,  -30,   60, 106, -56,  -7,  -3, -127, -98,
				 26,  93,  121,  -37,  102,  -93,  66, -95, -110,   23,  10, -50, -76, 119,  -38,  14,
				-42,  93,  -47,  -70, -103,   80, -73, -34,   18,  121, 106,  46, 119,  62,  -30, -86,
				-62,  71,  100,  -36,  -41,  119,  25, 101,  -45, -114, 115, 127, -57,   2,   73, -93,
			   -117,  30,  -45
		};
		String expectedString = Base64.encodeBase64URLSafeString(expectedBytes).substring(0,89) + "1qazxsw23ed";
		assertTrue(myHasher.matches(expectedString));
	}
}
