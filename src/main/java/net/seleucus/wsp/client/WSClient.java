package net.seleucus.wsp.client;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;

public class WSClient extends WSGestalt {

	public WSClient(final WebSpa myWebSpa) {
		super(myWebSpa);		
	}
	
	@Override
	public void exitConsole() {
		getWSConsole().writer().println("\nGoodbye!\n");
	}
	
	@Override
	public void runConsole() {
		
		getWSConsole().writer().println("");
		getWSConsole().writer().println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		getWSConsole().writer().println("");

		String host = getWSConsole().readLine("Please enter the target host [e.g. http://localhost/]: ");
		CharSequence password = java.nio.CharBuffer.wrap(getWSConsole().readPassword("Please enter your pass-phrase for that host: "));
		int action = -1;
		do {

			String actionString = getWSConsole().readLine("Please enter the action you want to execute on the host [1-16]: ");
			action = Integer.parseInt(actionString);
			
		} while ( (action < 1) || (action > 16) );
		
		
		WSRequestBuilder myClient = new WSRequestBuilder(host, password, action);
		String knock = myClient.getKnock();
		
		System.out.println("\nYour Web-Spa Knock is:\n\n" + knock + "\n");
		
		// Clipboard nonsense 
		final String choice = getWSConsole().readLine("Copy the above URL to the clipboard? ");
				
		if("yes".equalsIgnoreCase(choice) ||
			"y".equalsIgnoreCase(choice) ||
			choice.isEmpty() ) {
			
			final StringSelection data = new StringSelection(knock);
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
			
		}

	}

}
