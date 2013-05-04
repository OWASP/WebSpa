package net.seleucus.wsp.crypto;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.crypto.util.EncodingUtils;

public class ActionNumberCrypto extends WebSpaUtils {

	public static byte[] getHashedActionNumberInTime(CharSequence passPhrase, int actionNumber, long currentTimeMinutes) {

		byte[] passBytes = passPhrase.toString().getBytes(Charsets.UTF_8);
		byte[] actionBytes = ByteBuffer.allocate(4).putInt(actionNumber).array();
		byte[] timeBytes = ByteBuffer.allocate(8).putLong(currentTimeMinutes).array();

		byte[] sortedBytes = new byte[passBytes.length + timeBytes.length - 4 + 1];		
		System.arraycopy(passBytes, 0, sortedBytes, 0, passBytes.length);
		System.arraycopy(timeBytes, 4, sortedBytes, passBytes.length, timeBytes.length - 4);
		System.arraycopy(actionBytes, actionBytes.length - 1, sortedBytes, sortedBytes.length - 1, 1);
		
		Arrays.sort(sortedBytes);

		SecureRandom scRandom = new SecureRandom();
		byte[] randomByte = new byte[4];
		scRandom.nextBytes(randomByte);
		
		byte[] allBytes = EncodingUtils.concatenate(sortedBytes, randomByte);
		byte[] hashedBytes = ArrayUtils.subarray(digest(allBytes), 0, 20); 
		
		return EncodingUtils.concatenate(randomByte, hashedBytes);
		
	}
	
	private ActionNumberCrypto() {
		// Standard to avoid instantiation 'accidents'		
	}
	
}
