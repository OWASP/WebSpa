package net.seleucus.wsp.server.commands;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.seleucus.wsp.server.WSServer;

import org.apache.commons.io.IOUtils;

public class WSHelpOptions extends WSCommandOption {
	
    private static final String[] ARRAY_COMMANDS = new String[] { "action", "config", "help", "pass-phrase", "service", "shortcuts", "user" };
    private static final Set<String> SERVER_COMMANDS = new HashSet<String>(Arrays.asList(ARRAY_COMMANDS));

	private String option;

	public WSHelpOptions(WSServer myServer) {
		
		super(myServer);
		option = "default";
		
	}

	@Override
	protected void execute() {
		
		final int MAX_CHARS = 4096;
		
		InputStream fstream = ClassLoader.getSystemResourceAsStream("help/" + option);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = null;
	
		StringBuffer fileContents = new StringBuffer(MAX_CHARS + 100);
		
		int counter = 0;
		int currentChar;
		
		try {
			
			br = new BufferedReader(new InputStreamReader(in));
			
			while (((currentChar = in.read()) > 0) && (counter < MAX_CHARS)) {
				
				if( (currentChar >= 32 && currentChar < 127) || (currentChar == 10) ) {
					fileContents.append((char) currentChar);
				}
				counter++;
				
			}
			
			br.close();
			in.close();
						
		} catch (IOException e) {
			
			fileContents.append("Attempting to open the file caused an I/O Error:\n\n" + option);
			
		} finally {
			
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(in);
			
		}
		
		if(counter == MAX_CHARS) {
			
			fileContents.append("\n... stopped reading after " + MAX_CHARS + " characters.\n");
			
		}
		
		System.out.println(fileContents.toString());		
		
	} // execute method

	@Override
	public boolean handle(String cmd) {
		
		boolean validCommand = false;
		option = "default";

		if(isValid(cmd)) {
			validCommand = true;
			this.execute();
		}
		
		return validCommand;
		
	} // handle method

	@Override
	protected boolean isValid(final String cmd) {
		
		boolean valid = false;
		
		if(cmd.equalsIgnoreCase("help")  || 
		   cmd.equalsIgnoreCase("?")) {
			
			valid = true;
			
		} else if(cmd.startsWith("help")) {
			
			final String[] params = cmd.split(" ");
			
			if(params.length > 1 ) {
				
				if(SERVER_COMMANDS.contains(params[1])) {
					option = params[1];
					valid = true;
				}
				
			} // params > 1
		} 
		
		return valid;
		
	} // isValid method

}
