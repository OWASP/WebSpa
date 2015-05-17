package net.seleucus.wsp.server.request.filter.filters;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.FilterResult;
import net.seleucus.wsp.server.request.filter.WebLogFilter;
import net.seleucus.wsp.time.TimeSource;

public class TimestampWebLogFilter implements WebLogFilter {

    private final TimeSource timeSource;
    private final long maxAgeSeconds;

    public TimestampWebLogFilter(TimeSource timeSource, final long maxAgeSeconds) {
        this.timeSource = timeSource;
        this.maxAgeSeconds = maxAgeSeconds;
    }

    @Override
    public FilterResult parse(final LogMessage logMessage) {
        final long maxAllowedAgeOfLog = timeSource.getTimestamp() - maxAgeSeconds;
        return FilterResult.evaluateCondition(logMessage.getTimestamp() >= maxAllowedAgeOfLog);
    }
}
