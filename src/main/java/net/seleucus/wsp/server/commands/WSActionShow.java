package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSActionShow extends WSCommandOption {

	public WSActionShow(WSServer myServer) {
		super(myServer);
	}

	@Override
	protected void execute() {

		final String users = this.myServer.getWSDatabase().users.showUsers();
		myServer.println(users);
		
		final int ppID = myServer.readLineOptionalInt("Select a User ID");
		final boolean userIDFound = myServer.getWSDatabase().passPhrases.isPPIDInUse(ppID);
		
		if(userIDFound == false) {
			
			myServer.println("User ID Not Found");

		} else {
			
			final String actions = myServer.getWSDatabase().actionsAvailable.showActions(ppID);
			myServer.println(actions);
			
			final int actionNumber = myServer.readLineOptionalInt("Select an Action Number");
			final String actionDetails = myServer.getWSDatabase().actionsAvailable.showActionDetails(ppID, actionNumber);
			myServer.println(actionDetails);
			
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
		
		if(cmd.equalsIgnoreCase("action show")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
