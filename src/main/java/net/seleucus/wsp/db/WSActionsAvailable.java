package net.seleucus.wsp.db;

import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

import net.seleucus.wsp.crypto.WebSpaEncoder;

public class WSActionsAvailable {

	private Connection wsConnection;
	
	protected WSActionsAvailable(Connection wsConnection) {
		
		this.wsConnection = wsConnection;
		
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

	public String getOSCommand(final int ppID, final int actionNumber) {
		
		String command = "";
		
		final String sqlSelect = "SELECT COMMAND FROM ACTIONS_AVAILABLE WHERE PPID = ? AND ACTION_NUMBER = ? ;";
		
		PreparedStatement psPassPhrase;
		try {
			psPassPhrase = wsConnection.prepareStatement(sqlSelect);
			psPassPhrase.setInt(1, ppID);
			psPassPhrase.setInt(2, actionNumber);
			ResultSet rs = psPassPhrase.executeQuery();
			
			if (rs.next()) {
				
				command = rs.getString(1);
				
			} 
			
			rs.close();
			psPassPhrase.close();
			
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
			
		}
		
		return command;
	}
	
	public int getAAID(final int ppID, final int actionNumber) {
		
		int output = -1;
		
		final String sqlSelect = "SELECT AAID FROM ACTIONS_AVAILABLE WHERE PPID = ? AND ACTION_NUMBER = ? ;";
		
		PreparedStatement psStatement;
		try {
			psStatement = wsConnection.prepareStatement(sqlSelect);
			psStatement.setInt(1, ppID);
			psStatement.setInt(2, actionNumber);
			ResultSet rs = psStatement.executeQuery();
			
			if (rs.next()) {
				
				output = rs.getInt(1);
				
			}
			
			rs.close();
			psStatement.close();
			
		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
		
		return output;
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

	private synchronized boolean isPPIDInUse(int ppID) {
	
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

	public synchronized String showActionDetails(final int ppID, final int actionNumber) {
		
		StringBuffer resultsBuffer = new StringBuffer();
		
		resultsBuffer.append('\n');
		resultsBuffer.append("Action with Number: ");
		resultsBuffer.append(actionNumber);
		resultsBuffer.append('\n');
		
		final String sqlSelect = "SELECT COMMAND, LAST_EXECUTED, LAST_RUN_SUCCESS, IP_ADDR FROM ACTIONS_AVAILABLE WHERE PPID = ? AND ACTION_NUMBER = ? ;";
		
		try {
			PreparedStatement ps = wsConnection.prepareStatement(sqlSelect);
			ps.setInt(1, ppID);
			ps.setInt(2, actionNumber);
			ResultSet rs = ps.executeQuery();
	
			if (rs.next()) {
				
				resultsBuffer.append("Belongs to User: ");
				resultsBuffer.append(ppID);
				resultsBuffer.append('\n');
				
				resultsBuffer.append("Represents the O/S Command: ");
				resultsBuffer.append(rs.getString(1));
				resultsBuffer.append('\n');				
				resultsBuffer.append('\n');
	
				final String lastExecuted = rs.getString(2);
				if(lastExecuted == null) {
					
					resultsBuffer.append("Has Never Been Executed");
					
				} else {
					
					resultsBuffer.append("Was last Executed On: ");
					resultsBuffer.append(lastExecuted.substring(0, 23));
					
				}
				resultsBuffer.append('\n');
				
				final String lastSuccess = rs.getString(3);
				resultsBuffer.append("The last Execution was Successful: ");
				if(lastSuccess == null) {
	
					resultsBuffer.append("n/a");
	
				} else {
					
					resultsBuffer.append(lastSuccess);
					
				}
				resultsBuffer.append('\n');
				
				final String remoteLocation = rs.getString(4);
				resultsBuffer.append("It was Received from the Remote Location: ");
				if(remoteLocation == null) {
					
					resultsBuffer.append("n/a");
					
				} else {
					
					resultsBuffer.append(remoteLocation);
				}
				// resultsBuffer.append('\n');
	
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

	public synchronized String showActions(final int ppID) {
		
		StringBuffer resultsBuffer = new StringBuffer();
		resultsBuffer.append('\n');
		resultsBuffer.append("Actions for user with ID: ");
		resultsBuffer.append(ppID);
		resultsBuffer.append('\n');
		resultsBuffer.append("___________________________________________________________");
		resultsBuffer.append('\n');
		// resultsBuffer.append(StringUtils.rightPad("ID", 4));
		resultsBuffer.append(" #  ");
		resultsBuffer.append(StringUtils.rightPad("O/S Command", 32));
		resultsBuffer.append(StringUtils.rightPad("Last Executed", 25));
		resultsBuffer.append('\n');
		resultsBuffer.append("-----------------------------------------------------------");
		resultsBuffer.append('\n');
		final String sqlSelect = "SELECT ACTION_NUMBER, COMMAND, LAST_EXECUTED FROM ACTIONS_AVAILABLE WHERE PPID = ? ;";
		try {
			PreparedStatement ps = wsConnection.prepareStatement(sqlSelect);
			ps.setInt(1, ppID);
			ResultSet rs = ps.executeQuery();
	
			while (rs.next()) {
				resultsBuffer.append(' ');
				resultsBuffer.append(StringUtils.rightPad(rs.getString(1), 3));
				resultsBuffer.append(StringUtils.rightPad(StringUtils.abbreviate(rs.getString(2), 30), 32));
				final String lastExecuted = rs.getString(3);
				
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

	public synchronized void updateAction(final int ppID, final int action, 
										  final boolean success, final String ipAddress) {

		final String sqlUpdate = "UPDATE PUBLIC.ACTIONS_AVAILABLE SET LAST_EXECUTED = CURRENT_TIMESTAMP, LAST_RUN_SUCCESS = ?, IP_ADDR = ? WHERE PPID = ? AND ACTION_NUMBER = ? ;";
		
		try {
				
		    	PreparedStatement psPassPhrase = wsConnection.prepareStatement(sqlUpdate);
		    	psPassPhrase.setBoolean(1, success);
		    	psPassPhrase.setString(2, ipAddress);
		    	psPassPhrase.setInt(3, ppID);
		    	psPassPhrase.setInt(4, action);
		    	
		    	psPassPhrase.executeUpdate();
		    	psPassPhrase.close();
		    	
		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
				 
		}
		
	}

}
