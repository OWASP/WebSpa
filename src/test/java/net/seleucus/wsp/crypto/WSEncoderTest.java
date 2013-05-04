package net.seleucus.wsp.crypto;

import static org.junit.Assert.*;

import net.seleucus.wsp.crypto.WSEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class WSEncoderTest {

	@Test
	public final void shouldDecodeToTheSameEncodedString() throws DecoderException {
		WSEncoder myEncoder = new WSEncoder();
		byte [] sameData = {Byte.MIN_VALUE, -99, -67, 0, 33, 64, 100, Byte.MIN_VALUE};
		assertArrayEquals(sameData, myEncoder.decode( myEncoder.encode(sameData) ) );
	}
	
	@Test
	public final void shouldEncodeToCorrectValue() {
		// http://tools.ietf.org/html/rfc4648#section-4
		// Base64URLSafe(14fb9c03d97e) => FPucA9l-
		WSEncoder myEncoder = new WSEncoder();
		String hexString = "14fb9c03d97e";
		try {
			byte [] binaryData = Hex.decodeHex(hexString.toCharArray());
			assertEquals("FPucA9l-", myEncoder.encode(binaryData));
		} catch (DecoderException e) {
			  fail("DecoderException was thrown in shouldEncodeToCorrectValue"); 
		}
	}
	
	@Test
	public final void shouldDecodeToCorrectValue() {
		// http://tools.ietf.org/html/rfc4648#section-4
		// Base64DecodeURLSafe(FPucA9l-FPucAw==) => 14fb9c03d97e14fb9c03
		String base64String = "FPucA9l-FPucAw==";
		String hexString = "14fb9c03d97e14fb9c03";

		WSEncoder myEncoder = new WSEncoder();		
		try {
			
			assertArrayEquals(myEncoder.decode(base64String), Hex.decodeHex(hexString.toCharArray()));
			
		} catch (DecoderException e) {
			
			fail("DecoderException was thrown in shouldDecodeToCorrectValue"); 

		}
	}
}
