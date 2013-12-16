package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSUserActivate extends WSCommandOption {

	public WSUserActivate(WSServer myServer) {
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
			
			final String oldPPIDStatus = myServer.getWSDatabase().passPhrases.getActivationStatusString(ppID);
			myServer.println(oldPPIDStatus);
			
			// Toggle user
			final String choice = myServer.readLineOptional("Toggle user activation [Y/n]");
			
			if("yes".equalsIgnoreCase(choice) ||
				"y".equalsIgnoreCase(choice) ||
				choice.isEmpty() ) {
				
				myServer.getWSDatabase().passPhrases.toggleUserActivation(ppID);
				
			}
			
			final String newPPIDStatus = myServer.getWSDatabase().passPhrases.getActivationStatusString(ppID);
			myServer.println(newPPIDStatus);
		
		}
		
	} // execute method

	@Override
	public boolean handle(final String cmd) {

		boolean validCommand = false;

		if(isValid(cmd)) {
			validCommand = true;
			this.execute();
		}
		
		return validCommand;
		
	} // handle method

	@Override
	protected boolean isValid(final String cmd) {
		
		boolean valid = false;
		
		if(cmd.equalsIgnoreCase("user activate")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
