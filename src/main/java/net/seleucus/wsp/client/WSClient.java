package net.seleucus.wsp.client;

import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;
import net.seleucus.wsp.util.WSUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSClient extends WSGestalt {

	private final static Logger LOGGER = LoggerFactory.getLogger(WSClient.class);    

	public WSClient(final WebSpa myWebSpa) {
		super(myWebSpa);		
	}
	
	@Override
	public void exitConsole() {
		LOGGER.info("Goodbye!");
	}
	
	@Override
	public void runConsole() {
		
		LOGGER.info("");
		LOGGER.info("WebSpa - Single HTTP/S Request Authorisation");
		LOGGER.info("version " + WSVersion.getValue() + " (webspa@seleucus.net)"); 		
		LOGGER.info("");

		String host = readLineRequired("Host [e.g. https://localhost/]");
		CharSequence password = readPasswordRequired("Your pass-phrase for that host");
		int action = readLineRequiredInt("The action number", 0, 9);
		
		WSRequestBuilder myClient = new WSRequestBuilder(host, password, action);
		String knock = myClient.getKnock();
		
		LOGGER.info("Your WebSpa Knock is: {}", knock);
		
		// URL nonsense 
		final String sendChoice = readLineOptional("Send the above URL [Y/n]");
		
		if ( WSUtil.isAnswerPositive(sendChoice) || sendChoice.isEmpty() ) {
			
			WSConnection myConnection = new WSConnection(knock);
			
			LOGGER.info(myConnection.getActionToBeTaken());
		
			myConnection.sendRequest();

			// is the connection HTTPS
			if( myConnection.isHttps() ) {
				// TODO add known hosts check and handling here
				// get fingerprint and algorithm from certificate
				// myConnection.getCertificateAlgorithm()
				// myConnection.getCertificateFingerprint()

				// get fingerprint from known hosts file
				// WSKnownHosts.getFingerprint(host-ip, algorithm)

				// if a fingerprint is found compare fingerprints, if not equal warn and exit
				// else ask to store new fingerprint to known hosts
				// WSKnownHosts.store...(host-ip, algorithm, fingerprint);

				try {
					
					LOGGER.info(myConnection.getCertSHA1Hash());
					
				} catch (NullPointerException npEx) {
					
					LOGGER.info("Couldn't get the SHA1 hash of the server certificate - probably a self signed certificate.");
					
					if (!WSUtil.hasMinJreRequirements(1, 7)) {
						LOGGER.error("Be sure to run WebSpa with a JRE 1.7 or greater.");
					} else {
						LOGGER.error("An exception was raised when reading the server certificate.");
						npEx.printStackTrace();
					}
				}

				final String trustChoice = readLineOptional("Continue connecting [Y/n]");
				
				if( WSUtil.isAnswerPositive(trustChoice) || sendChoice.isEmpty() ) {
					
					myConnection.sendRequest();
					LOGGER.info(myConnection.responseMessage());
					LOGGER.info("HTTPS Response Code: {}", myConnection.responseCode());
					
				} else {
					
					LOGGER.info("Nothing was sent.");
					
				}
				
			} else {
				
				myConnection.sendRequest();
				LOGGER.info(myConnection.responseMessage());
				LOGGER.info("HTTP Response Code: {}", myConnection.responseCode());
	
			}
						
		}

	}

}
