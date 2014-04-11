package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSPassPhraseModify extends WSCommandOption {

	public WSPassPhraseModify(WSServer myServer) {
		super(myServer);
	}

	@Override
	protected void execute() {
		
		final String users = this.myServer.getWSDatabase().users.showUsers();
		myServer.println(users);
		
		int ppID = myServer.readLineOptionalInt("Select a User ID");
		boolean userIDFound = myServer.getWSDatabase().passPhrases.isPPIDInUse(ppID);
		
		if(userIDFound == false) {
			
			myServer.println("User ID Not Found");

		} else {
			
			String userName = myServer.getWSDatabase().users.getUsersFullName(ppID);
			
			final String choice = myServer.readLineOptional("Change pass-phrase [y/N]");
			
			if("yes".equalsIgnoreCase(choice) ||
				"y".equalsIgnoreCase(choice) ) {
				
				boolean passPhraseInUse = true;
				CharSequence passSeq;
				
				do {
					
					passSeq = myServer.readPasswordRequired("Enter the User's New Pass-Phrase");
								
					passPhraseInUse = myServer.getWSDatabase().passPhrases.isPassPhraseInUse(passSeq);
					
					if(passPhraseInUse == true) {
						myServer.println("This Pass-Phrase is already taken and in use by another user");
						myServer.println("WebSpa pass-phrases have to be unique for each user");
					}
					
					
				} while(passPhraseInUse);
				
				boolean success = myServer.getWSDatabase().passPhrases.updatePassPhrase(ppID, passSeq);

				myServer.println("");
				myServer.println("ID: " + ppID);
				myServer.println("Full Name: " + userName);
				myServer.println("");
				if(success) {
					
					myServer.println("Pass-Phrase Updated Successfully");
					
				} else {
					
					myServer.println("Pass-Phrase Unchanged");
				}
				myServer.println("");
				
			}
			
		}
		
	} // execute method

	@Override
	public boolean handle(String cmd) {

		boolean validCommand = false;

		if(isValid(cmd)) {
			validCommand = true;
			this.execute();
		}
		
		return validCommand;
		
	} // handle method

	@Override
	protected boolean isValid(String cmd) {
		
		boolean valid = false;
		
		if(cmd.equalsIgnoreCase("pass-phrase modify")  || 
		   cmd.equalsIgnoreCase("passwd")) {
			
			valid = true;
			
		}
		
		return valid;
		
	} // isValid method

}
