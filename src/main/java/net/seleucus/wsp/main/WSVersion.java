package net.seleucus.wsp.main;

public class WSVersion extends WSGestalt {

	private static final byte[] VERSION = {0,7};

	public WSVersion(WebSpa myWebSpa) {
		super(myWebSpa);
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
	
	public static boolean isCurrentVersion(final String versionString) {
		return WSVersion.getValue().equalsIgnoreCase(versionString);
	}
	
	@Override
	public void exitConsole() {
		// Nothing to add here...
	}

	@Override
	public void runConsole() {
		println(WSVersion.getValue());
	}
	
}
