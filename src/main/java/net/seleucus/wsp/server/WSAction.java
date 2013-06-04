package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WSAction implements Runnable {

	private StringBuilder stdOutBuilder, stdErrorBuilder;
	private boolean hasExecuted, wasSuccessful;
	private final String command;
	
	public WSAction(final String command) {
		
		this.command = command;
		
		stdOutBuilder = new StringBuilder();
		stdErrorBuilder = new StringBuilder();
		
		this.hasExecuted = false;
		this.wasSuccessful = false;
		
	}
	
	public String getCommand() {
		
		return command;
		
	}
	
	public boolean getHasExecuted() {
		
		return hasExecuted;
		
	}
	
	public boolean getWasSuccessful() {
		
		return wasSuccessful;
		
	}
	
	public String getStdOut() {
		
		return stdOutBuilder.toString();
	}
	
	public String getStdErr() {
		
		return stdErrorBuilder.toString();
		
	}
	
	protected boolean runCommand(final String command) {
		
		boolean success = false;
		
		try {
			Process myProcess = Runtime.getRuntime().exec(command);
			
			// Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));
			
            getInputStreamText(stdInput, stdOutBuilder);
            getInputStreamText(stdError, stdErrorBuilder);
    		
			success = true;
			
		} catch (IOException e) {
			
			success = false;
			
		}
		
		return success;
		
	}
	
	private void getInputStreamText(final BufferedReader inputReader, final StringBuilder textBuilder) throws IOException {
		
		int counter = 0;
		int value;
		
		while (((value = inputReader.read()) > 0) && (counter <= Character.MAX_VALUE)) {
			textBuilder.append((char) value);
			counter++;
			
		}
	}
	
	@Override
	public void run() {

		if(hasExecuted == false) {
		
			wasSuccessful = runCommand(command);
		}
		
		hasExecuted = true; 
		
	}

}
