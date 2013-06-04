package net.seleucus.wsp.server;

import net.seleucus.wsp.config.WSConfigLoader;
import net.seleucus.wsp.db.WSDatabaseManager;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

public class WSLogListener implements TailerListener {
	
	private final WSConfigLoader config; 
	private final WSDatabaseManager database;
	
	public WSLogListener(final WSConfigLoader config, final WSDatabaseManager database) {
		
		this.config = config;
		this.database = database;
		
	}
	
	@Override
	public void fileNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fileRotated() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(final String line) {

		// Check if the line length is more than 65535 chars
		
		// Check if the regex pattern has been found
		
		// Check if the request is 100 base64 encoded chars in length
		
		// Check if the request has been seen before in the database 
		
		// For all the passwords in the database, check if we have a valid incoming password
		
		// Having found a correct password, check if it is mapped against an action
		
		// Run the action, by executing the command found in the database
		
	}

	@Override
	public void handle(Exception arg0) {

	}

	@Override
	public void init(Tailer arg0) {
		// TODO Auto-generated method stub

	}

}
