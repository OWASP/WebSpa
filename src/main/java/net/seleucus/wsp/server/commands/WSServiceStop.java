package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSServiceStop extends WSCommandOption {

	public WSServiceStop(WSServer myServer) {
		
		super(myServer);
		
	}

	@Override
	protected void execute() {
		
		myServer.serverStop();

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
		
		if(cmd.equalsIgnoreCase("service stop")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
