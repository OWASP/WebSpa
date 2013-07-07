package net.seleucus.wsp.main;

import java.io.Console;

public abstract class WSGestalt {

	private Console myConsole;

	public WSGestalt(WebSpa myWebSpa) {
		
		this.myConsole = myWebSpa.getConsole();
		
	}
	
	public Console getMyConsole() {
		
		return myConsole;
		
	}
	
	public abstract void exitConsole();

	public abstract void runConsole();
	
}