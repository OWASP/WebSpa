package net.seleucus.wsp.crypto;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.util.EncodingUtils;

public class PassPhraseEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence passPhrase) {
		byte[] passKnockBytes = PassPhraseCrypto.getHashedPassPhraseNow(passPhrase);
		return Base64.encodeBase64URLSafeString(passKnockBytes);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String webSpaRequest) {

		if(webSpaRequest.length() != 100) {
			
			return false;
			
		} else {
			
			byte[] webSpaBytes = Base64.decodeBase64(webSpaRequest);
			byte[] passBytes = EncodingUtils.subArray(webSpaBytes, 0, 51);
			byte randomByte = webSpaBytes[50];
			
			byte[] expectedBytes = PassPhraseCrypto.getHashedPassPhraseNowWithSalt(rawPassword, randomByte);
			
			return Arrays.equals(passBytes, expectedBytes);
		}
	}

}
