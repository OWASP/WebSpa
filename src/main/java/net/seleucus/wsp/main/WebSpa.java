package net.seleucus.wsp.main;

import net.seleucus.wsp.client.WSClient;
import net.seleucus.wsp.console.WSConsole;
import net.seleucus.wsp.daemon.WSDaemonStart;
import net.seleucus.wsp.daemon.WSDaemonStatus;
import net.seleucus.wsp.daemon.WSDaemonStop;
import net.seleucus.wsp.server.WSServer;
import net.seleucus.wsp.util.WSUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSpa {

	protected static final String[] ALLOWED_FIRST_PARAM = {"-help", "-client", "-server", "-version", "-start", "-stop", "-status"};
	
	private final static Logger LOGGER = LoggerFactory.getLogger(WebSpa.class);    

	private WSConsole myConsole;
	
	public WebSpa(final WSConsole myConsole) {
		
		this.myConsole = myConsole;
		
	}
	
	public WSConsole getConsole() {
		return myConsole;
	}
	
	public int processParameters(final String[] args) {
		int mode = -2;
		
		if(args.length > 0) {

			if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[0])) {
				// java -jar webspa.jar -help
				mode = 0;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[1])) {
				// java -jar webspa.jar -client
				mode = 1;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[2])) {
				// java -jar webspa.jar -server
				LOGGER.info("Running WebSpa Server");
				mode = 2;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[3])) {
				// java -jar webspa.jar -version
				mode = 3;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[4])) {
				// java -jar webspa.jar -start
				mode = 4;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[5])) {
				// java -jar webspa.jar -stop
				mode = 5;
			} else if(args[0].equalsIgnoreCase(ALLOWED_FIRST_PARAM[6])) {
				// java -jar webspa.jar -status
				mode = 6;
			} else {
				// java -jar webspa.jar -invalidParam
				mode = -1;
			}
		}
		
		return mode;
	}
	
	public static void main(String[] args) throws Exception {

		final WSConsole myWsConsole = WSConsole.getWsConsole();
		
		if(!WSUtil.hasMinJreRequirements(1, 6)) {
			LOGGER.error("!!! Minimum JRE requirements are 1.6 !!!");
			System.exit(1);
		}

		WSGestalt myGestalt; 
		WebSpa mySpa = new WebSpa(myWsConsole);
		int mode = mySpa.processParameters(args);

		switch (mode) {
		case -1:
		    LOGGER.info("Invalid Parameter Specified - Use \"java -jar webspa-{}{}.jar -help\" for More Options", WSVersion.getMajor(), WSVersion.getMinor() );
		 	myGestalt = new WSHelper(mySpa);
		 	break; 
		case 1:
			LOGGER.info("Welcome - Running the WebSpa Client");
			myGestalt = new WSClient(mySpa);
			myGestalt.runConsole();
			break;
		case 2:
			LOGGER.info("Welcome - Running the WebSpa Server");
			myGestalt = new WSServer(mySpa);
			myGestalt.runConsole();
		 	break;
		case 3:
			// System.out.println("version");
			myGestalt = new WSVersion(mySpa);
			myGestalt.runConsole();
			break;
		case 4:
			LOGGER.info("No Action - A Future Way to Start the WebSpa Server");
			myGestalt = new WSDaemonStart(mySpa);
			myGestalt.runConsole();
			break;
		case 5:
			LOGGER.info("No Action - A Future Way to Stop the WebSpa Server");
			myGestalt = new WSDaemonStop(mySpa);
			myGestalt.runConsole();
			break;
		case 6:
			LOGGER.info("No Action - A Future Way to Query the Status of the WebSpa Server");
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
