package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSServiceStatus extends WSCommandOption {

	public WSServiceStatus(WSServer myServer) {
		super(myServer);
	}

	@Override
	protected void execute() {

		myServer.serverStatus();
		
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
		
		if(cmd.equalsIgnoreCase("service status")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
