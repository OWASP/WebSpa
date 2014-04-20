package net.seleucus.wsp.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class PassPhraseCryptoTest {

	// A static password value specified as a char sequence
	private static final CharSequence PASS_PHRASE = "IfY0u0nlyKn3w!";
	// System.currentTimeMillis() / (60 * 1000) on the 26th of April 2013
	private static final long MINUTES = 22782223;
	// A salt value of a single byte
	private static final byte SALT = -33;
	// The expected byte array when using all the above
	private static final byte[] EXPECTED_BYTES = { 
		-33, 18, 48, 42, 39, -38, -19, -4, -47, -71, -118, 71, 18, -97, -41, -11, -46, -6, 23, 38, -79, 13, -69, -103, -70, -110, -116, -79, 55, 8, 32, -7, -47, 43, -8, -99, 48, -39, -88, 35, -111, 14, 101, 4, 127, -56, 36, -24, 99, -45, -31
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
		
		// fail("Array is: " + Arrays.toString(byteArray));
		assertArrayEquals(EXPECTED_BYTES, byteArray);

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
