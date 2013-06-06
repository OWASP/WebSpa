package net.seleucus.wsp.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class PassPhraseCryptoTest {

	// A static password value specified as a char sequence
	private static final CharSequence PASS_PHRASE = "!QAZ@WSX�EDC123-�";
	// System.currentTimeMillis() / (60 * 1000) on the 26th of April 2013
	private static final long MINUTES = 22782223;
	// A salt value of a single byte
	private static final byte SALT = -33;
	// The expected byte array when using all the above
	private static final byte[] EXPECTED_BYTES = {
		-33, -112,   78,   53, -6, -106,  -50,  -97,   52,  -59, 
		-43,   10, -121, -102,  6,   28, -112,  -45,  127,  -11, 
		 58,  -54,   -7,   83, 37,   22,   91, -113,   33,  -20,  
		 74, -104,   52,   66, 35,  102,  -14,   55, -127,  -31,
	   -106, -121, -122,   72, 83,  -96,  -12,  -78,  -11,  122, 
	    -65
	};
	
	@Test
	public final void shouldReturnAByteArrayWith51Elements() {
		
		int length1 = PassPhraseCrypto.getHashedPassPhraseNow(PASS_PHRASE).length;
		int length2 = PassPhraseCrypto.getHashedPassPhraseInTime(PASS_PHRASE, MINUTES).length;
		int length3 = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(PASS_PHRASE, SALT).length;
		int length4 = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(PASS_PHRASE, MINUTES, SALT).length;
		
		assertTrue( (length1 == length2) && (length2 == length3) && (length3 == length4) && (length4 == 51)  );
		
	}
	
	@Test
	public final void shouldHaveTheFirstElementEqualToTheSalt() {
		SecureRandom scRandom = new SecureRandom();
		byte[] saltBytes = new byte[1];
		scRandom.nextBytes(saltBytes);
		byte[] byteArray = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(PASS_PHRASE, MINUTES, saltBytes[0]);
		assertEquals(saltBytes[0], byteArray[0]);
	}

	@Test
	public final void testGetHashedPassPhraseInTimeWithSalt() {
		byte[] byteArray = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(PASS_PHRASE, MINUTES, SALT);
		
		assertArrayEquals(EXPECTED_BYTES, byteArray);
		// fail("" + Arrays.toString(byteArray));

	}

	@Test
	public final void testGetHashedPassPhraseInTime() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(20);
		
		byte[] byteArray = PassPhraseCrypto.getHashedPassPhraseInTime(passPhrase, MINUTES);
		byte salt = byteArray[0];
		byte[] expectedArray = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(passPhrase, MINUTES, salt);
		
		assertArrayEquals(expectedArray, byteArray);
	}

	@Test
	public final void testGetHashedPassPhraseNowWithSalt() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(13);
		
		byte[] byteArray = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(passPhrase, SALT);
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		byte[] expectedArray = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(passPhrase, currentTimeMinutes, SALT);
		
		assertArrayEquals(expectedArray, byteArray);
	}

	@Test
	public final void testGetHashedPassPhraseNow() {

		String passPhrase = RandomStringUtils.randomAlphabetic(17);

		byte[] byteArray = PassPhraseCrypto.getHashedPassPhraseNow(passPhrase);
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		byte salt = byteArray[0];
		byte[] expectedArray = PassPhraseCrypto.getHashedPassPhraseInTimeWithSalt(passPhrase, currentTimeMinutes, salt);
		
		assertArrayEquals(expectedArray, byteArray);
	}
	
	@Test(expected=InvocationTargetException.class)
	public final void shouldThrowAnUnsupportedOperationExceptionIfInstantiated() throws Exception {
		Constructor<PassPhraseCrypto> c = PassPhraseCrypto.class.getDeclaredConstructor();
		c.setAccessible(true);
		c.newInstance();
	}

}
