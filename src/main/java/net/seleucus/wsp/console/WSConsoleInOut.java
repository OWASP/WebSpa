package net.seleucus.wsp.console;

import java.util.Scanner;

public class WSConsoleInOut extends WSConsole {

	@Override
	public String readLine(String string) {
		System.out.println(string);

		Scanner in = new Scanner(System.in);
		return in.nextLine();
	}

	@Override
	public char[] readPassword(String string) {
		System.out.print(string);

		return this.readLine("").toCharArray();
	}

	@Override
	public void println(String string) {
		System.out.println(string);
	}

}
