package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.Arrays;

import net.seleucus.wsp.util.WSConstants;

public class WSServerCommand {

	private WSServer myServer;

	protected WSServerCommand(WSServer myServer) {

		this.myServer = myServer;

	}

	public void executeCommand(final String command) {

		String[] params = command.split(" ");

		if(command.equalsIgnoreCase("config show") || 
		   command.equalsIgnoreCase("c")) {
			
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();

			myServer.getWSConsole().writer().println("\n1. " + prop1);
			myServer.getWSConsole().writer().println("\n2. " + prop2);
			
			myServer.getWSConsole().writer().println("\nType \"help config\" for further information");
		}
		else
		if(command.startsWith("help")) {
			
			if(params.length > 1) {

				if(WSConstants.SERVER_COMMANDS.contains(params[1])) {
					
					WSServerCommand.showHelp(params[1]);
					
				} else {
					
					WSServerCommand.showHelp("default");
					
				}
				
			} else {
				
				WSServerCommand.showHelp("default");
				myServer.getWSConsole().writer().println("This is a holding prompt, type \"exit\" to quit");        

			}
			
		} // help end
		else
		if(command.startsWith("service") ) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("start")) {
					
					myServer.serverStart();
					
				}
				else
				if(params[1].equalsIgnoreCase("status")) {
					
					String status = myServer.serverStatus();
					myServer.getWSConsole().writer().println(status);
					
				}
				else
				if(params[1].equalsIgnoreCase("stop")) {
					
					myServer.serverStop();
					
				} else {
					
					myServer.getWSConsole().writer().println("\nInvalid Option. Type \"help service\" for further information");

				}
			} else {
				
				myServer.getWSConsole().writer().println("\nNo Option Specified. Type \"help service\" for further information");
				
			}
		} // service end
		else
		if(command.startsWith("user")) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("activate")) {
					
					userActivate();
				}
				else
				if(params[1].equalsIgnoreCase("add")) {
					
					userAdd();
					
				}
				else
				if(params[1].equalsIgnoreCase("delete")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("modify")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("show")) {
					
					userShow();
					
				} else {
					
					myServer.getWSConsole().writer().println("\nInvalid Option. Type \"help user\" for further information");
					
				}
				
			} else {
				
				myServer.getWSConsole().writer().println("\nNo Option Specified. Type \"help user\" for further information");
			}
		}
		else {
			
			System.out.println("\nUnknown command - type \"help\" for more options");
			
		}
	}

	private void userShow() {

		final String users = myServer.getWSDatabase().showUsers();
		myServer.getWSConsole().writer().println(users);
		
	}

	private void userAdd() {
		
		boolean fullNameBlank = true;
		String fullNameTrimmed;
		
		do {
			String fullName = myServer.getWSConsole().readLine("Enter the New User's Full Name: ");
			fullNameTrimmed = fullName.trim();
			
			fullNameBlank = fullNameTrimmed.isEmpty();
			if(fullNameBlank == true) {
				
				myServer.getWSConsole().writer().println("The Full Name Cannot be Blank");
				
			}
			
		} while (fullNameBlank);
		
		
		boolean passPhraseOneEqualsTwo = true;
		boolean passPhraseInUse = true;

		CharSequence passSeq;
		
		do {
			
			char[] passArray = myServer.getWSConsole().readPassword("Enter the New User's Pass-Phrase: ");
			
			while(passArray.length == 0) {
				
				myServer.getWSConsole().writer().println("Pass-phrase cannot be blank");
				passArray = myServer.getWSConsole().readPassword("Enter the New User's Pass-Phrase: ");
				
			}
			
			char[] passArrayTwo = myServer.getWSConsole().readPassword("Re-enter the New User's Pass-Phrase");
			
			passSeq = CharBuffer.wrap(passArray);
			
			passPhraseInUse = myServer.getWSDatabase().isPassPhraseInUse(passSeq);
			passPhraseOneEqualsTwo = Arrays.equals(passArray, passArrayTwo);
			
			if(passPhraseInUse == true) {
				myServer.getWSConsole().writer().println("This Pass-Phrase is already taken and in use by another user");
				myServer.getWSConsole().writer().println("Web-Spa pass-phrases have to be unique for each user");
			}
			
			if(passPhraseOneEqualsTwo == false) {
				myServer.getWSConsole().writer().println("Pass-phrases entered do not match");
				myServer.getWSConsole().writer().println("Please try again");
			}
			
		} while(passPhraseInUse || (!passPhraseOneEqualsTwo));
		
		myServer.getWSConsole().writer().println("\nThe following information is optional");
		
		String eMail = myServer.getWSConsole().readLine("\tPlease enter the New User's Email Address: ");
		String phone = myServer.getWSConsole().readLine("\tPlease enter the New User's Phone Number: ");
				
		myServer.getWSDatabase().addUser(fullNameTrimmed, passSeq, eMail, phone);
	}
	
	private void userActivate() {
		
		userShow();
		int ppID = -1;
		boolean userIDFound = false;
		
		String idString = myServer.getWSConsole().readLine("\nSelect a User ID: ");
		try {
			
			ppID = Integer.parseInt(idString);
			
		} catch(NumberFormatException ex) {
			
			myServer.getWSConsole().writer().println("Invalid Number");
			
		}
		
		userIDFound = myServer.getWSDatabase().isPPIDInUse(ppID);
		
		if(userIDFound == false) {
			
			myServer.getWSConsole().writer().println("User ID Not Found");

		} else {
			
			final String oldPPIDStatus = myServer.getWSDatabase().getActivationStatusString(ppID);
			myServer.getWSConsole().writer().println(oldPPIDStatus);
			
			// Toggle user
			final String choice = myServer.getWSConsole().readLine("Toggle user activation [Y/n]? ");
			
			if("yes".equalsIgnoreCase(choice) ||
				"y".equalsIgnoreCase(choice) ||
				choice.isEmpty() ) {
				
				myServer.getWSDatabase().toggleUserActivation(ppID);
				
			}
			
			final String newPPIDStatus = myServer.getWSDatabase().getActivationStatusString(ppID);
			myServer.getWSConsole().writer().println(newPPIDStatus);
		
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
			
			throw new RuntimeException(e);
			
		}
		
	}

}
