package net.seleucus.wsp.db;

import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.seleucus.wsp.crypto.WebSpaEncoder;

public class WSPassPhrases {

	private Connection wsConnection;
	
	protected WSPassPhrases(Connection wsConnection) {
		
		this.wsConnection = wsConnection;
		
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
			
		} // ppID > 0
		
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
			
		} // ppID > 0
		
		return success;
		
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
	
	public synchronized CharSequence getPassPhrase(final int ppID) {
		
		CharSequence rawPassPhrase = CharBuffer.wrap("<could-not-find-pass-phrase>".toCharArray());
		
		if(ppID > 0) {
			
			final String sqlPassPhraseLookup = "SELECT PASSPHRASE FROM PASSPHRASES WHERE PPID = ? ;";
			
			PreparedStatement psPassPhrase;
			try {
				psPassPhrase = wsConnection.prepareStatement(sqlPassPhraseLookup);
				psPassPhrase.setInt(1, ppID);
				ResultSet rs = psPassPhrase.executeQuery();
				
				if (rs.next()) {
					char[] dbPassPhraseArray = rs.getString(1).toCharArray();
					rawPassPhrase = CharBuffer.wrap(dbPassPhraseArray);
				}
				
				rs.close();
				psPassPhrase.close();
				
			} catch (SQLException ex) {
				
				throw new RuntimeException(ex);
				
			}
			
		} // ppID > 0
		
		return rawPassPhrase;
		
	}
	
	public synchronized String getLastModifiedDate(final int ppID) {
		
		String lastModified = "0000-00-00 00:00:00.000";
		
		if(ppID > 0) {
			
			final String sqlModifiedLookup = "SELECT MODIFIED FROM PASSPHRASES WHERE PPID = ? ;";
			
			PreparedStatement psModified;
			try {
				psModified = wsConnection.prepareStatement(sqlModifiedLookup);
				psModified.setInt(1, ppID);
				ResultSet rs = psModified.executeQuery();
				
				if (rs.next()) {
					lastModified = rs.getString(1).substring(0, 23);
				}
				
				rs.close();
				psModified.close();
				
			} catch (SQLException ex) {
				
				throw new RuntimeException(ex);
				
			}
			
		} // ppID > 0
		
		return lastModified;
	}

	public synchronized boolean updatePassPhrase(final int ppID, final CharSequence passSeq) {
		
		boolean success = false;
		
		if(ppID > 0) {
			
			final String passPhraseUpdate = "UPDATE PASSPHRASES SET PASSPHRASE = ? , MODIFIED = CURRENT_TIMESTAMP WHERE PPID = ? ;";
			
			try {
				PreparedStatement ps = wsConnection.prepareStatement(passPhraseUpdate);
				ps.setString(1, passSeq.toString());
				ps.setInt(2, ppID);
				
				int rowCount = ps.executeUpdate();
				
				if(rowCount == 1) {
					success = true;
				}
				
			} catch (SQLException ex) {

				throw new RuntimeException(ex);
				
			}
			
		}
		
		return success; 
		
	}

}
