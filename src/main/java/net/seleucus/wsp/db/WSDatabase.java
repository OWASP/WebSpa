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

import net.seleucus.wsp.crypto.WebSpaEncoder;

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

	public synchronized void shutdown() {

		Statement st;
		
		try {

			st = wsConnection.createStatement();
			// Normal shutdown operations...
			st.execute("SHUTDOWN");
			wsConnection.close();
			
		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);

		}

	}

	protected synchronized void deleteAllDatabaseFiles() {

		this.shutdown();

		final String[] extensions = { ".properties", ".script", ".log", 
									  ".data", ".backup" };
		
		for (String extension : extensions) {
			
			File dbFile = new File(DB_PATH + extension);
			
			if (dbFile.exists()) {
			
				dbFile.delete();
				
			}
			
		}	// for loop

	}

	public synchronized void addAction(int ppID, String osCommand, int action) {
		
		final String sqlInsert = "INSERT INTO PUBLIC.ACTIONS_AVAILABLE (PPID, ACTION_NUMBER, COMMAND) VALUES (?, ?, ?); ";
		
		try {
			
			PreparedStatement preStatement = wsConnection.prepareStatement(sqlInsert);
			preStatement.setInt(1, ppID);
			preStatement.setInt(2, action);
			preStatement.setString(3, osCommand);
			
			preStatement.executeUpdate();
			
			preStatement.close();
			
		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
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

	public synchronized boolean isActionNumberInUse(int ppID, int action) {
		
		boolean actionNumberInUse = false;
		
		if(this.isPPIDInUse(ppID)) {
		
			final String sqlSelect = "SELECT ACTION_NUMBER FROM ACTIONS_AVAILABLE WHERE PPID = ? ;";
			
			try {
				
				PreparedStatement stmt = wsConnection.prepareStatement(sqlSelect);
				stmt.setInt(1, ppID);
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					int dbAction = rs.getInt(1);
					
					if(dbAction == action) {
						actionNumberInUse = true;
						break;
					}
				}
				
				rs.close();
				stmt.close();
				
			} catch(SQLException ex) {
				
				throw new RuntimeException(ex);
	
			}
		}
		
		return actionNumberInUse;
		
	}

	public synchronized boolean isPPIDInUse(int ppID) {
	
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
				
			}	// while loop...

			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
		
		return passExists;
		
	}
	
	public synchronized int getActionNumberFromRequest(final int ppID, final String webSpaRequest) {
		
		int actionNumber = -1;
		
		if(ppID > 0) {
			
			String sqlActivationLookup = "SELECT PASSPHRASE FROM PASSPHRASES WHERE PPID = ? ;";
			try {
				PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlActivationLookup);
				psPassPhrase.setInt(1, ppID);
				ResultSet rs = psPassPhrase.executeQuery();
				
				if (rs.next()) {
					char[] dbPassPhraseArray = rs.getString(1).toCharArray();
					CharSequence rawPassword = CharBuffer.wrap(dbPassPhraseArray);
					
					actionNumber = WebSpaEncoder.getActionNumber(rawPassword, webSpaRequest);

				}
				
				rs.close();
				psPassPhrase.close();
				
			} catch (SQLException ex) {

				throw new RuntimeException(ex);

			}
		}

		return actionNumber;
	}

	public synchronized boolean getActivationStatus(int ppID) {
		
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

	public synchronized int getPPIDFromRequest(final String webSpaRequest) {
		
		int output = -1;
		final String sqlPassPhrases = "SELECT PASSPHRASE, PPID FROM PASSPHRASES;";
		
		try {
			Statement stmt = wsConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlPassPhrases);
	
			while (rs.next()) {
				
				char[] dbPassPhraseArray = rs.getString(1).toCharArray();
				final int dbPPID = rs.getInt(2);
				CharSequence rawPassword = CharBuffer.wrap(dbPassPhraseArray);
				
				if(WebSpaEncoder.matches(rawPassword, webSpaRequest)) {
					
					output = dbPPID;
					break;
					
				}
				
			}	// while loop...
	
			rs.close();
			stmt.close();
	
		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
		
		return output;
		
	}

	public synchronized boolean toggleUserActivation(int ppID) {

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

	public synchronized String showActions(final int ppID) {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append('\n');
		resultsBuffer.append("Actions for user with ID: ");
		resultsBuffer.append(ppID);
		resultsBuffer.append('\n');
		resultsBuffer.append("___________________________________________________________");
		resultsBuffer.append('\n');
		resultsBuffer.append(StringUtils.rightPad("ID", 4));
		resultsBuffer.append(StringUtils.rightPad("#", 2));
		resultsBuffer.append(StringUtils.rightPad("O/S Command", 30));
		resultsBuffer.append(StringUtils.rightPad("Last Executed", 25));
		resultsBuffer.append('\n');
		resultsBuffer.append("-----------------------------------------------------------");
		resultsBuffer.append('\n');
		final String sqlSelect = "SELECT AAID, ACTION_NUMBER, COMMAND, LAST_EXECUTED FROM ACTIONS_AVAILABLE WHERE PPID = ? ;";
		try {
			PreparedStatement ps = wsConnection.prepareStatement(sqlSelect);
			ps.setInt(1, ppID);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				resultsBuffer.append(StringUtils.rightPad(rs.getString(1), 4));
				resultsBuffer.append(StringUtils.rightPad(rs.getString(2), 2));
				resultsBuffer.append(StringUtils.rightPad(StringUtils.abbreviate(rs.getString(3), 29), 30));
				final String lastExecuted = rs.getString(4);
				if(lastExecuted != null) {
					resultsBuffer.append(lastExecuted.substring(0, 23));
				} else {
					resultsBuffer.append("has never been executed");
				}
				resultsBuffer.append('\n');
			}
			resultsBuffer.append("___________________________________________________________");
			resultsBuffer.append('\n');

			rs.close();
			ps.close();

		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);

		}
		

		return resultsBuffer.toString();
	}

	public synchronized String showActionDetails(final int aaID) {
		
		StringBuffer resultsBuffer = new StringBuffer();
		
		resultsBuffer.append('\n');
		resultsBuffer.append("Action with ID: ");
		resultsBuffer.append(aaID);
		resultsBuffer.append('\n');
		
		final String sqlSelect = "SELECT PPID, COMMAND, ACTION_NUMBER, LAST_EXECUTED, LAST_RUN_SUCCESS, IP_ADDR FROM ACTIONS_AVAILABLE WHERE AAID = ? ;";
		
		try {
			PreparedStatement ps = wsConnection.prepareStatement(sqlSelect);
			ps.setInt(1, aaID);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				
				resultsBuffer.append("Belongs to User: ");
				resultsBuffer.append(rs.getString(1));
				resultsBuffer.append('\n');
				
				resultsBuffer.append("Represents the O/S Command: ");
				resultsBuffer.append(rs.getString(2));
				resultsBuffer.append('\n');
				
				resultsBuffer.append("Has the Unique Action Number: ");
				resultsBuffer.append(rs.getString(3));
				resultsBuffer.append('\n');
				
				resultsBuffer.append('\n');

				final String lastExecuted = rs.getString(4);
				if(lastExecuted == null) {
					
					resultsBuffer.append("Has Never Been Executed");
					
				} else {
					
					resultsBuffer.append("Was last Executed On: ");
					resultsBuffer.append(lastExecuted.substring(0, 23));
					
				}
				resultsBuffer.append('\n');
				
				final String lastSuccess = rs.getString(5);
				resultsBuffer.append("The last Execution was Successful: ");
				if(lastSuccess == null) {

					resultsBuffer.append("n/a");

				} else {
					
					resultsBuffer.append(lastSuccess);
					
				}
				resultsBuffer.append('\n');
				
				final String remoteLocation = rs.getString(6);
				resultsBuffer.append("It was Received from the Remote Location: ");
				if(remoteLocation == null) {
					
					resultsBuffer.append("n/a");
					
				} else {
					
					resultsBuffer.append(remoteLocation);
				}
				resultsBuffer.append('\n');

			} else {

				resultsBuffer.append("Cannot Be Found!");

			}
			
			resultsBuffer.append('\n');
			
			rs.close();
			ps.close();

		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);

		}

		return resultsBuffer.toString();
	}

	public synchronized String showUsers() {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append('\n');
		resultsBuffer.append("Users:");
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
		
		final String sqlPassUsers = "SELECT PPID, ACTIVE, FULLNAME, MODIFIED FROM PASSPHRASES JOIN USERS ON PASSPHRASES.PPID = USERS.PPID;";
		
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
			resultsBuffer.append('\n');
			
			rs.close();
			stmt.close();
	
		} catch (SQLException ex) {
			
			 throw new RuntimeException(ex);
	
		}
		
		return resultsBuffer.toString();
	}


}
