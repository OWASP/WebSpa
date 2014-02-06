package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSPassPhraseShow extends WSCommandOption {

	public WSPassPhraseShow(WSServer myServer) {
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
			
			final String choice = myServer.readLineOptional("Show pass-phrase [y/N]");
			
			if("yes".equalsIgnoreCase(choice) ||
				"y".equalsIgnoreCase(choice) ) {
				
				final CharSequence passSeq = myServer.getWSDatabase().passPhrases.getPassPhrase(ppID);
				final String lastModifiedDate = myServer.getWSDatabase().passPhrases.getLastModifiedDate(ppID);
				myServer.println("");
				myServer.println("ID: " + ppID);
				myServer.println("Full Name: " + userName);
				myServer.println("Pass-Phrase: " + passSeq.toString());
				myServer.println("Last Modified: " + lastModifiedDate );
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
		
		if(cmd.equalsIgnoreCase("pass-phrase show")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
