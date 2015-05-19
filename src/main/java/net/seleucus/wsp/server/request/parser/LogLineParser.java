package net.seleucus.wsp.server.request.parser;

import net.seleucus.wsp.server.request.domain.LogMessage;

public interface LogLineParser {

    LogMessage parse(final String line) throws LogParseException;
}
