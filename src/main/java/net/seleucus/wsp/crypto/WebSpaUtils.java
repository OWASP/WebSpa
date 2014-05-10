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
	
	protected static boolean equals(byte[] first, byte[] second) {
		
		boolean arraysAreEqual = false;
				
		boolean foundOneValueThatDoesNotMatch = false;
		
		if(first == null || second == null) {
			
			arraysAreEqual = false;
			
		} else {

			int maximum = (first.length < second.length) ? first.length : second.length;

			for(int count = 0; count < maximum; count++) {
				
				if(first[count] != second[count]) {
					
					foundOneValueThatDoesNotMatch = true;
					
				}
			}

			arraysAreEqual = ! (foundOneValueThatDoesNotMatch);

			if(first.length != second.length) {
				
				arraysAreEqual = false;
				
			}
		}
		
		
		return arraysAreEqual;
		
	}

}
