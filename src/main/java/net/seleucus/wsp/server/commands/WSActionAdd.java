package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSActionAdd extends WSCommandOption {

	public WSActionAdd(WSServer myServer) {
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
			
			myServer.println("The existing actions for this user are: ");
			final String actions = myServer.getWSDatabase().actionsAvailable.showActions(ppID);
			myServer.println(actions);
			
			final String osCommand = myServer.readLineRequired("Enter the new O/S Command");
			int action = myServer.readLineRequiredInt("Select an action number for this O/S Command", 0, 9);
			
			final boolean actionNumberInUse = myServer.getWSDatabase().actionsAvailable.isActionNumberInUse(ppID, action);
			
			if(actionNumberInUse == false) {
				
				myServer.getWSDatabase().actionsAvailable.addAction(ppID, osCommand, action);
				
			} else {
				
				myServer.println("I am sorry, that Action Number is already in Use");
				
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
		
		if(cmd.equalsIgnoreCase("action add")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
