package net.seleucus.wsp.console;

import org.apache.commons.lang3.StringUtils;


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
	public void println(final String input) {
		
		String output = StringUtils.replace(input, "%", "%%") + "\n"; 
		System.console().printf(output);
		
	}

}
