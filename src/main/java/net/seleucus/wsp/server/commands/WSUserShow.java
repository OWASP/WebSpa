package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSUserShow extends WSCommandOption {

	public WSUserShow(WSServer myServer) {
		super(myServer);
	}

	@Override
	protected void execute() {

		final String users = this.myServer.getWSDatabase().users.showUsers();
		myServer.println(users);

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
		
		if(cmd.equalsIgnoreCase("user show")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
