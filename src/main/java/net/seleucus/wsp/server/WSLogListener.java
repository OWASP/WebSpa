package net.seleucus.wsp.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.db.WSDatabase;
import net.seleucus.wsp.main.WebSpa;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSLogListener extends TailerListenerAdapter {

	final static Logger LOGGER = LoggerFactory.getLogger(WSLogListener.class);    

	private WSServer myServer;
	private WSDatabase myDatabase;
	private WSConfiguration myConfiguration;

 	public WSLogListener(WSServer myServer) {
 		
 		this.myServer = myServer;
 		this.myDatabase = myServer.getWSDatabase();
 		this.myConfiguration = myServer.getWSConfiguration();
 		
 	}

    @Override
    public void handle(final String requestLine) {
    	
        // Check if the line length is more than 65535 chars
        if (requestLine.length() > Character.MAX_VALUE) {
        	return;
        }
                      
        // Check if the regex pattern has been found
    	Pattern wsPattern = Pattern.compile(myConfiguration.getLoginRegexForEachRequest());
    	Matcher wsMatcher = wsPattern.matcher(requestLine);        
    	
        if (!wsMatcher.matches( ) || 
            2 != wsMatcher.groupCount( )) {
        	LOGGER.info("Regex Problem?");
        	LOGGER.info("Request line is {}.", requestLine);
        	LOGGER.info("The regex is {}.", myConfiguration.getLoginRegexForEachRequest());
            return;
        }

        final String ipAddress = wsMatcher.group(1);
        String webSpaRequest = wsMatcher.group(2);
        if(webSpaRequest.endsWith("/")) {
        	webSpaRequest = webSpaRequest.substring(0, webSpaRequest.length() - 1);
        }
        

        if(webSpaRequest.length() == 100) {
        	
        	// Nest the world away!
        	LOGGER.info("The 100 chars received are {}.", webSpaRequest); 
            // Get the unique user ID from the request
            final int ppID = myDatabase.passPhrases.getPPIDFromRequest(webSpaRequest);
            
            if(ppID < 0) {
                
            	LOGGER.info("No User Found");
            	
            } else {

            	String username = myDatabase.users.getUsersFullName(ppID); 
            	LOGGER.info("User Found {}.", username);
            	// Check the user's activation status
            	final boolean userActive = myDatabase.passPhrases.getActivationStatus(ppID);
            	LOGGER.info(myDatabase.passPhrases.getActivationStatusString(ppID));
            	
                if(userActive) {
                	
                	final int action = myDatabase.actionsAvailable.getActionNumberFromRequest(ppID, webSpaRequest);
                	LOGGER.info("Action Number {}.", action);
                	
                	if( (action >= 0) && (action <= 9) ) {
                	
                		// Log this in the actions received table...
                		final int aaID = myServer.getWSDatabase().actionsAvailable.getAAID(ppID, action);
                		myServer.getWSDatabase().actionsReceived.addAction(ipAddress, webSpaRequest, aaID);
                		
                		// Log this on the screen for the user
                		final String osCommand = myServer.getWSDatabase().actionsAvailable.getOSCommand(ppID, action);
                		LOGGER.info(ipAddress + " ->  '" + osCommand + "'");
                		
                		// Fetch and execute the O/S command...        		
                		myServer.runOSCommand(ppID, action, ipAddress);

                	}
                }

            }
            
            
        }
 


    }

    @Override
    public void handle(Exception arg0) {

    }

    @Override
    public void init(Tailer arg0) {
        // TODO Auto-generated method stub
    }

}
