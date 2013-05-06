package net.seleucus.wsp.main;

public final class WSVersion {

	private static final byte[] VERSION = {0,5};

	private WSVersion() {
		// Standard to avoid instantiation 'accidents'
		throw new UnsupportedOperationException();
	}
	
	public static int getMajor() {
		return VERSION[0];
	}
	
	public static int getMinor() {
		return VERSION[1];
	}
	
	public static String getValue() {
		return VERSION[0] + "." + VERSION[1];
	}
	
	public static boolean isValid(final String versionString) {
		return WSVersion.getValue().equalsIgnoreCase(versionString);
	}
	
}
