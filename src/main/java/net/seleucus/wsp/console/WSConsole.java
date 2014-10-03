package net.seleucus.wsp.console;

public abstract class WSConsole {

	public static final WSConsole getWsConsole() {
		if (System.console() != null) {
			return new WSConsoleNative();
		} else {
			return new WSConsoleInOut();
		}
	}

	public abstract String readLine(String string);

	public abstract char[] readPassword(String string);

	public abstract void println(String string);

}
