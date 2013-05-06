package net.seleucus.wsp.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.crypto.util.EncodingUtils;

public final class PassPhraseCrypto extends WebSpaUtils {
	
	protected static byte[] getHashedPassPhraseInTimeWithSalt(final CharSequence passPhrase, final long currentTimeMinutes, final byte salt) {
		
		byte[] passBytes = passPhrase.toString().getBytes(Charsets.UTF_8);
		byte[] timeBytes = ByteBuffer.allocate(8).putLong(currentTimeMinutes).array();
		
		byte[] sortedBytes = new byte[passBytes.length + timeBytes.length - 4];
		System.arraycopy(passBytes, 0, sortedBytes, 0, passBytes.length);
		System.arraycopy(timeBytes, 4, sortedBytes, passBytes.length, timeBytes.length - 4);

		Arrays.sort(sortedBytes);
		
		byte[] randomBytes = new byte[1];
		randomBytes[0] = salt;
		
		byte[] allBytes = EncodingUtils.concatenate(sortedBytes, randomBytes);
		byte[] hashedBytes = ArrayUtils.subarray(digest(allBytes), 0, 50); 
		
		return EncodingUtils.concatenate(randomBytes, hashedBytes);
		
	}

	protected static byte[] getHashedPassPhraseNowWithSalt(final CharSequence passPhrase, final byte salt) {
		
		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		
		return getHashedPassPhraseInTimeWithSalt(passPhrase, currentTimeMinutes, salt);

	}

	protected static byte[] getHashedPassPhraseInTime(final CharSequence passPhrase, final long currentTimeMinutes) {
		
		SecureRandom scRandom = new SecureRandom();
		byte[] randomBytes = new byte[1];
		scRandom.nextBytes(randomBytes);
		
		return getHashedPassPhraseInTimeWithSalt(passPhrase, currentTimeMinutes, randomBytes[0]);

	}
	
	protected static byte[] getHashedPassPhraseNow(final CharSequence passPhrase) {

		long currentTimeMinutes = System.currentTimeMillis() / (60 * 1000);
		
		SecureRandom scRandom = new SecureRandom();
		byte[] randomBytes = new byte[1];
		scRandom.nextBytes(randomBytes);
		
		return getHashedPassPhraseInTimeWithSalt(passPhrase, currentTimeMinutes, randomBytes[0]);

	}
	
	private PassPhraseCrypto() { 
		// Standard to avoid instantiation 'accidents'
		throw new UnsupportedOperationException();
	}

}
