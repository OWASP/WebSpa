package net.seleucus.wsp.server.request.parser;

import net.seleucus.wsp.server.exception.ParametrisedMessageException;

public class LogParseException extends ParametrisedMessageException {

    public LogParseException(String message, Object... arguments) {
        super(message, arguments);
    }
}
