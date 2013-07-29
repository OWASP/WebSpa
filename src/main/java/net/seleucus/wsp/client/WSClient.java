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
		println("\nGoodbye!\n");
	}
	
	@Override
	public void runConsole() {
		
		println("");
		println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		println("");

		String host = readLineRequired("Please enter the target host [e.g. http://localhost/]: ");
		CharSequence password = readPasswordRequired("Please enter your pass-phrase for that host: ");
		int action = readLineRequiredInt("Please enter the action you want to execute on the host", 0, 9);
		
		WSRequestBuilder myClient = new WSRequestBuilder(host, password, action);
		String knock = myClient.getKnock();
		
		println("\nYour Web-Spa Knock is:\n\n" + knock + "\n");
		
		// Clipboard nonsense 
		final String choice = readLineOptional("Copy the above URL to the clipboard [Y/n]? ");
				
		if("yes".equalsIgnoreCase(choice) ||
			"y".equalsIgnoreCase(choice) ||
			choice.isEmpty() ) {
			
			final StringSelection data = new StringSelection(knock);
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
			
		}

	}

}
