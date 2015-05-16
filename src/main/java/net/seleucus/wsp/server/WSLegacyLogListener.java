package net.seleucus.wsp.server;

import net.seleucus.wsp.db.WSDatabase;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Legacy class responsible for processing log file lines. This class is a callback listener
 * called by {@link org.apache.commons.io.input.Tailer}.
 *
 * @deprecated Deprecated since version 0.9. Replaced by {@link WebServerLogTailCallbackListener}
 *
 * @see net.seleucus.wsp.server.WebServerLogTailCallbackListener
 */
@Deprecated
public class WSLegacyLogListener extends TailerListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WSLegacyLogListener.class);

    public static final int EXPECTED_REQUEST_LENGTH = 100;

    private final WSServer myServer;
	private final WSDatabase myDatabase;
	private final Pattern wsPattern;

 	public WSLegacyLogListener(final WSServer myServer) {
 		this.myServer = myServer;
 		this.myDatabase = myServer.getWSDatabase();
 		this.wsPattern = Pattern.compile(myServer.getWSConfiguration().getLoginRegexForEachRequest());
 	}

    @Override
    public void handle(final String requestLine) {
    	
        // Check if the line length is more than 65535 chars
        if (requestLine == null || requestLine.length() > Character.MAX_VALUE) {
        	return;
        }
                      
        // Check if the regex pattern has been found
    	final Matcher wsMatcher = wsPattern.matcher(requestLine);
    	
        if (! wsMatcher.matches() || 2 != wsMatcher.groupCount( )) {
        	logger.info("Regex Problem?");
        	logger.info("Request line is {}.", requestLine);
        	logger.info("The regex is {}.", wsPattern.pattern());
            return;
        }

        final String ipAddress = wsMatcher.group(1);
        String webSpaRequest = wsMatcher.group(2);
        if(webSpaRequest.endsWith("/")) {
        	webSpaRequest = webSpaRequest.substring(0, webSpaRequest.length() - 1);
        }

        if(EXPECTED_REQUEST_LENGTH != webSpaRequest.length()){
            logger.warn("Request length expected to be {} but was {}", EXPECTED_REQUEST_LENGTH, webSpaRequest.length());
            return;
        }

        logger.info("The 100 chars received are {}.", webSpaRequest);
        // Get the unique user ID from the request
        final int ppId = myDatabase.passPhrases.getPPIDFromRequest(webSpaRequest);
        if(ppId < 0) {
            logger.warn("No User Found");
            return;
        }

        final String username = myDatabase.users.getUsersFullName(ppId);
        logger.info("User Found {}.", username);

        final boolean userActive = myDatabase.passPhrases.getActivationStatus(ppId);
        logger.info(myDatabase.passPhrases.getActivationStatusString(ppId));
        if(userActive) {

            final int action = myDatabase.actionsAvailable.getActionNumberFromRequest(ppId, webSpaRequest);
            logger.info("Action Number {}.", action);

            if(action >= 0 && action <= 9) {

                // Log this in the actions received table...
                final int aaID = myServer.getWSDatabase().actionsAvailable.getAAID(ppId, action);
                myServer.getWSDatabase().actionsReceived.addAction(ipAddress, webSpaRequest, aaID);

                // Log this on the screen for the user
                final String osCommand = myServer.getWSDatabase().actionsAvailable.getOSCommand(ppId, action);
                logger.info(ipAddress + " ->  '" + osCommand + "'");

                // Fetch and execute the O/S command...
                myServer.runOSCommand(ppId, action, ipAddress);
            }
        }
    }
}
