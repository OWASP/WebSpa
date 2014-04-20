package net.seleucus.wsp.client;

import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;

public class WSClient extends WSGestalt {

	public WSClient(final WebSpa myWebSpa) {
		super(myWebSpa);		
	}
	
	@Override
	public void exitConsole() {
		printlnWithTimeStamp("Goodbye!\n");
	}
	
	@Override
	public void runConsole() {
		
		println("");
		println("WebSpa - Single HTTP/S Request Authorisation");
		println("version " + WSVersion.getValue() + " (webspa@seleucus.net)"); 		
		println("");

		String host = readLineRequired("Host [e.g. https://localhost/]");
		CharSequence password = readPasswordRequired("Your pass-phrase for that host");
		int action = readLineRequiredInt("The action number", 0, 9);
		
		WSRequestBuilder myClient = new WSRequestBuilder(host, password, action);
		String knock = myClient.getKnock();
		
		println("");
		printlnWithTimeStamp("Your WebSpa Knock is:");
		println("\n" + knock + "\n");
		
		// URL nonsense 
		final String sendChoice = readLineOptional("Send the above URL [Y/n]");
		println("");
		
		if("yes".equalsIgnoreCase(sendChoice) ||
			"y".equalsIgnoreCase(sendChoice) ||
			sendChoice.isEmpty() ) {
			
			WSConnection myConnection = new WSConnection(knock);
			
			printlnWithTimeStamp(myConnection.getActionToBeTaken());
		
			myConnection.sendRequest();

			// is the connection HTTPS
			if( myConnection.isHttps() ) {

				println(myConnection.getCertSHA1Hash());
				
				final String trustChoice = readLineRequired("Are you sure you want to continue connecting [y/n]");
				
				if("yes".equalsIgnoreCase(trustChoice) ||
						"y".equalsIgnoreCase(trustChoice) ) {
					
					myConnection.sendRequest();
					printlnWithTimeStamp(myConnection.responseMessage());
					printlnWithTimeStamp("Response Code : " + myConnection.responseCode());
					
				} else {
					
					printlnWithTimeStamp("Nothing was sent.");
					
				}
				
			} else {
				
				myConnection.sendRequest();
				printlnWithTimeStamp(myConnection.responseMessage());
				printlnWithTimeStamp("Response Code : " + myConnection.responseCode());
	
			}
						
		}

	}

}
