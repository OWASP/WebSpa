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

        for(final CommandLineArgument arg : CommandLineArgument.values()){
            myConsole.println(String.format("\t%1$-16s : %2$s", arg.getName(), arg.getDescription()));
        }

		myConsole.println("");
		myConsole.println("Examples:");
		myConsole.println("");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -client");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -server");
		myConsole.println("java -jar webspa-" + versionNoDots + ".jar -status");
		myConsole.println("");
	}
}
