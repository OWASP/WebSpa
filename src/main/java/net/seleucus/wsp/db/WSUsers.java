package net.seleucus.wsp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class WSUsers {

	private Connection wsConnection;

	protected WSUsers(Connection wsConnection) {
		
		this.wsConnection = wsConnection;
		
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
	
	public synchronized String getUsersFullName(int ppID) {
		
		String fullName = "Invalid User"; 
		
		if(ppID > 0) {
			
			String sqlActivationLookup = "SELECT FULLNAME FROM USERS WHERE PPID = ? ;";
			try {
				PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlActivationLookup);
				psPassPhrase.setInt(1, ppID);
				ResultSet rs = psPassPhrase.executeQuery();
				
				if (rs.next()) {
					fullName = rs.getString(1);
				}
				
				rs.close();
				psPassPhrase.close();
				
			} catch (SQLException ex) {
	
				throw new RuntimeException(ex);
	
			}
		}
		
		return fullName;
	}

	public synchronized String showUsers() {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append('\n');
		resultsBuffer.append("Users:");
		resultsBuffer.append('\n');
		resultsBuffer.append("___________________________________________________________");
		resultsBuffer.append('\n');
		resultsBuffer.append(StringUtils.rightPad("ID", 4));
		resultsBuffer.append(StringUtils.rightPad("Active", 8));
		resultsBuffer.append(StringUtils.rightPad("Full Name", 24));
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
				resultsBuffer.append(StringUtils.rightPad(rs.getString(2), 8));
				resultsBuffer.append(StringUtils.rightPad(StringUtils.abbreviate(rs.getString(3), 23), 24));
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
