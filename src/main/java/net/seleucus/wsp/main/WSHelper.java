package net.seleucus.wsp.main;

public class WSHelper extends WSGestalt {

	public WSHelper(WebSpa myWebSpa) {
		super(myWebSpa);
	}


	@Override
	public void runConsole() {
		
		println("");
		println("WebSpa - Single HTTP/S Request Authorisation");
		println("version " + WSVersion.getValue() + " (webspa@seleucus.net)"); 		
		println("");
		
		println("Usage: java -jar webspa.jar [-option]");
		println("");
		println("\t-client         : Run the client, generate valid requests");
		println("\t-help           : Print this usage message");
		println("\t-server         : Run the server");
		println("\t-version        : " + WSVersion.getValue());
		println("");
		println("Examples:");
		println("");
		println("java -jar webspa.jar -client");
		println("java -jar webspa.jar -server");
		println("");
		
	}

	@Override
	public void exitConsole() {
		
	}

}
