package net.seleucus.wsp.server;


import java.util.ArrayList;

import net.seleucus.wsp.server.commands.WSActionAdd;
import net.seleucus.wsp.server.commands.WSActionShow;
import net.seleucus.wsp.server.commands.WSCommandOption;
import net.seleucus.wsp.server.commands.WSConfigShow;
import net.seleucus.wsp.server.commands.WSHelpOptions;
import net.seleucus.wsp.server.commands.WSServiceStart;
import net.seleucus.wsp.server.commands.WSServiceStatus;
import net.seleucus.wsp.server.commands.WSServiceStop;
import net.seleucus.wsp.server.commands.WSUserActivate;
import net.seleucus.wsp.server.commands.WSUserAdd;
import net.seleucus.wsp.server.commands.WSUserShow;


public class WSServerConsole {

	private WSServer myServer;
	private ArrayList<WSCommandOption> commands;
	
	protected WSServerConsole(WSServer myServer) {
		
		this.commands = new ArrayList<WSCommandOption>();
        this.myServer = myServer;

		this.commands.add(new WSActionAdd(this.myServer));
		this.commands.add(new WSActionShow(this.myServer));
		this.commands.add(new WSConfigShow(this.myServer));
		this.commands.add(new WSHelpOptions(this.myServer));
		this.commands.add(new WSServiceStart(this.myServer));
		this.commands.add(new WSServiceStatus(this.myServer));
		this.commands.add(new WSServiceStop(this.myServer));
		this.commands.add(new WSUserActivate(this.myServer));
		this.commands.add(new WSUserAdd(this.myServer));
		this.commands.add(new WSUserShow(this.myServer));
		
	}

	public void executeCommand(final String command) {
		
		boolean commandFound = false;
		
		for (WSCommandOption action : commands) {
			
			if(action.handle(command)){
				commandFound = true;
				return;
			}
		}

		if( (commandFound == false) && (command.isEmpty() == false) ) {
			
			System.out.println("\nUnknown command - type \"help\" for more options\n");

		}

/*		String[] params = command.split(" ");

		if(command.startsWith("action") ) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("add")) {
					
					actionAdd();
					
				}
				else
				if(params[1].equalsIgnoreCase("delete")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("modify")) {				
					
				} 
				else
				if(params[1].equalsIgnoreCase("show")) {
					
					actionShow();
					
				} else {
					
					myServer.println("\nInvalid Option. Type \"help service\" for further information");

				}
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help service\" for further information");
				
			}
		} // action end
		else
		if(command.equalsIgnoreCase("config show") || 
		   command.equalsIgnoreCase("c")) {
			
			final String prop1 = myServer.getWSConfiguration().getAccesLogFileLocation();
			final String prop2 = myServer.getWSConfiguration().getLoginRegexForEachRequest();

			myServer.println("\n1. " + prop1);
			myServer.println("\n2. " + prop2);
			
			myServer.println("\nType \"help config\" for further information");
		}
		else
		if(command.startsWith("help")) {
			
			if(params.length > 1) {

				if(WSConstants.SERVER_COMMANDS.contains(params[1])) {
					
					WSServerConsole.showHelp(params[1]);
					
				} else {
					
					WSServerConsole.showHelp("default");
					
				}
				
			} else {
				
				WSServerConsole.showHelp("default");
				myServer.println("This is a holding prompt, type \"exit\" to quit");        

			}
			
		} // help end
		else
		if(command.startsWith("service") ) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("start")) {
					
					myServer.serverStart();
					
				}
				else
				if(params[1].equalsIgnoreCase("status")) {
					
					String status = myServer.serverStatus();
					myServer.println(status);
					
				}
				else
				if(params[1].equalsIgnoreCase("stop")) {
					
					myServer.serverStop();
					
				} else {
					
					myServer.println("\nInvalid Option. Type \"help service\" for further information");

				}
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help service\" for further information");
				
			}
		} // service end
		else
		if(command.startsWith("user")) {
			
			if(params.length > 1) {
				
				if(params[1].equalsIgnoreCase("activate")) {
					
					userActivate();
				}
				else
				if(params[1].equalsIgnoreCase("add")) {
					
					userAdd();
					
				}
				else
				if(params[1].equalsIgnoreCase("delete")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("modify")) {
					
				}
				else
				if(params[1].equalsIgnoreCase("show")) {
					
					userShow();
					
				} else {
					
					myServer.println("\nInvalid Option. Type \"help user\" for further information");
					
				}
				
			} else {
				
				myServer.println("\nNo Option Specified. Type \"help user\" for further information");
			}
		} // user end
		else {
			
			System.out.println("\nUnknown command - type \"help\" for more options");
			
		}
		*/
	}

}
