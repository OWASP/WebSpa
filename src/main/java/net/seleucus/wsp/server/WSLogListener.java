package net.seleucus.wsp.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.seleucus.wsp.config.WSConfiguration;
import net.seleucus.wsp.crypto.WebSpaEncoder;
import net.seleucus.wsp.db.WSDatabase;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.lang3.ArrayUtils;

public class WSLogListener implements TailerListener {

	private WSDatabase myDatabase;
	private WSConfiguration myConfiguration;

 	public WSLogListener(WSDatabase myDatabase, WSConfiguration myConfiguration) {
 		this.myDatabase = myDatabase;
 		this.myConfiguration = myConfiguration;
 	}

	@Override
    public void fileNotFound() {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileRotated() {
        // TODO Auto-generated method stub

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
            System.err.println("\nRegex Problem?\n");
            System.err.println(requestLine);
            System.err.println(myConfiguration.getLoginRegexForEachRequest());
            return;
        }

        final String ipAddress = wsMatcher.group(1);
        String webSpaRequest = wsMatcher.group(2);
        if(webSpaRequest.endsWith("/")) {
        	webSpaRequest = webSpaRequest.substring(0, webSpaRequest.length() - 1);
        }

        if(webSpaRequest.length() != 100) {
            System.err.println("\nRequest is not 100 chars\n");
            return;        	
        }
 
        // Get the unique user ID from the request
        int ppID = myDatabase.getPPIDFromRequest(webSpaRequest);
        System.out.println("\nPPID: " + ppID + "\n");
        if(ppID < 0) {
        	return;
        }
        
        if(myDatabase.getActivationStatus(ppID)) {
        	
        	final int action = myDatabase.getActionNumberFromRequest(ppID, webSpaRequest);
        	System.out.println("\nAction Number: " + action + "\n");
        	if(action < 0) {
        		
        		return;
        		
        	} else {
        		
        		// Fetch and execute the O/S command...
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
