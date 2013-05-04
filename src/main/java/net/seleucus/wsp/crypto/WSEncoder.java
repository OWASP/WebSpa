package net.seleucus.wsp.crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;

public class WSEncoder {

	public byte[] decode(String base64String) throws DecoderException {
		return Base64.decodeBase64(base64String);
	}

	public String encode(byte[] binaryData) {
		return Base64.encodeBase64URLSafeString(binaryData); 
	}

}
