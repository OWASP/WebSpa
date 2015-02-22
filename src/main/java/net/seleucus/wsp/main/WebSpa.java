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

import static net.seleucus.wsp.main.CommandLineArgument.*;

public class WebSpa {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebSpa.class);    

	private WSConsole myConsole;
	
	public WebSpa(final WSConsole myConsole) {
		this.myConsole = myConsole;
	}
	
	public WSConsole getConsole() {
		return myConsole;
	}
	
	public CommandLineArgument processParameters(final String[] args) {
        if(args.length == 0){
            return SHOW_HELP;
        }

        final CommandLineArgument mode = CommandLineArgument.getByName(args[0]);
		return mode != null ? mode : SHOW_HELP;
	}
	
	public static void main(String[] args) throws Exception {
		if(! WSUtil.hasMinJreRequirements(1, 6)) {
			LOGGER.error("!!! Minimum JRE requirements are 1.6 !!!");
			System.exit(1);
		}

		WSGestalt wsGestalt;
		final WebSpa webSpa = new WebSpa(WSConsole.getWsConsole());
		final CommandLineArgument argument = webSpa.processParameters(args);

		switch (argument) {
		case SHOW_HELP:
		    LOGGER.info("Invalid Parameter Specified - Use \"java -jar webspa-{}{}.jar -help\" for More Options", WSVersion.getMajor(), WSVersion.getMinor() );
            wsGestalt = new WSHelper(webSpa);
		 	break; 
		case CLIENT_MODE:
			LOGGER.info("Welcome - Running the WebSpa Client");
            wsGestalt = new WSClient(webSpa);
			break;
		case SERVER_MODE:
			LOGGER.info("Welcome - Running the WebSpa Server");
            wsGestalt = new WSServer(webSpa);
		 	break;
		case SHOW_VERSION:
            wsGestalt = new WSVersion(webSpa);
			break;
		case START:
			LOGGER.info("No Action - A Future Way to Start the WebSpa Server");
            wsGestalt = new WSDaemonStart(webSpa);
			break;
		case STOP:
			LOGGER.info("No Action - A Future Way to Stop the WebSpa Server");
            wsGestalt = new WSDaemonStop(webSpa);
			break;
		case SHOW_STATUS:
			LOGGER.info("No Action - A Future Way to Query the Status of the WebSpa Server");
            wsGestalt = new WSDaemonStatus(webSpa);
			break;
		default:
		 	wsGestalt = new WSHelper(webSpa);
		 	break;
		}
        wsGestalt.runConsole();
        wsGestalt.exitConsole();
	}
}
