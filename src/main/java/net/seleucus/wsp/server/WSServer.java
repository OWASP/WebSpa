package net.seleucus.wsp.server;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private WSServerConsole myServerCommand;
	
	public WSServer(WebSpa myWebSpa) throws Exception {

		super(myWebSpa);
		
		myLogTailer = null;
		myExecService = Executors.newSingleThreadExecutor();
		
		serviceStarted = false;
		
		myDatabase = new WSDatabase();
		myConfiguration = new WSConfiguration();
		
		myLogListener = new WSLogListener(this);
		myServerCommand = new WSServerConsole(this);

	}
	
	public void serverStart() {
		
		if(serviceStarted == true) {
			
			printlnWithTimeStamp("Service is already running");
			
		} else {
			
			printlnWithTimeStamp("Attempting to start WebSpa...");
			File accessLog = new File(myConfiguration.getAccesLogFileLocation());

			if(accessLog.exists()) {

				printlnWithTimeStamp("Found access log file: " + accessLog.getPath());			
				serviceStarted = true; 

				if(myLogTailer == null) {
					
					printlnWithTimeStamp("Creating tail listener...");
					myLogTailer = Tailer.create(accessLog, myLogListener, 10000, true);

				} /* else {

					printlnWithTimeStamp("Re-starting listener...");
					myLogTailer.run();
					
				} */ 

				printlnWithTimeStamp("WebSpa server started!");
				printlnWithTimeStamp("Please make sure your web server is also up");

			} else {

				printlnWithTimeStamp("Access log file NOT found at: " + accessLog.getPath());
				printlnWithTimeStamp("WebSpa Server Not Started\n");

			}
		}

	}
	
	public void serverStatus() {
		
		if(serviceStarted) {
			
			printlnWithTimeStamp("WebSpa is Running!");
			
		} else {
			
			printlnWithTimeStamp("WebSpa is Stopped.");
			
		}
	}
	
	public void serverStop() {
		
		if (myLogTailer == null) {
			
			printlnWithTimeStamp("WebSpa Server Had Not Started");
			
		} else {
			
			printlnWithTimeStamp("WebSpa Server Stopped");
			myLogTailer.stop();

			myLogTailer = null; 
			serviceStarted = false;
			
		}
		
	}

	@Override
	public void exitConsole() {
		printlnWithTimeStamp("Goodbye!\n");
	}
	
	public void runOSCommand(final int ppID, final int actionNumber, final String ipAddress) {
		
		final WSAction action = new WSAction(this, ppID, actionNumber, ipAddress);
		// Future<Boolean> task = myExecService.submit(action);
		myExecService.submit(action);
		
	}

	@Override
	public void runConsole() throws SQLException {
		
		println("");
		println("WebSpa - Single HTTP/S Request Authorisation");
		println("version " + WSVersion.getValue() + " (WebSpa@seleucus.net)"); 		
		println("");
		println("This is a holding prompt, type \"exit\" or \"x\" to quit");
		println("");
		println("- type \"service start\" to start the WebSpa server");
		println("- type \"help\" or \"?\" for more options");
		println("");
		
		do {

			String command = readLineServerPrompt();
			
			if( "laterz".equalsIgnoreCase(command) ||
				"exit".equalsIgnoreCase(command) ||
				"quit".equalsIgnoreCase(command) ||
				"bye".equalsIgnoreCase(command) ||
				"x".equalsIgnoreCase(command)) {
				
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
		
		if(serviceStarted == true) {
			
			this.serverStop();
			
		}
		
		myDatabase.shutdown();	
	}

	public void processCommand(final String command) {
		
		myServerCommand.executeCommand(command.trim());
		
	}

}
