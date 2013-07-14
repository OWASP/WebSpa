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
		
		getWSConsole().writer().println("");
		getWSConsole().writer().println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		getWSConsole().writer().println("");
		
		getWSConsole().writer().println("Usage: java -jar web-spa.jar [-option]");
		getWSConsole().writer().println("");
		getWSConsole().writer().println("\t-client         : Run the client, generate valid requests");
		getWSConsole().writer().println("\t-help           : Print this usage message");
		getWSConsole().writer().println("\t-server         : Run the server");
		getWSConsole().writer().println("\t-version        : " + WSVersion.getValue());
		getWSConsole().writer().println("");
		getWSConsole().writer().println("Examples:");
		getWSConsole().writer().println("");
		getWSConsole().writer().println("java -jar web-spa.jar -client");
		getWSConsole().writer().println("java -jar web-spa.jar -server");
		getWSConsole().writer().println("");
	}

}
