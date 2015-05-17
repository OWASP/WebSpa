package net.seleucus.wsp.client;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateEncodingException;

import javax.net.ssl.SSLPeerUnverifiedException;

import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;
import net.seleucus.wsp.util.WSKnownHosts;
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

		if (WSUtil.isAnswerPositive(sendChoice) || sendChoice.isEmpty()) {

			WSConnection myConnection = new WSConnection(knock);

			LOGGER.info(myConnection.getActionToBeTaken());

			myConnection.sendRequest();

			// is the connection HTTPS
			if (myConnection.isHttps()) {
				try {
					WSKnownHosts knownHosts = new WSKnownHosts();

					LOGGER.info(myConnection.getCertSHA1Hash());

					String ipAddr = getIpAddr(host);
					String certAlgorithm = myConnection.getCertificateAlgorithm();
					String certFingerprint = myConnection.getCertificateFingerprint();

					Boolean fingerprintMatches = knownHosts.check(ipAddr, certAlgorithm, certFingerprint);

					if (fingerprintMatches == null) {
						final String storeNewFingerprint = readLineOptional("New fingerprint, add it to the WebSPA known hosts file [Y/n]");

						if (WSUtil.isAnswerPositive(storeNewFingerprint)) {
							knownHosts.addKnownHost(ipAddr, certAlgorithm, certFingerprint);
						}
					} else if (fingerprintMatches.booleanValue()) {
						LOGGER.info("fingerprint matches");
					} else {
						myConsole.println("WARNING: The certificate fingerprint does NOT match with the known hosts file");
					}

				} catch (NullPointerException npEx) {
					LOGGER.info("Couldn't get the SHA1 hash of the server certificate - probably a self signed certificate.");

					if (!WSUtil.hasMinJreRequirements(1, 7)) {
						LOGGER.error("Be sure to run WebSpa with a JRE 1.7 or greater.");
					} else {
						LOGGER.error("An exception was raised when reading the server certificate.");
						npEx.printStackTrace();
					}
				} catch (SSLPeerUnverifiedException e) {
					e.printStackTrace();
				} catch (CertificateEncodingException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				final String trustChoice = readLineOptional("Continue connecting [Y/n]");

				if (WSUtil.isAnswerPositive(trustChoice) || sendChoice.isEmpty()) {

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

	private String getIpAddr(String host) throws MalformedURLException, UnknownHostException {
		URL url = new URL(host);
		return InetAddress.getByName(url.getHost()).getHostAddress();
	}

}
