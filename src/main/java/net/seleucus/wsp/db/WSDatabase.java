package net.seleucus.wsp.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class WSDatabase {

	private static final String DB_PATH = "web-spa-db";

	private Connection wsConnection;

	public WSDatabase() throws ClassNotFoundException, SQLException,
			IOException {

		// Check if the database properties file is present, if not create it
		// from the bundled template...
		File propsFile = new File(DB_PATH + ".properties");
		if (!propsFile.exists()) {
			URL bundledPropsLocation = ClassLoader
					.getSystemResource("data/bundled-web-spa-db.properties");
			FileUtils.copyURLToFile(bundledPropsLocation, propsFile);
		}

		// Check if the database script file is present, if not create it from
		// the bundled template...
		File scriptFile = new File(DB_PATH + ".script");
		if (!scriptFile.exists()) {
			URL bundledScriptlocation = ClassLoader
					.getSystemResource("data/bundled-web-spa-db.script");
			FileUtils.copyURLToFile(bundledScriptlocation, scriptFile);
		}

		Class.forName("org.hsqldb.jdbcDriver");
		wsConnection = DriverManager.getConnection("jdbc:hsqldb:" + DB_PATH);

	}

	public synchronized void shutdown() throws SQLException {

		Statement st = wsConnection.createStatement();
		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		wsConnection.close();
	}

	protected synchronized void initialise() {

	}

	protected synchronized void deleteAllDatabaseFiles() throws SQLException {

		this.shutdown();

		String[] extensions = { ".properties", ".script", ".log", ".data",
				".backup" };
		for (String extension : extensions) {
			File dbFile = new File(DB_PATH + extension);
			if (dbFile.exists()) {
				dbFile.delete();
			}
		}

	}

	public synchronized void addUser(String fullName, CharSequence passSeq, String eMail,
			String phone) {
		
		String sqlPassPhrase = "INSERT INTO PASSPHRASES (PASSPHRASE, CREATED) VALUES (?, CURRENT_TIMESTAMP);";
		
		String sqlUsers = "INSERT INTO PUBLIC.USERS (PPID, FULLNAME, EMAIL, PHONE, CREATED, MODIFIED) VALUES " +
				"(SELECT PPID FROM PUBLIC.PASSPHRASES WHERE PASSPHRASE = ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
		
        try {
			
        	PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlPassPhrase);
        	psPassPhrase.setString(1, passSeq.toString());
        	psPassPhrase.executeUpdate();
        	
        	psPassPhrase.close();
        	
        	PreparedStatement psUsers = wsConnection.prepareStatement(sqlUsers);
        	psUsers.setString(1, passSeq.toString());
        	psUsers.setString(2, fullName);
        	psUsers.setString(3, eMail);
        	psUsers.setString(4, phone);
        	psUsers.executeUpdate();
        	
        	psUsers.close();
        	
		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);
			 
		}

	}

	public synchronized String showUsers() {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append('\n');
		resultsBuffer.append("Web-Spa Users:");
		resultsBuffer.append('\n');
		resultsBuffer.append("___________________________________________________________");
		resultsBuffer.append('\n');
		resultsBuffer.append(StringUtils.rightPad("ID", 4));
		resultsBuffer.append(StringUtils.rightPad("Active", 7));
		resultsBuffer.append(StringUtils.rightPad("Full Name", 25));
		resultsBuffer.append(StringUtils.rightPad("Last Modified", 25));
		resultsBuffer.append('\n');
		resultsBuffer.append("-----------------------------------------------------------");
		resultsBuffer.append('\n');
		String sqlPassUsers = "SELECT PPID, ACTIVE, FULLNAME, MODIFIED FROM PASSPHRASES JOIN USERS ON PASSPHRASES.PPID = USERS.PPID;";
		try {
			Statement stmt = wsConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlPassUsers);

			while (rs.next()) {
				resultsBuffer.append(StringUtils.rightPad(rs.getString(1), 4));
				resultsBuffer.append(StringUtils.rightPad(rs.getString(2), 7));
				resultsBuffer.append(StringUtils.rightPad(StringUtils.abbreviate(rs.getString(3), 24), 25));
				resultsBuffer.append(rs.getString(4).substring(0, 23));
				resultsBuffer.append('\n');
			}
			resultsBuffer.append("___________________________________________________________");

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);

		}
		
		return resultsBuffer.toString();
	}
	
	public synchronized boolean isPassPhraseInUse(CharSequence passSeq) {
		
		boolean passExists = false;
		
		String sqlPassPhrases = "SELECT PASSPHRASE FROM PASSPHRASES;";
		try {
			Statement stmt = wsConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlPassPhrases);

			while (rs.next()) {
				char[] dbPassPhraseArray = rs.getString(1).toCharArray();
				CharSequence dbPassSeq = CharBuffer.wrap(dbPassPhraseArray);
				
				if(dbPassSeq.equals(passSeq)) {
					passExists = true;
					break;
				}
			}

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
		
		return passExists;
		
	}

	public boolean isPPIDInUse(int ppID) {

		boolean idExists = false;
		
		if(ppID > 0) {

			String sqlidLookup = "SELECT PPID FROM PASSPHRASES;";
			
			try {
				Statement stmt = wsConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sqlidLookup);
				
				while (rs.next()) {
					int dbPpID = rs.getInt(1);
					
					if(dbPpID == ppID) {
						idExists = true;
						break;
					}
				}
				
				rs.close();
				stmt.close();
				
			} catch (SQLException ex) {
				
				throw new RuntimeException(ex);

			}

		} // ppID > 0 
		
		return idExists;
	}

	public boolean getActivationStatus(int ppID) {
		
		boolean activationStatus = false;
		
		if(ppID > 0) {
			
			String sqlActivationLookup = "SELECT ACTIVE FROM PASSPHRASES WHERE PPID = ? ;";
			try {
				PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlActivationLookup);
				psPassPhrase.setInt(1, ppID);
				ResultSet rs = psPassPhrase.executeQuery();
				
				if (rs.next()) {
					activationStatus = rs.getBoolean(1);
				}
				
				rs.close();
				psPassPhrase.close();
				
			} catch (SQLException ex) {

				throw new RuntimeException(ex);

			}
		}
		
		return activationStatus;
	}
	
	public synchronized String getActivationStatusString(final int ppID) {
		
		StringBuilder outputStatusBuffer = new StringBuilder(Byte.MAX_VALUE);
		outputStatusBuffer.append("User with ID: ");
		outputStatusBuffer.append(ppID);
		outputStatusBuffer.append(' ');
		
		String sqlActivationLookup = "SELECT ACTIVE FROM PASSPHRASES WHERE PPID = ? ;";

		PreparedStatement psPassPhrase;
		try {
			psPassPhrase = wsConnection.prepareStatement(sqlActivationLookup);
			psPassPhrase.setInt(1, ppID);
			ResultSet rs = psPassPhrase.executeQuery();
			
			if (rs.next()) {
				
				boolean activationStatus = rs.getBoolean(1);
				
				if(activationStatus == true) {
					
					outputStatusBuffer.append("is active");
					
				} else {
					
					outputStatusBuffer.append("is in-active");
					
				}
				
			} else {
				
				outputStatusBuffer.append("does not exist");
				
			}
			
			rs.close();
			psPassPhrase.close();
			
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
			
		}

		return outputStatusBuffer.toString();
		
	}

	public boolean toggleUserActivation(int ppID) {

		boolean success = false;
		
		if(ppID > 0) {
		
			boolean currentActiveStatus = this.getActivationStatus(ppID);
			boolean oppositeActiveStatus = !currentActiveStatus;
			
			String sqlUpdate = "UPDATE PASSPHRASES SET ACTIVE = ? WHERE PPID = ? ;";
			try {
				PreparedStatement ps = wsConnection.prepareStatement(sqlUpdate);
				ps.setBoolean(1, oppositeActiveStatus);
				ps.setInt(2, ppID);
				
				ps.executeUpdate();
				
			} catch (SQLException ex) {
				
				throw new RuntimeException(ex);
				
			}
		}
		
		return success;
		
	}


}
