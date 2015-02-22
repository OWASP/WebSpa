package net.seleucus.wsp.main;

import net.seleucus.wsp.console.WSConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.sql.SQLException;
import java.util.Arrays;

public abstract class WSGestalt {

	private final static Logger LOGGER = LoggerFactory.getLogger(WSGestalt.class);    

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_RESET = "\u001B[0m";

	protected final WSConsole myConsole;

	public WSGestalt(WebSpa myWebSpa) {
		this.myConsole = myWebSpa.getConsole();
	}
	
	public String readLineOptional(final String displayString) {
		final StringBuilder displayBuilder = new StringBuilder();
		
		displayBuilder.append("[Input Option] ");
		displayBuilder.append(displayString);
		displayBuilder.append(':');
		displayBuilder.append(' ');
		
		return myConsole.readLine(ANSI_BLUE + " USER [xx.xxx] " + displayBuilder.toString() + ANSI_RESET);
	}
	
	public int readLineOptionalInt(final String displayString) {
		final String inputString = readLineOptional(displayString);
		int output = -1;
		
		try {
			output = Integer.parseInt(inputString);
			
		} catch(NumberFormatException ex) {
			LOGGER.error("Invalid Number");
		}
		
		return output;
	}
	
	public String readLineRequired(final String displayString) {
		final StringBuilder displayBuilder = new StringBuilder();
		
		displayBuilder.append("[Input Required] ");
		displayBuilder.append(displayString);
		displayBuilder.append(':');
		displayBuilder.append(' ');
		
		String outputString;
		
		do {
			outputString = myConsole.readLine(ANSI_BLUE + " USER [xx.xxx] " + displayBuilder.toString() + ANSI_RESET).trim();
			
			if(outputString.isEmpty()) {
				LOGGER.error("Your Input Cannot Be Blank");
			}
			
		} while(outputString.isEmpty());
		
		return outputString;
	}
	
	public int readLineRequiredInt(final String displayString, int minInclusiveValue, int maxInclusiveValue) {
		final StringBuilder displayBuilder = new StringBuilder();
		displayBuilder.append(displayString);
		displayBuilder.append(" [");
		displayBuilder.append(minInclusiveValue);
		displayBuilder.append(',');
		displayBuilder.append(maxInclusiveValue);
		displayBuilder.append(']');
		
		int output = -1;
		do {
			final String inputString = readLineRequired(displayBuilder.toString());
			
			try {
				output = Integer.parseInt(inputString);
			
			} catch(NumberFormatException ex) {
				LOGGER.error("Invalid Number Format");
			}
		
		} while( (output < minInclusiveValue) || (output > maxInclusiveValue) );
		
		return output;
	}
	
	public String readLineServerPrompt() {
		return myConsole.readLine( ANSI_PURPLE + "webspa-server> " + ANSI_RESET );
	}
	
	public CharSequence readPasswordRequired(final String displayString) {
		final StringBuilder displayBuilder = new StringBuilder();
		displayBuilder.append("[Input Required] ");
		displayBuilder.append(displayString);
		displayBuilder.append(':');
		displayBuilder.append(' ');
		
		char[] passCharArrayOne, passCharArrayTwo;
		boolean passPhrasesMatch = false;
		
		do {
			passCharArrayOne = myConsole.readPassword(ANSI_PURPLE + " PASS [xx.xxx] " + displayBuilder.toString() + ANSI_RESET);
			passCharArrayTwo = myConsole.readPassword(ANSI_PURPLE + " PASS [xx.xxx] [Input Required] Re-enter the above value: " + ANSI_RESET);
			
			passPhrasesMatch = Arrays.equals(passCharArrayOne, passCharArrayTwo);
			
			if(!passPhrasesMatch) {
				
				LOGGER.info("The Pass-Phrases Entered do not Match");
				LOGGER.info("Please try again");
				
			} else if(passCharArrayOne.length <= 0) {
				LOGGER.error(ANSI_RED + "The Pass-Phrase Cannot Be Blank" + ANSI_RESET);
			}
						
		} while( (passCharArrayOne.length <= 0) || (!passPhrasesMatch) );
		
		return CharBuffer.wrap(passCharArrayOne);
	}

	public void exitConsole(){
        LOGGER.info("Goodbye!");
    }

	public void runConsole() throws SQLException {
        LOGGER.debug("Run console called");
    }
	
}