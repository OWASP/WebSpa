package net.seleucus.wsp.main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Console;

import net.seleucus.wsp.client.WSClient;

public class WebSpa {

	private static final String[] ALLOWED_FIRST_PARAM = {"-help", "-client", "-server", "-version"};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length < 1) {
			
			showHelp();
			
		} // java -jar web-spa.jar -help
		  else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[0])) {
			
			  showHelp();
			
		} // java -jar web-spa.jar -client
		  else if (args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[1])) {
		
			  runClient(System.console());
		} // java -jar web-spa.jar -server
		  else if (args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[2])) {
			  
			  runServer(System.console());
			  
		} // java -jar web-spa.jar -version
		  else if (args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[3])) {
		
			System.out.println(WSVersion.getValue());
			
		}
		  else {
			
			showHelp();
			
		}
	}
	
	private static void showHelp() {

		System.out.println("");
		System.out.println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		System.out.println("");
		System.out.println("Usage: java -jar web-spa.jar [-option]");
		System.out.println("");
		System.out.println("\t-client         : Run the client, generate valid requests");
		System.out.println("\t-help           : Print this usage message");
		System.out.println("\t-server         : Run the server");
		System.out.println("\t-version        : " + WSVersion.getValue());
		System.out.println("");
		System.out.println("Examples:");
		System.out.println("");
		System.out.println("java -jar web-spa.jar -client");
		System.out.println("java -jar web-spa.jar -server");
		System.out.println("");
		
	}
	
	private static void runServer(final Console console) {

		System.out.println("");
		System.out.println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		System.out.println("");

		if (console == null) {
			System.err.println("Could not get console; will exit now...");
			System.exit(1);
		}
		
		System.out.println("This is a holding prompt, type \"exit\" to quit");        
		
		do {

			String command = console.readLine("\nweb-spa-server>");
			if( "exit".equalsIgnoreCase(command) ||
				"quit".equalsIgnoreCase(command) ||
				"laterz".equalsIgnoreCase(command) ||
				"bye".equalsIgnoreCase(command) ) {
				break;
			}

		} while (true);
		
		System.out.println("\nGoodbye!\n");
		System.exit(0);
	}
	
	private static void runClient(final Console console) {

		System.out.println("");
		System.out.println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		System.out.println("");

		if (console == null) {
			System.err.println("Could not get console; will exit now...");
			System.exit(1);
		}

		String host = console.readLine("Please enter the target host [e.g. http://localhost/]: ");
		CharSequence password = java.nio.CharBuffer.wrap(console.readPassword("Please enter your pass-phrase for that host: "));
		int action = -1;
		do {

			String actionString = console.readLine("Please enter the action you want to execute on the host [1-16]: ");
			action = Integer.parseInt(actionString);
			
		} while ( (action < 1) || (action > 16) );
		
		
		WSClient myClient = new WSClient(host, password, action);
		String knock = myClient.getKnock();
		
		System.out.println("\nYour Web-Spa Knock is:\n\n" + knock + "\n");
		
		// Clipboard nonsense 
		final String choice = console.readLine("Copy the above URL to the clipboard? ");
				
		if("yes".equalsIgnoreCase(choice) ||
			"y".equalsIgnoreCase(choice) ||
			choice.isEmpty() ) {
			
			final StringSelection data = new StringSelection(knock);
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data, data);
			
		}
		
		System.out.println("\nGoodbye!\n");
		System.exit(0);
	}

}
