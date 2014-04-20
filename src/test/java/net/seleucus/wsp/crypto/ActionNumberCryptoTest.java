package net.seleucus.wsp.crypto;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class ActionNumberCryptoTest {

	// A static password value specified as a char sequence
	private static final CharSequence PASS_PHRASE = "IfY0u0nlyKn3w!";
	// System.currentTimeMillis() / (60 * 1000) on the 26th of April 2014
	private static final long MINUTES = 23308172;
	// The action number representing an O/S command
	private static final int ACTION_NUMBER = 13;
	// A salt value of a single byte
	private static final byte[] SALT = {100, Byte.MIN_VALUE, Byte.MAX_VALUE, -33};
	// The expected byte array when using all the above
	private static final byte[] EXPECTED_BYTES = {
		100, -128, 127, -33, 38, 101, 82, -41, 111, 69, -33, 19, 56, -21, 111, -7, 83, 26, -22, 110, -82, -52, -50, 94
	};

	
	@Test(expected=InvocationTargetException.class)
	public final void shouldThrowAnUnsupportedOperationExceptionIfInstantiated() throws Exception {
		Constructor<ActionNumberCrypto> c = ActionNumberCrypto.class.getDeclaredConstructor();
		c.setAccessible(true);
		c.newInstance();
	}
	
	@Test
	public final void shouldReturnAByteArrayWith24Elements() {
		
		int length1 = ActionNumberCrypto.getHashedActionNumberNow(PASS_PHRASE, ACTION_NUMBER).length;
		// getHashedPassPhraseNow(PASS_PHRASE).length;
		int length2 = ActionNumberCrypto.getHashedActionNumberInTime(PASS_PHRASE, ACTION_NUMBER, MINUTES).length;
		// getHashedPassPhraseInTime(PASS_PHRASE, MINUTES).length;
		int length3 = ActionNumberCrypto.getHashedActionNumberNowWithSalt(PASS_PHRASE, ACTION_NUMBER, SALT).length;
		// getHashedPassPhraseNowWithSalt(PASS_PHRASE, SALT).length;
		int length4 = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(PASS_PHRASE, ACTION_NUMBER, MINUTES, SALT).length;
		// getHashedPassPhraseInTimeWithSalt(PASS_PHRASE, MINUTES, SALT).length;
		
		assertTrue( (length1 == length2) && (length2 == length3) && (length3 == length4) && (length4 == 24)  );
		
	}
	
	@Test
	public final void shouldHaveTheFirstFourElementsEqualToTheSalt() {
		SecureRandom scRandom = new SecureRandom();
		byte[] saltBytes = new byte[4];
		scRandom.nextBytes(saltBytes);
		byte[] byteArray = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(PASS_PHRASE, ACTION_NUMBER, MINUTES, saltBytes);
		
		assertArrayEquals(saltBytes, ArrayUtils.subarray(byteArray, 0, 4));
	}


	
	@Test
	public final void testGetHashedActionNumberInTimeWithSalt() {
		final byte[] byteArray = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(PASS_PHRASE, ACTION_NUMBER, MINUTES, SALT);

		// fail("Array is: " + Arrays.toString(byteArray));
		assertArrayEquals(EXPECTED_BYTES, byteArray);
	}

	@Test
	public final void testGetHashedActionNumberNowWithSalt() {

		String passPhrase = RandomStringUtils.randomAlphabetic(13);
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		byte[] byteArray = ActionNumberCrypto.getHashedActionNumberNowWithSalt(passPhrase, ACTION_NUMBER, SALT);
		byte[] expectedArray = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(passPhrase, ACTION_NUMBER, currentTimeMinutes, SALT);
		
		assertArrayEquals(expectedArray, byteArray);

	}

	@Test
	public final void testGetHashedActionNumberInTime() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(11);
		
		byte[] byteArray = ActionNumberCrypto.getHashedActionNumberInTime(passPhrase, ACTION_NUMBER, MINUTES);
		byte[] salt = ArrayUtils.subarray(byteArray, 0, 4);
		byte[] expectedArray = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(passPhrase, ACTION_NUMBER, MINUTES, salt);
		
		assertArrayEquals(expectedArray, byteArray);
		
	}

	@Test
	public final void testGetHashedActionNumberNow() {
		
		String passPhrase = RandomStringUtils.randomAlphabetic(13);
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		
		byte[] byteArray = ActionNumberCrypto.getHashedActionNumberNow(passPhrase, ACTION_NUMBER);
		byte[] salt = ArrayUtils.subarray(byteArray, 0, 4);
		byte[] expectedArray = ActionNumberCrypto.getHashedActionNumberInTimeWithSalt(passPhrase, ACTION_NUMBER, currentTimeMinutes, salt);
		
		assertArrayEquals(expectedArray, byteArray);
	}

}
