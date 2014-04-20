package net.seleucus.wsp.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class WSConnection {
	
	protected static String[] ACTION_CAN_BE_TAKEN = {
		"...",
		"The URL is neither HTTP nor HTTPS: No action will be taken",
		"Malformed URL: No action will be taken", 
		"I/O Exception: No action will be taken", 
		"Sending the above HTTP request",
		"Sending the above HTTPS request"
	};

	protected static String[] RESPONSE_MESSAGES = {
		"...",
		"I/O Exception: While attempting to connect",
		"Reponse received"
	};

	private int action, response;
			
	private URLConnection connection = null;
	
	public WSConnection(final String knock) {
		
		action = 0;
		response = 0;
		
		try {
			
			URL knockURL = new URL(knock);

			if (knockURL.getProtocol().toLowerCase().equals("https")) {
				action = 5;
			    trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) knockURL.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				connection = https;
			} else {
				action = 4;
				connection = (HttpURLConnection) knockURL.openConnection();
			}
			
			// connection = knockURL.openConnection();
			
			setRequestProperties();
			
			/*
			if(connection instanceof HttpURLConnection) {
				action = 4;
			}
			
			if(connection instanceof HttpsURLConnection) {
				action = 5;
				trustAllHosts();
				((HttpsURLConnection) connection).setHostnameVerifier(DO_NOT_VERIFY);
			}
			*/
		} catch (MalformedURLException e) {
			
			action = 2;
			
		} catch (IOException e) {
			
			action = 3;
			
		} 
	}
	

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// return new java.security.cert.X509Certificate[] {};
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setRequestProperties() {

		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,el;q=0.6");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		
	}

	public void sendRequest() {
		
		if(action == 4 || action == 5) {
			try {

				connection.connect();
				response = 2;

			} catch (IOException e) {
				// "I/O Exception: While attempting to connect"
				response = 1;

			}
		}
	}

	public String responseCode() {

		if(action == 4) {
			try {

				return "" + ((HttpURLConnection) connection).getResponseCode();

			} catch (IOException e) {
				
				return "---";

			}
			
		} else if(action == 5) {
			
			try {

				return "" + ((HttpsURLConnection) connection).getResponseCode();

			} catch (IOException e) {
				
				e.printStackTrace();
				return "***";

			}			
			
		} else {
			
			return "000";
			
		}

	}

	public String getActionToBeTaken() {
		return ACTION_CAN_BE_TAKEN[action];
	}

	public String responseMessage() {
		return RESPONSE_MESSAGES[response];
	}

	public boolean isHttps() {
		return (connection instanceof HttpsURLConnection);
	}

	public String getCertHash() {
		
		String hashCode = "n/a";
		
		if(action == 5) {
			
			try {
				
				Certificate[] certs = ((HttpsURLConnection) connection).getServerCertificates();
				
				for(Certificate cert : certs) {
					
					hashCode = cert.getPublicKey().getFormat();
				}
				
			} catch (SSLPeerUnverifiedException e) {
				
				hashCode = "No Certificate Hash Values";
			}
					

			
		}
		
		return hashCode;
		
	}
}
