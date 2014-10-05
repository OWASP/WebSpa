package net.seleucus.wsp.crypto;


public class SCryptPassPhraseCryptoTest {

	// A static password value specified as a char sequence
	private static final CharSequence PASS_PHRASE = "IfY0u0nlyKn3w!";
	/*
	@Test
	public final void shouldImplementThisAfterWeDecideOnTheWorkFactors() {
		
		final String hashedPassPhraseWithSalt = SCryptPassPhraseCrypto.getHashedPassPhraseWithSalt(PASS_PHRASE);
		assertTrue(SCryptUtil.check(PASS_PHRASE.toString(), hashedPassPhraseWithSalt));
		
	}
	
	@Test
	public final void shouldTestRandomValueGeneration() {
		
		SecureRandom sRandom = new SecureRandom();
		final int nFactor = (int) (128 * ( Math.pow(2, sRandom.nextInt(3) ) )) ;
		final int rFactor = 32 + sRandom.nextInt(32);
		final int pFactor = 16 + sRandom.nextInt(8);
		
		fail("Factors:\nN = " + nFactor + "\nr = " + rFactor + "\np = " + pFactor);
	}
	*/

}
