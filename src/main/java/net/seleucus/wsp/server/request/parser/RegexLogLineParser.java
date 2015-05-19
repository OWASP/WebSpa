package net.seleucus.wsp.server.request.parser;

import net.seleucus.wsp.server.request.domain.LogMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.seleucus.wsp.server.request.domain.LogMessage.LogMessageBuilder.createLogMessage;

public class RegexLogLineParser implements LogLineParser {

    private final Pattern pattern;

    public RegexLogLineParser(final String regex){
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public LogMessage parse(String line) throws LogParseException {
        final Matcher matcher = pattern.matcher(line);
        if(!matcher.matches()){
            throw new LogParseException("Log message does not match the expected regex pattern");
        }

        //TODO: Extract the log message attributes from the string using regex named groups
        return createLogMessage().build();
    }
}
