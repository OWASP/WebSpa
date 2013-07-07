package net.seleucus.wsp.server;

import java.io.File;
import java.sql.SQLException;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.db.WSDatabase;
import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;

import org.apache.commons.io.input.Tailer;

public class WSServer extends WSGestalt {

	private Tailer myLogTailer;
	private WSDatabase myDatabase;
	private WSLogListener myLogListener;
	private WSConfiguration myConfiguration;

	public WSServer(WebSpa myWebSpa) throws Exception {

		super(myWebSpa);
		
		myLogTailer = null;

		myDatabase = new WSDatabase();
		myConfiguration = new WSConfiguration();
		
		myLogListener = new WSLogListener(myDatabase, myConfiguration);

	}
	
	public void serverStart() {
		
		File accessLog = new File(myConfiguration.getAccesLogFileLocation());
		
		if(myLogTailer == null) {
			myLogTailer = Tailer.create(accessLog, myLogListener, 10000, true);
		} else {
			// logTailer.run();
		}
	}
	
	public void serverStop() {
		
		if (myLogTailer != null) {
			myLogTailer.stop();
		}

	}

	@Override
	public void exitConsole() {
		getMyConsole().writer().println("\nGoodbye!\n");
	}

	@Override
	public void runConsole() {
		
		getMyConsole().writer().println("");
		getMyConsole().writer().println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		getMyConsole().writer().println("");
		
		getMyConsole().writer().println("This is a holding prompt, type \"exit\" to quit");        
				
		do {

			String command = getMyConsole().readLine("\nweb-spa-server>");
			
			if( "exit".equalsIgnoreCase(command) ||
				"quit".equalsIgnoreCase(command) ||
				"laterz".equalsIgnoreCase(command) ||
				"bye".equalsIgnoreCase(command) ) {
				
				// myServer.shutdown();
				
				break;
				
			} else {
				
				// myServer.processCommand(command);
			}

		} while (true);
		
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
		// myCommand.executeCommand(command);
	}

}
