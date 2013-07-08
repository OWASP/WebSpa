package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WSServerCommand {

	private WSServer myServer;

	protected WSServerCommand(WSServer myServer) {

		this.myServer = myServer;

	}

	public void executeCommand(final String command) {

		if(command.equalsIgnoreCase("banner") || 
		   command.equalsIgnoreCase("b")) {
			
			WSServerCommand.showHelp("banner");
		}
		else 
		if(command.equalsIgnoreCase("config") || 
		   command.equalsIgnoreCase("c")) {
			
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();
			myServer.getMyConsole().writer().println("1. " + prop1);
			myServer.getMyConsole().writer().println("2. " + prop2);
		}
		else
		if(command.equalsIgnoreCase("help") ||
		   command.equalsIgnoreCase("h")) {
			
			myServer.getMyConsole().writer().println("Supported Commands:\n");
			WSServerCommand.showHelp("help");
			
		}
		else
		if(command.equalsIgnoreCase("start") ) {
			
			myServer.serverStart();
			
		}
		else
		if(command.equalsIgnoreCase("stop") ) {
					
			myServer.serverStop();
			
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

}
