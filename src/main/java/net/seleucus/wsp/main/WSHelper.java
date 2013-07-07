package net.seleucus.wsp.main;

public class WSHelper extends WSGestalt {

	public WSHelper(WebSpa myWebSpa) {
		super(myWebSpa);
	}

	@Override
	public void exitConsole() {
		// Nothing to add here...
	}

	@Override
	public void runConsole() {
		
		getMyConsole().writer().println("");
		getMyConsole().writer().println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		getMyConsole().writer().println("");
		
		getMyConsole().writer().println("Usage: java -jar web-spa.jar [-option]");
		getMyConsole().writer().println("");
		getMyConsole().writer().println("\t-client         : Run the client, generate valid requests");
		getMyConsole().writer().println("\t-help           : Print this usage message");
		getMyConsole().writer().println("\t-server         : Run the server");
		getMyConsole().writer().println("\t-version        : " + WSVersion.getValue());
		getMyConsole().writer().println("");
		getMyConsole().writer().println("Examples:");
		getMyConsole().writer().println("");
		getMyConsole().writer().println("java -jar web-spa.jar -client");
		getMyConsole().writer().println("java -jar web-spa.jar -server");
		getMyConsole().writer().println("");
	}

}
