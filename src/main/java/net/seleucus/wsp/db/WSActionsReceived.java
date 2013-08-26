package net.seleucus.wsp.db;

import java.sql.Connection;



public class WSActionsReceived {

	private Connection wsConnection; 
	
	protected WSActionsReceived(Connection wsConnection) {
		
		this.wsConnection = wsConnection;
	}

}
