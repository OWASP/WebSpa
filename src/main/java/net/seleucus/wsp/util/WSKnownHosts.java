package net.seleucus.wsp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class manages the known hosts file in the .webspa folder in the users
 * home folder.<br/>
 * Each line in the file is an entry for a given IP address and the information
 * is separated by a space. The information in the file is
 * <ul>
 * <li>IP address</li>
 * <li>Algorithm</li>
 * <li>Certificate finger print</li>
 * </ul>
 */
public class WSKnownHosts {

	private String userWebSpaKnownHostsFileName;
	private File userWebSpaKnownHostsFile;

	public WSKnownHosts() {
		userWebSpaKnownHostsFileName = System.getProperty("user.home") + File.separator + ".webspa" + File.separator + "known_hosts";
		userWebSpaKnownHostsFile = new File(userWebSpaKnownHostsFileName);
	}

	/**
	 * Adds an entry to the users known hosts file.
	 * 
	 * @throws RuntimeException
	 *             if the known hosts file cannot be created or entries added.
	 */
	public void addKnownHost(final String ipAddr, final String certAlgorithm, final String certFingerprint) {
		if (!userWebSpaKnownHostsFile.exists()) {
			try {
				userWebSpaKnownHostsFile.getParentFile().mkdirs();
				userWebSpaKnownHostsFile.createNewFile();
			} catch (IOException ioEx) {
				throw new RuntimeException("Couldn't create the WebSPA known hosts file: " + userWebSpaKnownHostsFileName);
			}
		}

		StringBuilder newEntry = new StringBuilder();
		newEntry.append(ipAddr);
		newEntry.append(" ");
		newEntry.append(certAlgorithm);
		newEntry.append(" ");
		newEntry.append(certFingerprint);
		newEntry.append(System.lineSeparator());

		try {
			FileWriter fw = new FileWriter(userWebSpaKnownHostsFile, true);
			fw.append(newEntry);
			fw.close();
		} catch (IOException e) {
			throw new RuntimeException("Problems adding a new entry to the WebSPA known hosts file: " + userWebSpaKnownHostsFileName);
		}
	}

	/**
	 * Checks if this entry already exists in the users known hosts file.<br/>
	 * The possible return values are:
	 * <ul>
	 * <li>NULL: no information for the given IP address exists
	 * <li>TRUE: information found and finger print matches
	 * <li>FALSE: information found but finger print doesn't match
	 * </ul>
	 */
	public Boolean check(final String ipAddr, final String certAlgorithm, final String certFingerprint) {
		Boolean fingerprintFits = null;

		if (userWebSpaKnownHostsFile.exists()) {
			FileReader fr;

			try {
				fr = new FileReader(userWebSpaKnownHostsFile);
				BufferedReader br = new BufferedReader(fr);

				String line;
				while ((line = br.readLine()) != null) {
					String urlAlgoHash[] = line.split(" ");

					if (ipAddr.equals(urlAlgoHash[0]) && certAlgorithm.equals(urlAlgoHash[1])) {
						if (certFingerprint.equals(urlAlgoHash[2])) {
							fingerprintFits = Boolean.TRUE;
						} else {
							fingerprintFits = Boolean.FALSE;
						}

						break;
					}
				}

				br.close();
				fr.close();
			} catch (IOException ioEx) {
				throw new RuntimeException("Problems reading the WebSPA known hosts file: " + userWebSpaKnownHostsFileName);
			}
		}

		return fingerprintFits;
	}

}
