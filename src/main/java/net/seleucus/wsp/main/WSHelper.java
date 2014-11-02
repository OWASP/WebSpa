package net.seleucus.wsp.main;

public class WSHelper extends WSGestalt {

	public WSHelper(WebSpa myWebSpa) {
		super(myWebSpa);
	}


	@Override
	public void runConsole() {
		
		final String versionNoDots = WSVersion.getMajor() + "" + WSVersion.getMinor();
		
		myConsole.println("");
		myConsole.println("WebSpa - Single HTTP/S Request Authorisation");
		myConsole.println("version " + WSVersion.getValue() + " (webspa@seleucus.net)"); 		
		myConsole.println("");
		
		myConsole.println("Usage: java -jar webspa.jar [-option]");
		myConsole.println("");
		myConsole.println("\t-client         : Run the client, generate valid requests");
		myConsole.println("\t-help           : Print this usage message");
		myConsole.println("\t-server         : Run the server");
		myConsole.println("\t-start          : Start the daemon service");
		myConsole.println("\t-status         : Display the status of the daemon service");
		myConsole.println("\t-stop           : Stop the daemon service");
		myConsole.println("\t-version        : " + WSVersion.getValue());
		myConsole.println("");
		myConsole.println("Examples:");
		myConsole.println("");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -client");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -server");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -status");
		myConsole.println("");
		
	}

	@Override
	public void exitConsole() {
		
	}

}
