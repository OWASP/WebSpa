package net.seleucus.wsp.server.request.filter;

import net.seleucus.wsp.server.request.domain.LogMessage;

public interface WebLogFilter {

    FilterResult parse(LogMessage logMessage);

}
