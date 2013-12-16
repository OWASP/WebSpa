package net.seleucus.wsp.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.db.WSDatabase;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

public class WSLogListener extends TailerListenerAdapter {

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
        	myServer.log("Regex Problem?\n");
        	myServer.log(requestLine);
        	myServer.log(myConfiguration.getLoginRegexForEachRequest());
            return;
        }

        final String ipAddress = wsMatcher.group(1);
        String webSpaRequest = wsMatcher.group(2);
        if(webSpaRequest.endsWith("/")) {
        	webSpaRequest = webSpaRequest.substring(0, webSpaRequest.length() - 1);
        }
        

        if(webSpaRequest.length() == 100) {
        	
        	// Nest the world away!
        	myServer.log(webSpaRequest); 
            // Get the unique user ID from the request
            final int ppID = myDatabase.passPhrases.getPPIDFromRequest(webSpaRequest);
            
            if(ppID < 0) {
                
            	myServer.log("No User Found");
            	
            } else {

            	String username = myDatabase.users.getUsersFullName(ppID); 
            	myServer.log("User Found: " + username);
            	// Check the user's activation status
            	final boolean userActive = myDatabase.passPhrases.getActivationStatus(ppID);
            	myServer.log(myDatabase.passPhrases.getActivationStatusString(ppID));
            	
                if(userActive) {
                	
                	final int action = myDatabase.actionsAvailable.getActionNumberFromRequest(ppID, webSpaRequest);
                	myServer.log("Action Number: " + action);
                	
                	if( (action >= 0) && (action <= 9) ) {
                	
                		// Log this in the actions received table...
                		final int aaID = myServer.getWSDatabase().actionsAvailable.getAAID(ppID, action);
                		myServer.getWSDatabase().actionsReceived.addAction(ipAddress, webSpaRequest, aaID);
                		
                		// Log this on the screen for the user
                		final String osCommand = myServer.getWSDatabase().actionsAvailable.getOSCommand(ppID, action);
                		myServer.log(ipAddress + " ->  '" + osCommand + "'");
                		
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
