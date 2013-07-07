package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WSCommand {

	private final static String[] COMMANDS = { "help",
			"show config access-log-file-location",
			"show config logging-regex", "start", "stop", "show banner"};

	private WSServer myServer;

	public WSCommand(WSServer myServer) {

		this.myServer = myServer;

	}

	public void executeCommand(final String command) {

		if(command.equalsIgnoreCase("show banner") ||
		   command.equalsIgnoreCase("banner") || 
		   command.equalsIgnoreCase("b")) {
			
			WSCommand.showHelp("banner");
		}
		else 
		if(command.equalsIgnoreCase("show config") ||
		   command.equalsIgnoreCase("config") || 
		   command.equalsIgnoreCase("c")) {
			
			WSCommand.showHelp("config");
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();
			System.out.println("1. " + prop1);
			System.out.println("2. " + prop2);
		}
		else
		if(command.equalsIgnoreCase("show help") ||
		   command.equalsIgnoreCase("help") ||
		   command.equalsIgnoreCase("h")) {
			
			WSCommand.showHelp("help");
		}
		else
		if(command.equalsIgnoreCase("show begin") || 
		   command.equalsIgnoreCase("begin") ||  
		   command.equalsIgnoreCase("b") ) {
			
			// myServer.start();
		}
		else
		if(command.equalsIgnoreCase("show stop") || 
				   command.equalsIgnoreCase("stop") ||  
				   command.equalsIgnoreCase("s") ) {
					
			// myServer.stop();
		}
		else {
			System.out.println("Unknown command - type \"help\" for more options");
		}
	}

	private static void showHelp(final String topic) {

		InputStream fstream = ClassLoader.getSystemResourceAsStream("help/" + topic);
		DataInputStream in = new DataInputStream(fstream);

		BufferedReader br;
		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void showConfig() {

		InputStream fstream = ClassLoader.getSystemResourceAsStream("help/config");
		DataInputStream in = new DataInputStream(fstream);

		BufferedReader br;
		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
