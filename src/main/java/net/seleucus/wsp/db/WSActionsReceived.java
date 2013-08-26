package net.seleucus.wsp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class WSActionsReceived {

	private Connection wsConnection; 
	
	protected WSActionsReceived(Connection wsConnection) {
		
		this.wsConnection = wsConnection;
	}

	public synchronized void addAction(final String ipAddress, final String webSpaRequest, final int aaID) {

		final String sqlInsert = "INSERT INTO PUBLIC.ACTIONS_RECEIVED (IP_ADDR, RECEIVED, KNOCK, AAID) VALUES (?, CURRENT_TIMESTAMP, ?, ?); ";
		
		try {
			
			PreparedStatement preStatement = wsConnection.prepareStatement(sqlInsert);
			preStatement.setString(1, ipAddress);
			preStatement.setString(2, webSpaRequest);
			preStatement.setInt(3, aaID);
			
			preStatement.executeUpdate();
			preStatement.close();
			
		} catch (SQLException ex) {
			
			throw new RuntimeException(ex);
			
		}
	}

}
