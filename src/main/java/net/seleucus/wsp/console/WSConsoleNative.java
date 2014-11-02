package net.seleucus.wsp.console;

import net.seleucus.wsp.db.WSUsers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WSConsoleNative extends WSConsole {

	private final static Logger LOGGER = LoggerFactory.getLogger(WSConsoleNative.class);    

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
