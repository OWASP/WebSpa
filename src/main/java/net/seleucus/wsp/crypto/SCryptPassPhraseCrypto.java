package net.seleucus.wsp.crypto;


public final class SCryptPassPhraseCrypto extends WebSpaUtils {
	/*
	protected static String getHashedPassPhraseWithSalt(final CharSequence passPhrase) {
		
		SecureRandom sRandom = new SecureRandom();
		final int nFactor = (int) (128 * ( Math.pow(2, sRandom.nextInt(3) ) )) ;
		final int rFactor = 32 + sRandom.nextInt(32);
		final int pFactor = 16 + sRandom.nextInt(8);
		
		return SCryptUtil.scrypt(passPhrase.toString(), nFactor, rFactor, pFactor);
	    
	}
	*/
}
