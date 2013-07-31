package net.seleucus.wsp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.seleucus.wsp.util.WSConstants;

public class WSServerCommand {

	private WSServer myServer;

	protected WSServerCommand(WSServer myServer) {

		this.myServer = myServer;

	}

	public void executeCommand(final String command) {

		String[] params = command.split(" ");

		if(command.startsWith("action") ) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("create")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("delete")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("modify")) {				
					
				} 
				else
				if(params[1].equalsIgnoreCase("show")) {
					
					actionShow();
					
				} else {
					
					myServer.println("\nInvalid Option. Type \"help service\" for further information");

				}
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help service\" for further information");
				
			}
		} // action end
		else
		if(command.equalsIgnoreCase("config show") || 
		   command.equalsIgnoreCase("c")) {
			
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();

			myServer.println("\n1. " + prop1);
			myServer.println("\n2. " + prop2);
			
			myServer.println("\nType \"help config\" for further information");
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
				myServer.println("This is a holding prompt, type \"exit\" to quit");        

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
					myServer.println(status);
					
				}
				else
				if(params[1].equalsIgnoreCase("stop")) {
					
					myServer.serverStop();
					
				} else {
					
					myServer.println("\nInvalid Option. Type \"help service\" for further information");

				}
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help service\" for further information");
				
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
					
					myServer.println("\nInvalid Option. Type \"help user\" for further information");
					
				}
				
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help user\" for further information");
			}
		} // user end
		else {
			
			System.out.println("\nUnknown command - type \"help\" for more options");
			
		}
	}
	
	private void actionShow() {
		
		userShow();
		final int ppID = myServer.readLineOptionalInt("Select a User ID: ");
		final boolean userIDFound = myServer.getWSDatabase().isPPIDInUse(ppID);
		
		if(userIDFound == false) {
			
			myServer.println("User ID Not Found");

		} else {
			
			final String actions = myServer.getWSDatabase().showActions(ppID);
			myServer.println(actions);
			
			final int aaID = myServer.readLineOptionalInt("Select an Action ID: ");
			final String actionDetails = myServer.getWSDatabase().showActionDetails(aaID);
			myServer.println(actionDetails);
			
		}
		
	}

	private void userShow() {

		final String users = myServer.getWSDatabase().showUsers();
		myServer.println(users);
		
	}

	private void userAdd() {
		
		String fullName = myServer.readLineRequired("Enter the New User's Full Name: ");
		
		boolean passPhraseInUse = true;
		CharSequence passSeq;
		
		do {
			
			passSeq = myServer.readPasswordRequired("Enter the New User's Pass-Phrase: ");
						
			passPhraseInUse = myServer.getWSDatabase().isPassPhraseInUse(passSeq);
			
			if(passPhraseInUse == true) {
				myServer.println("This Pass-Phrase is already taken and in use by another user");
				myServer.println("Web-Spa pass-phrases have to be unique for each user");
			}
			
			
		} while(passPhraseInUse);
				
		String eMail = myServer.readLineOptional("Please enter the New User's Email Address: ");
		String phone = myServer.readLineOptional("Please enter the New User's Phone Number: ");
				
		myServer.getWSDatabase().addUser(fullName, passSeq, eMail, phone);
	}
	
	private void userActivate() {
		
		userShow();
		int ppID = myServer.readLineOptionalInt("Select a User ID: ");
		boolean userIDFound = myServer.getWSDatabase().isPPIDInUse(ppID);
		
		if(userIDFound == false) {
			
			myServer.println("User ID Not Found");

		} else {
			
			final String oldPPIDStatus = myServer.getWSDatabase().getActivationStatusString(ppID);
			myServer.println(oldPPIDStatus);
			
			// Toggle user
			final String choice = myServer.readLineOptional("Toggle user activation [Y/n]? ");
			
			if("yes".equalsIgnoreCase(choice) ||
				"y".equalsIgnoreCase(choice) ||
				choice.isEmpty() ) {
				
				myServer.getWSDatabase().toggleUserActivation(ppID);
				
			}
			
			final String newPPIDStatus = myServer.getWSDatabase().getActivationStatusString(ppID);
			myServer.println(newPPIDStatus);
		
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
