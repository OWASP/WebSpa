package net.seleucus.wsp.server.commands;

import net.seleucus.wsp.server.WSServer;

public abstract class WSCommandOption {

	protected WSServer myServer;

	public WSCommandOption(WSServer myServer) {
		this.myServer = myServer;
	}
	
	protected abstract void execute();
	public abstract boolean handle(final String cmd);
	protected abstract boolean isValid(final String cmd);

}
