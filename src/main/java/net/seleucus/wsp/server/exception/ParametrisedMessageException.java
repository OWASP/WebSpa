package net.seleucus.wsp.server.exception;

import org.slf4j.helpers.MessageFormatter;

public abstract class ParametrisedMessageException extends Exception {

    public ParametrisedMessageException(String message, Object... arguments) {
        super(MessageFormatter.arrayFormat(message, arguments).getMessage());
    }

}
