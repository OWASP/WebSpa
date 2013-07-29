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
		
		println("");
		println("Web-Spa - Single HTTP/S Request Authorisation - version " + WSVersion.getValue() + " (web-spa@seleucus.net)"); 
		println("");
		
		println("Usage: java -jar web-spa.jar [-option]");
		println("");
		println("\t-client         : Run the client, generate valid requests");
		println("\t-help           : Print this usage message");
		println("\t-server         : Run the server");
		println("\t-version        : " + WSVersion.getValue());
		println("");
		println("Examples:");
		println("");
		println("java -jar web-spa.jar -client");
		println("java -jar web-spa.jar -server");
		println("");
		
	}

}
