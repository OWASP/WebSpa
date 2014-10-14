package net.seleucus.wsp.client;

import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;
import net.seleucus.wsp.util.WSUtil;

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
		
		myConsole.println("");
		myConsole.println("WebSpa - Single HTTP/S Request Authorisation");
		myConsole.println("version " + WSVersion.getValue() + " (webspa@seleucus.net)"); 		
		myConsole.println("");

		String host = readLineRequired("Host [e.g. https://localhost/]");
		CharSequence password = readPasswordRequired("Your pass-phrase for that host");
		int action = readLineRequiredInt("The action number", 0, 9);
		
		WSRequestBuilder myClient = new WSRequestBuilder(host, password, action);
		String knock = myClient.getKnock();
		
		myConsole.println("");
		printlnWithTimeStamp("Your WebSpa Knock is:");
		myConsole.println("\n" + knock + "\n");
		
		// URL nonsense 
		final String sendChoice = readLineOptional("Send the above URL [Y/n]");
		myConsole.println("");
		
		if (WSUtil.isAnswerPositive(sendChoice) || sendChoice.isEmpty()) {
			
			WSConnection myConnection = new WSConnection(knock);
			
			printlnWithTimeStamp(myConnection.getActionToBeTaken());
		
			myConnection.sendRequest();

			// is the connection HTTPS
			if( myConnection.isHttps() ) {

				try {
					myConsole.println(myConnection.getCertSHA1Hash());
				} catch (NullPointerException npEx) {
					myConsole
							.println("Couldn't get the SHA1 hash of the server certificate - probably a self signed certificate.");
					if (!WSUtil.hasMinJreRequirements(1, 7)) {
						myConsole
								.println("Be sure to run WebSpa with a JRE 1.7 or greater.");
					} else {
						myConsole
								.println("An exception was raised when reading the server certificate.");
						npEx.printStackTrace();
					}
				}

				final String trustChoice = readLineRequired("Are you sure you want to continue connecting [y/n]");
				
				if(WSUtil.isAnswerPositive(trustChoice)) {
					
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
