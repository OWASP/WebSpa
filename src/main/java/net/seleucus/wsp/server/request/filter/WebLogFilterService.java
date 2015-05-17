package net.seleucus.wsp.server.request.filter;

import net.seleucus.wsp.server.request.domain.LogMessage;

public interface WebLogFilterService {

    FilterResult isFiltered(final LogMessage logMessage);
}
