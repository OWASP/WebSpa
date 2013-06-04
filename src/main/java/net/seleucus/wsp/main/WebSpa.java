package net.seleucus.wsp.main;

import java.io.Console;

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
			
		} // java -jar web-spa.jar -server
		  else if (args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[2])) {
			  
			  runServer();
			  
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
		System.out.println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (subere@uncon.org)"); 
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
	
	private static void runServer() {

		System.out.println("");
		System.out.println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (subere@uncon.org)"); 
		System.out.println("");

		final Console console = System.console();
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
		
		System.out.println("\nGoodbye!");
		System.exit(0);
	}

}
