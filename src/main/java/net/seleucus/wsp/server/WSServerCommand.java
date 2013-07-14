package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

public class WSServerCommand {

	private WSServer myServer;

	protected WSServerCommand(WSServer myServer) {

		this.myServer = myServer;

	}

	public void executeCommand(final String command) {

		if(command.equalsIgnoreCase("adduser")) {
			
			String fullName = myServer.getWSConsole().readLine("Enter the New User's Full Name: ");
			boolean passDoesNotExist = true;
			CharSequence passSeq;
			do {
				
				char[] passArray = myServer.getWSConsole().readPassword("Enter the New User's Pass-Phrase: ");
				passSeq = CharBuffer.wrap(passArray);
				
				passDoesNotExist = myServer.getWSDatabase().isPasswordInUse(passSeq);
				if(passDoesNotExist == true) {
					myServer.getWSConsole().writer().println("This Pass-Phrase is already taken and in use by another user");
				}
				
			} while(passDoesNotExist);
			
			myServer.getWSConsole().writer().println("\nThe following information is optional");
			
			String eMail = myServer.getWSConsole().readLine("\tPlease enter the New User's Email Address: ");
			String phone = myServer.getWSConsole().readLine("\tPlease enter the New User's Phone Number: ");
					
			myServer.getWSDatabase().addUser(fullName, passSeq, eMail, phone);
		}
		else
		if(command.equalsIgnoreCase("banner") || 
		   command.equalsIgnoreCase("b")) {
			
			WSServerCommand.showHelp("banner");
		}
		else 
		if(command.equalsIgnoreCase("config") || 
		   command.equalsIgnoreCase("c")) {
			
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();
			myServer.getWSConsole().writer().println("1. " + prop1);
			myServer.getWSConsole().writer().println("2. " + prop2);
		}
		else
		if(command.equalsIgnoreCase("help") ||
		   command.equalsIgnoreCase("h")) {
			
			myServer.getWSConsole().writer().println("Supported Commands:\n");
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
		else
		if(command.equalsIgnoreCase("users")) {
			
			myServer.getWSConsole().writer().println("\nWeb-Spa Users:");
			myServer.getWSConsole().writer().println("__________________________________________________");
			String users = myServer.getWSDatabase().showUsers();
			
			myServer.getWSConsole().writer().println(users);
			
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
