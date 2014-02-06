package net.seleucus.wsp.crypto;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

public class WebSpaEncoder {

	private CharSequence passPhrase;	
	private int actionNumber;
	
	public WebSpaEncoder(CharSequence passPhrase, int actionNumber) {
		this.passPhrase = passPhrase;
		this.actionNumber = actionNumber;
	}
	
	public String getKnock() {
		byte[] passKnockBytes = PassPhraseCrypto.getHashedPassPhraseNow(passPhrase);
		byte[] actionKnockBytes = ActionNumberCrypto.getHashedActionNumberNow(passPhrase, actionNumber);
		
		byte[] allBytes = ArrayUtils.addAll(passKnockBytes, actionKnockBytes);
		
		return Base64.encodeBase64URLSafeString(allBytes);
	}

	public static String encode(CharSequence passPhrase) {
		byte[] passKnockBytes = PassPhraseCrypto.getHashedPassPhraseNow(passPhrase);
		return Base64.encodeBase64URLSafeString(passKnockBytes);
	}

	public static boolean matches(CharSequence rawPassword, String webSpaRequest) {

		if(webSpaRequest.length() != 100) {
			
			return false;
			
		} else {
			
			byte[] webSpaBytes = Base64.decodeBase64(webSpaRequest);
			byte[] passBytes = ArrayUtils.subarray(webSpaBytes, 0, 51);
			byte randomByte = webSpaBytes[0];
			
			byte[] expectedBytes = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(rawPassword, randomByte);
			
			return Arrays.equals(passBytes, expectedBytes);
		}
	}
	
	public static int getActionNumber(CharSequence rawPassword, String webSpaRequest) {
		
		int returnAction = -1;
		
		if(WebSpaEncoder.matches(rawPassword, webSpaRequest)) {
			
			byte[] webSpaBytes = Base64.decodeBase64(webSpaRequest);
			byte[] actionBytes = ArrayUtils.subarray(webSpaBytes, 51, 75);
			byte[] actionSalt = ArrayUtils.subarray(actionBytes, 0, 4);
						
			for(int count = 0; count <= 9; count++) {
				
				byte[] calculatedActionBytes = 
						ActionNumberCrypto.getHashedActionNumberNowWithSalt(rawPassword, count, actionSalt);
				
				if(Arrays.equals(calculatedActionBytes, actionBytes)) {
					returnAction = count;
					break;
				}
				
			} // for loop
		}
		
		return returnAction;
	}

}
