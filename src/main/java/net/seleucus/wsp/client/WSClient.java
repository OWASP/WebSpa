package net.seleucus.wsp.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;

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
		println("version " + WSVersion.getValue() + " (WebSpa@seleucus.net)"); 		
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
		final String choice = readLineOptional("Send the above URL [Y/n]");
		println("");
		
		if("yes".equalsIgnoreCase(choice) ||
			"y".equalsIgnoreCase(choice) ||
			choice.isEmpty() ) {
			
			URL webKnock;
			try {
				webKnock = new URL(knock);
				
				if(webKnock.getProtocol().equalsIgnoreCase("http")) {
					
					printlnWithTimeStamp("Sending the above HTTP request");
					
					HttpURLConnection straightConnection = (HttpURLConnection) webKnock.openConnection();
					straightConnection.setRequestMethod("GET");
					straightConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					straightConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
					straightConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,el;q=0.6");
					straightConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

					int responseCode = straightConnection.getResponseCode();
					printlnWithTimeStamp("Response Code : " + responseCode);

					straightConnection.disconnect();
					
				} else if(webKnock.getProtocol().equalsIgnoreCase("https")) {
					
					printlnWithTimeStamp("Sending the above HTTPS request");
					
					HttpsURLConnection sslConnection = (HttpsURLConnection) webKnock.openConnection();

					sslConnection.setRequestMethod("GET");
					sslConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					sslConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
					sslConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,el;q=0.6");
					sslConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");

					printlnWithTimeStamp("Response Code : " + sslConnection.getResponseCode());
					printlnWithTimeStamp("Cipher Suite: " + sslConnection.getCipherSuite());
					
					Certificate[] certs = sslConnection.getServerCertificates();
					for(Certificate cert : certs){

						printlnWithTimeStamp("Cert Type : " + cert.getType());
						printlnWithTimeStamp("Cert Hash Code : " + cert.hashCode());
						printlnWithTimeStamp("Cert Public Key Algorithm : " 
								+ cert.getPublicKey().getAlgorithm());
						printlnWithTimeStamp("Cert Public Key Format : " 
								+ cert.getPublicKey().getFormat());
						printlnWithTimeStamp("");
					}

					sslConnection.disconnect();

				} else {
					
					printlnWithTimeStamp("The URL is neither HTTP nor HTTPS");
					printlnWithTimeStamp("No action will be taken");
					
				}

			} catch (MalformedURLException mURLEx) {
				
				printlnWithTimeStamp("The URL is Malformed - " + mURLEx.getMessage());
				printlnWithTimeStamp("No action will be taken");
				
			} catch (IOException iOEx) {
				
				printlnWithTimeStamp("An I/O Exception Occured - " + iOEx.getMessage());
				printlnWithTimeStamp("No action will be taken");
				
			}
						
		}

	}

}
