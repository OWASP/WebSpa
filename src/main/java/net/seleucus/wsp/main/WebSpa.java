package net.seleucus.wsp.main;

import net.seleucus.wsp.client.WSClient;
import net.seleucus.wsp.console.WSConsole;
import net.seleucus.wsp.daemon.WSDaemonStart;
import net.seleucus.wsp.daemon.WSDaemonStatus;
import net.seleucus.wsp.daemon.WSDaemonStop;
import net.seleucus.wsp.server.WSServer;
import net.seleucus.wsp.util.WSUtil;

public class WebSpa {

	protected static final String[] ALLOWED_FIRST_PARAM = {"-help", "-client", "-server", "-version", "-start", "-stop", "-status"};
	
	private WSConsole myConsole;
	
	public WebSpa(final WSConsole myConsole) {
		
		this.myConsole = myConsole;
		
	}
	
	public WSConsole getConsole() {
		return myConsole;
	}
	
	public int processParameters(final String[] args) {
		int mode = -1;
		
		if(args.length > 0) {
			// java -jar webspa.jar -help
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[0])) {
				mode = 0;
			}
			// java -jar webspa.jar -client
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[1])) {
				mode = 1;
			}
			// java -jar webspa.jar -server
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[2])) {
				mode = 2;
			}
			// java -jar webspa.jar -version
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[3])) {
				mode = 3;
			}
			// java -jar webspa.jar -start
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[4])) {
				mode = 4;
			}
			// java -jar webspa.jar -stop
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[5])) {
				mode = 5;
			}
			// java -jar webspa.jar -status
			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[6])) {
				mode = 6;
			}
		}
		
		return mode;
	}
	
	public static void main(String[] args) throws Exception {

		final WSConsole myWsConsole = WSConsole.getWsConsole();
		
		if(!WSUtil.hasMinJreRequirements(1, 6)) {
			myWsConsole.println("!!! Minimum JRE requirements are 1.6 !!!");
			System.exit(1);
		}

		WSGestalt myGestalt; 
		WebSpa mySpa = new WebSpa(myWsConsole);
		int mode = mySpa.processParameters(args);

		switch (mode) {
		case 1:
			// System.out.println("client");
			myGestalt = new WSClient(mySpa);
			myGestalt.runConsole();
			break;
		case 2:
		    // System.out.println("server");
			myGestalt = new WSServer(mySpa);
			myGestalt.runConsole();
		 	break;
		case 3:
			// System.out.println("version");
			myGestalt = new WSVersion(mySpa);
			myGestalt.runConsole();
			break;
		case 4:
			System.out.println("start");
			myGestalt = new WSDaemonStart(mySpa);
			myGestalt.runConsole();
			break;
		case 5:
			System.out.println("stop");
			myGestalt = new WSDaemonStop(mySpa);
			myGestalt.runConsole();
			break;
		case 6:
			System.out.println("status");
			myGestalt = new WSDaemonStatus(mySpa);
			myGestalt.runConsole();
			break;
		default:
		 	// System.out.println("default");
		 	myGestalt = new WSHelper(mySpa);
		 	myGestalt.runConsole();
		 	break;
		}
		
		myGestalt.exitConsole();
	}
	
}
