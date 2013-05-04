package net.seleucus.wsp.crypto;

import org.apache.commons.codec.digest.DigestUtils;

public class WebSpaUtils {

	private static final int ITERATIONS = 1024;
	
	protected static byte[] digest(byte[] value) {
		
		for (int i = 0; i < ITERATIONS; i++) {
			value = DigestUtils.sha512(value);
        }
        
		return value;
		
    }
	
	protected static byte[] xor(final byte[] inputByteArray, final byte timeByte) {

		byte[] outputByteArray = new byte[inputByteArray.length];

		for (int intCount = 0; intCount < inputByteArray.length; intCount++) {
			outputByteArray[intCount] = (byte) (inputByteArray[intCount] ^ timeByte);
		}
		
		return outputByteArray;
		
	}

}
