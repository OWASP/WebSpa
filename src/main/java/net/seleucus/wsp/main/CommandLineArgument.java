package net.seleucus.wsp.main;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum CommandLineArgument {

    SHOW_HELP("-help", "Print this usage message"),
    CLIENT_MODE("-client", "Run the client, generate valid requests"),
    SERVER_MODE("-server", "Run the server"),
    SHOW_VERSION("-version", "Display application version number"),
    START("-start", "Start the daemon service"),
    STOP("-stop", "Stop the daemon service"),
    SHOW_STATUS("-status", "Display the status of the daemon service");

    private static final Map<String, CommandLineArgument> ARGUMENTS_BY_NAME = new HashMap<>(values().length);

    static {
        for(final CommandLineArgument arg: values()){
            ARGUMENTS_BY_NAME.put(arg.getName().toLowerCase(), arg);
        }
    }

    public static CommandLineArgument getByName(final String name){
        return ARGUMENTS_BY_NAME.get(StringUtils.lowerCase(name));
    }

    private final String name;
    private final String description;

    private CommandLineArgument(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
