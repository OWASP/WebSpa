package net.seleucus.wsp.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

public class WSHasher {

	private ByteArrayOutputStream hashedContents;

	public WSHasher(CharSequence passPhrase, int actionNumber,
			long currentTimeInMinutes) {

		hashedContents = new ByteArrayOutputStream(75);
		
		final byte[] hashedPassPhrase = WSHasher.getHashedPassPhraseInTime(passPhrase, currentTimeInMinutes);
		final byte[] hashedActionNumber = WSHasher.getHashedActionNumberInTime(passPhrase, actionNumber, currentTimeInMinutes);
		
		final byte[] eightRandomBytes = new byte[8];
		final SecureRandom scRandom = new SecureRandom();
		scRandom.nextBytes(eightRandomBytes);
		
		hashedContents.write(hashedPassPhrase, 0, 64);
		hashedContents.write(hashedActionNumber, 0, 3);
		hashedContents.write(eightRandomBytes, 0, 8);
		
	}

	public static byte[] getHashedPassPhraseInTime(CharSequence passPhrase,
			long currentTimeMinutes) {

		byte[] bytePassPhraseArray = passPhrase.toString().getBytes(Charsets.UTF_8);
		byte[] timeBytes = ByteBuffer.allocate(8).putLong(currentTimeMinutes).array();

		byte[] allBytes = new byte[bytePassPhraseArray.length + timeBytes.length - 4];
		System.arraycopy(bytePassPhraseArray, 0, allBytes, 0, bytePassPhraseArray.length);
		System.arraycopy(timeBytes, 4, allBytes, bytePassPhraseArray.length, timeBytes.length - 4);
		
		Arrays.sort(allBytes);
		
		return DigestUtils.sha512(allBytes);

	}

	public static byte[] getHashedActionNumberInTime(CharSequence passPhrase, int actionNumber, long currentTimeMinutes) {

		byte[] bytePassPhraseArray = passPhrase.toString().getBytes(Charsets.UTF_8);
		byte[] actionBytes = ByteBuffer.allocate(4).putInt(actionNumber).array();
		byte[] timeBytes = ByteBuffer.allocate(8).putLong(currentTimeMinutes).array();

		byte[] allBytes = new byte[bytePassPhraseArray.length + timeBytes.length - 4 + 1];
		
		System.arraycopy(bytePassPhraseArray, 0, allBytes, 0,
				bytePassPhraseArray.length);
		System.arraycopy(timeBytes, 4, allBytes,
				bytePassPhraseArray.length, timeBytes.length - 4);
		System.arraycopy(actionBytes, actionBytes.length - 1, allBytes, allBytes.length - 1, 1);
		

		return ArrayUtils.subarray(DigestUtils.sha512(allBytes), 0, 3);
		
	}
	
	private static byte[] xor(final byte[] inputByteArray, final byte timeByte) {

		byte[] outputByteArray = new byte[inputByteArray.length];

		for (int intCount = 0; intCount < inputByteArray.length; intCount++) {
			outputByteArray[intCount] = (byte) (inputByteArray[intCount] ^ timeByte);
		}
		return outputByteArray;
	}

	public String encode() {
		return Base64.encodeBase64URLSafeString(hashedContents.toByteArray());
	}

	public boolean matches(String encodedKnock) {

		if(encodedKnock.length() != 100) {
			
			return false;
			
		} else {
			
			String encodedString = Base64.encodeBase64URLSafeString(hashedContents.toByteArray()).substring(0, 89);
			final String currentString = encodedKnock.substring(0, 89);
			
			return encodedString.equalsIgnoreCase(currentString);
		}
				
	}

}
