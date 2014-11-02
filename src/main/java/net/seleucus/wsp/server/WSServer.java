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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSServer extends WSGestalt {

	private final static Logger LOGGER = LoggerFactory.getLogger(WSServer.class);    

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
			
			LOGGER.info("Service is already running");
			
		} else {
			
			LOGGER.info("Attempting to start WebSpa...");
			File accessLog = new File(myConfiguration.getAccesLogFileLocation());

			if(accessLog.exists()) {

				LOGGER.info("Found access log file: " + accessLog.getPath());			
				serviceStarted = true; 

				if(myLogTailer == null) {
					
					LOGGER.info("Creating tail listener...");
					myLogTailer = Tailer.create(accessLog, myLogListener, 10000, true);

				} /* else {

					printlnWithTimeStamp("Re-starting listener...");
					myLogTailer.run();
					
				} */ 

				LOGGER.info("WebSpa server started!");
				LOGGER.info("Please make sure your web server is also up");

			} else {

				LOGGER.error("Access log file NOT found at: " + accessLog.getPath());
				LOGGER.error("WebSpa Server Not Started");

			}
		}

	}
	
	public void serverStatus() {
		
		if(serviceStarted) {
			
			LOGGER.info("WebSpa is Running!");
			
		} else {
			
			LOGGER.info("WebSpa is Stopped.");
			
		}
	}
	
	public void serverStop() {
		
		if (myLogTailer == null) {
			
			LOGGER.info("WebSpa Server Had Not Started");
			
		} else {
			
			LOGGER.info("WebSpa Server Stopped");
			myLogTailer.stop();

			myLogTailer = null; 
			serviceStarted = false;
			
		}
		
	}

	@Override
	public void exitConsole() {
		
		LOGGER.info("Goodbye!");
		
	}
	
	public void runOSCommand(final int ppID, final int actionNumber, final String ipAddress) {
		
		final WSAction action = new WSAction(this, ppID, actionNumber, ipAddress);
		// Future<Boolean> task = myExecService.submit(action);
		myExecService.submit(action);
		
	}

	@Override
	public void runConsole() throws SQLException {
		
		LOGGER.info("");
		LOGGER.info("WebSpa - Single HTTP/S Request Authorisation");
		LOGGER.info("version " + WSVersion.getValue() + " (WebSpa@seleucus.net)"); 		
		LOGGER.info("");
		LOGGER.info("This is a holding prompt, type \"exit\" or \"x\" to quit");
		LOGGER.info("");
		LOGGER.info("Type \"service start\" to start the WebSpa server");
		LOGGER.info("Type \"help\" or \"?\" for more options");
		LOGGER.info("");
		
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

	public void println(final String msg) {
		myConsole.println(msg);
	}

}
