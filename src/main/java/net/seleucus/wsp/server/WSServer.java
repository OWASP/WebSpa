package net.seleucus.wsp.server;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.db.WSDatabase;
import net.seleucus.wsp.main.WSGestalt;
import net.seleucus.wsp.main.WSVersion;
import net.seleucus.wsp.main.WebSpa;

import org.apache.commons.io.input.Tailer;

public class WSServer extends WSGestalt {

	private Tailer myLogTailer;
	private ExecutorService myExecService;
	
	private boolean serviceStarted;
	
	private WSDatabase myDatabase;
	private WSLogListener myLogListener;
	private WSConfiguration myConfiguration;
	private WSServerCommand myServerCommand;
	
	public WSServer(WebSpa myWebSpa) throws Exception {

		super(myWebSpa);
		
		myLogTailer = null;
		myExecService = Executors.newSingleThreadExecutor();
		
		serviceStarted = false;
		
		myDatabase = new WSDatabase();
		myConfiguration = new WSConfiguration();
		
		myLogListener = new WSLogListener(this);
		myServerCommand = new WSServerCommand(this);

	}
	
	public void serverStart() {
		
		File accessLog = new File(myConfiguration.getAccesLogFileLocation());
		if(accessLog.exists()) {
			
			println("Access log file found at: " + accessLog.getPath());
			
			serviceStarted = true; 
			
			if(myLogTailer == null) {
				
				myLogTailer = Tailer.create(accessLog, myLogListener, 10000, true);
				
			} else {
				
				// myLogTailer.run();
			}
			
		} else {
			
			println("Access log file NOT found at: " + accessLog.getPath());
			println("Web-Spa Server Not Started");
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
			
			println("Web-Spa Server Had Not Started");
			
		} else {
			
			println("Web-Spa Server Stopped");
			myLogTailer.stop();
			
			serviceStarted = false;
		}

	}

	@Override
	public void exitConsole() {
		println("\nGoodbye!\n");
	}
	
	public void runOSCommand(final int ppID, final int actionNumber, final String ipAddress) {
		
		final WSAction action = new WSAction(this, ppID, actionNumber, ipAddress);
		Future<Boolean> task = myExecService.submit(action);
		
        System.out.println("O/S Command: " + action.getCommand());
        System.out.println("Has Executed: " + action.getHasExecuted());
        System.out.println("Was Successfull: " + action.getWasSuccessful());
        
        System.out.println("STD Output: " + action.getStdOut());
        System.out.println("STD Error: " + action.getStdErr());

	}

	@Override
	public void runConsole() throws SQLException {
		
		println("");
		println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		println("");
		
		println("This is a holding prompt, type \"exit\" to quit");        
		// TODO Add a boolean which gets set to true to exit from this loop		
		do {

			String command = readLineMainPrompt();
			
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
