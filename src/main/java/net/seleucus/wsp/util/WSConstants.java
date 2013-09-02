package net.seleucus.wsp.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WSConstants {

    public static final String LOGGING_REGEX_FOR_EACH_REQUEST = "logging-regex-for-each-request";
    public static final String ACCESS_LOG_FILE_LOCATION = "access-log-file-location";
    
    private static final String[] ARRAY_COMMANDS = new String[] { "action", "config", "help", "service", "shortcuts", "user" };
    public static final Set<String> SERVER_COMMANDS = new HashSet<String>(Arrays.asList(ARRAY_COMMANDS));

}
