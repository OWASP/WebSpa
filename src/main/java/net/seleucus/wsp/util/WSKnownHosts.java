package net.seleucus.wsp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class WSKnownHosts {

	private File userWebSpaKnownHostsFile;

	public WSKnownHosts() {
		String userWebSpaKnownHostsFileName = System.getProperty("user.home")
				+ File.separator + ".webspa" + File.separator + "known_hosts";

		userWebSpaKnownHostsFile = new File(userWebSpaKnownHostsFileName);
	}

	public boolean checkKnownHost(final URL url, final String fingerprint) {

		boolean knownHost = false;

		if (userWebSpaKnownHostsFile.exists()) {
			FileReader fr;

			try {
				fr = new FileReader(userWebSpaKnownHostsFile);
				BufferedReader br = new BufferedReader(fr);

				String line;
				while ((line = br.readLine()) != null) {
					String urlHash[] = line.split("=");

					if (url.getHost() == urlHash[0]
							&& fingerprint == urlHash[1]) {
						knownHost = true;
						// TODO what if url ok, but fingerprint not? -> return a 'state'
						break;
					}
				}

				br.close();
				fr.close();
			} catch (FileNotFoundException fnfEx) {

			} catch (IOException ioEx) {

			}
		}

		return knownHost;
	}

	public void addKnownHost(final URL url, final String fingerprint) {

	}

}
