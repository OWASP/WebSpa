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
	
	private boolean serviceStarted;
	
	private WSLogListener myLogListener;
	private WSConfiguration myConfiguration;
	private WSServerCommand myServerCommand;
	
	public WSServer(WebSpa myWebSpa) throws Exception {

		super(myWebSpa);
		
		myLogTailer = null;

		serviceStarted = false;
		
		myDatabase = new WSDatabase();
		myConfiguration = new WSConfiguration();
		
		myLogListener = new WSLogListener(myDatabase, myConfiguration);
		myServerCommand = new WSServerCommand(this);

	}
	
	public void serverStart() {
		
		File accessLog = new File(myConfiguration.getAccesLogFileLocation());
		if(accessLog.exists()) {
			
			getWSConsole().writer().println("Access log file found at: " + accessLog.getPath());
			
			serviceStarted = true; 
			
			if(myLogTailer == null) {
				
				myLogTailer = Tailer.create(accessLog, myLogListener, 10000, true);
				
			} else {
				
				// myLogTailer.run();
			}
			
		} else {
			
			getWSConsole().writer().println("Access log file NOT found at: " + accessLog.getPath());
			getWSConsole().writer().println("Web-Spa Server Not Started");
		}
		
	}
	
	public String serverStatus() {
		
		if(serviceStarted) {
			
			return "\nWeb-Spa is Running";
			
		} else {
			
			return "\nWeb-Spa is Stopped";
			
		}
	}
	
	public void serverStop() {
		
		if (myLogTailer == null) {
			
			getWSConsole().writer().println("Web-Spa Server Had Not Started");
			
		} else {
			
			getWSConsole().writer().println("Web-Spa Server Stopped");
			myLogTailer.stop();
			
			serviceStarted = false;
		}

	}

	@Override
	public void exitConsole() {
		getWSConsole().writer().println("\nGoodbye!\n");
	}

	@Override
	public void runConsole() throws SQLException {
		
		getWSConsole().writer().println("");
		getWSConsole().writer().println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		getWSConsole().writer().println("");
		
		getWSConsole().writer().println("This is a holding prompt, type \"exit\" to quit");        
				
		do {

			String command = getWSConsole().readLine("\nweb-spa-server>");
			
			if( "exit".equalsIgnoreCase(command) ||
				"quit".equalsIgnoreCase(command) ||
				"laterz".equalsIgnoreCase(command) ||
				"bye".equalsIgnoreCase(command) ) {
				
				this.shutdown();
				
				break;
				
			} else {
				
				this.processCommand(command);
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
		
		myServerCommand.executeCommand(command.trim());
		
	}

}
