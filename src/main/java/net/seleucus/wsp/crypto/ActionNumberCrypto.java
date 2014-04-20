package net.seleucus.wsp.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.ArrayUtils;

public class ActionNumberCrypto extends WebSpaUtils {

	public static byte[] getHashedActionNumberInTimeWithSalt(final CharSequence passPhrase, final int actionNumber, final long currentTimeMinutes, final byte[] salt) {

		byte[] passBytes = passPhrase.toString().getBytes(Charsets.UTF_8);
		byte[] actionBytes = ByteBuffer.allocate(4).putInt(actionNumber).array();
		byte[] timeBytes = ByteBuffer.allocate(8).putLong(currentTimeMinutes).array();

		byte[] sortedBytes = new byte[passBytes.length + timeBytes.length - 4 + 1];		
		System.arraycopy(passBytes, 0, sortedBytes, 0, passBytes.length);
		System.arraycopy(timeBytes, 4, sortedBytes, passBytes.length, timeBytes.length - 4);
		System.arraycopy(actionBytes, actionBytes.length - 1, sortedBytes, sortedBytes.length - 1, 1);
		
		byte[] allBytes = ArrayUtils.addAll(sortedBytes, salt);
		byte[] hashedBytes = ArrayUtils.subarray(digest(allBytes), 0, 20); 
		
		return ArrayUtils.addAll(salt, hashedBytes);
		
	}
	
	public static byte[] getHashedActionNumberNowWithSalt(final CharSequence passPhrase, final int actionNumber, final byte[] salt) {
		
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);

		return getHashedActionNumberInTimeWithSalt(passPhrase, actionNumber, currentTimeMinutes, salt);

	}
	
	public static byte[] getHashedActionNumberInTime(CharSequence passPhrase, int actionNumber, long currentTimeMinutes) {

		SecureRandom scRandom = new SecureRandom();
		byte[] randomBytes = new byte[4];
		scRandom.nextBytes(randomBytes);

		return getHashedActionNumberInTimeWithSalt(passPhrase, actionNumber, currentTimeMinutes, randomBytes);
		
	}
	
	public static byte[] getHashedActionNumberNow(final CharSequence passPhrase, final int actionNumber) {
		
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);

		SecureRandom scRandom = new SecureRandom();
		byte[] randomBytes = new byte[4];
		scRandom.nextBytes(randomBytes);

		return getHashedActionNumberInTimeWithSalt(passPhrase, actionNumber, currentTimeMinutes, randomBytes);

	}
	
	private ActionNumberCrypto() {
		// Standard to avoid instantiation 'accidents'
		throw new UnsupportedOperationException();
	}
	
}
