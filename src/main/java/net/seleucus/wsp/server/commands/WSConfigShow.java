package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public class WSConfigShow extends WSCommandOption {

	public WSConfigShow(WSServer myServer) {
		super(myServer);
	}

	@Override
	protected void execute() {

		final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
		final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();

		myServer.println("\n1. " + prop1);
		myServer.println("\n2. " + prop2);
		
		myServer.println("\nType \"help config\" for further information");
		
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
	protected boolean isValid(final String cmd) {
		
		boolean valid = false;
		
		if(cmd.equalsIgnoreCase("config show") || 
		   cmd.equalsIgnoreCase("c")) {
			
			valid = true;
		
		}
		
		return valid;
		
	}  // isValid method

}
