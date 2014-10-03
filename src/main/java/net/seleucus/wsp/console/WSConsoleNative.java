package net.seleucus.wsp.console;

public class WSConsoleNative extends WSConsole {

	@Override
	public String readLine(String string) {
		return System.console().readLine(string);
	}

	@Override
	public char[] readPassword(String string) {
		return System.console().readPassword(string);
	}

	@Override
	public void println(String string) {
		System.console().printf(string);
	}

}
