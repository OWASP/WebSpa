package net.seleucus.wsp.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class WebSpaEncoderTest {

	@Test
	public final void testEncode() {

		String passPhrase = RandomStringUtils.randomAlphabetic(20);
		
		String calculatedKnock = WebSpaEncoder.encode(passPhrase);
		byte calculatedSalt = Base64.decodeBase64(calculatedKnock)[0];
		
		byte[] expectedBytes = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(passPhrase, calculatedSalt);
		String expectedKnock = Base64.encodeBase64URLSafeString(expectedBytes);
		
		assertEquals(expectedKnock, calculatedKnock);
	}

	@Test
	public final void shouldCreateTheCorrectKnock() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(13);
		int actionNumber = 11;
		
		WebSpaEncoder myEncoder = new WebSpaEncoder(passPhrase, actionNumber);
		String calculatedWebSpaRequest = myEncoder.getKnock();
		
		byte[] calculatedBytes = Base64.decodeBase64(calculatedWebSpaRequest);
		byte passSalt = calculatedBytes[0];
		byte[]  actionSalt = ArrayUtils.subarray(calculatedBytes, 51, 55);
		
		byte[] expectedPassBytes = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(passPhrase, passSalt);
		byte[] expectedActionBytes = ActionNumberCrypto.getHashedActionNumberNowWithSalt(passPhrase, actionNumber, actionSalt);
		
		byte[] expectedAllBytes = ArrayUtils.addAll(expectedPassBytes, expectedActionBytes);
		String expectedWebSpaRequest = Base64.encodeBase64URLSafeString(expectedAllBytes);
		
		assertEquals(expectedWebSpaRequest, calculatedWebSpaRequest);
		
	}
	
	@Test
	public final void testMatchesTrue() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(20);
		int actionNumber = 11;
		
		WebSpaEncoder myEncoder = new WebSpaEncoder(passPhrase, actionNumber);
		String calculatedWebSpaRequest = myEncoder.getKnock();
		
		assertTrue(WebSpaEncoder.matches(passPhrase, calculatedWebSpaRequest));

	}
	
	@Test
	public final void testMatchesFalse() {
		
		String passPhraseTrue = RandomStringUtils.randomAlphabetic(20);
		String passPhraseFalse = RandomStringUtils.randomAlphabetic(20);
		
		int actionNumber = 7;
		
		WebSpaEncoder myEncoder = new WebSpaEncoder(passPhraseTrue, actionNumber);
		String calculatedWebSpaRequest = myEncoder.getKnock();
		
		assertFalse(WebSpaEncoder.matches(passPhraseFalse, calculatedWebSpaRequest));
		
	}
	
	@Test
	public final void testMatchesShouldReturnFalseIfWebSpaRequestIsNot100Chars() {
		assertFalse(WebSpaEncoder.matches("A-Pa$$w0rd", "A-Web-Spa-Request-Of-Length-Not-100"));
	}
	
	@Test
	public final void testGetActionWithAValidWebSpaRequest() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(11);
		int actionNumber = 6;
		
		WebSpaEncoder myEncoder = new WebSpaEncoder(passPhrase, actionNumber);
		String webSpaRequest = myEncoder.getKnock();
		
		assertEquals(actionNumber, WebSpaEncoder.getActionNumber(passPhrase, webSpaRequest));
	}
}
