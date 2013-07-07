package net.seleucus.wsp.server;

import java.io.IOException;
import java.sql.SQLException;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.db.WSDatabase;

public class WSServer {

	// private final Console myConsole; 
	
	private WSCommand myCommand;
	private WSDatabase myDatabase;
	private WSConfiguration myConfiguration;

	public WSServer() throws ClassNotFoundException, SQLException, IOException {
		
		// this.myConsole = myConsole;
		
		myDatabase = new WSDatabase();
		myConfiguration = new WSConfiguration();
		//
		myCommand = new WSCommand(this);
	}
	
	public WSConfiguration getWSConfiguration() {
		return myConfiguration;
	}
	
	public WSDatabase getWSDatabase() {
		return myDatabase;
	}

	public void shutdown() throws SQLException {	
		myDatabase.shutdown();	
	}

	public void processCommand(final String command) {

		myCommand.executeCommand(command);

	}

}
