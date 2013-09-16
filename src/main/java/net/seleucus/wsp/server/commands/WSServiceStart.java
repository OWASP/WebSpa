package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSServiceStart extends WSCommandOption {

	public WSServiceStart(WSServer myServer) {
		
		super(myServer);
		
	}

	@Override
	protected void execute() {

		myServer.serverStart();

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
		
		if(cmd.equalsIgnoreCase("service start")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
