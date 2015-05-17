package net.seleucus.wsp.server.request.filter.filters;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.FilterResult;
import net.seleucus.wsp.server.request.filter.WebLogFilter;
import org.apache.commons.lang3.StringUtils;

public class UriLengthWebLogFilter implements WebLogFilter {

    private final int minLength;
    private final int maxLength;

    public UriLengthWebLogFilter(final int minLength, final int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public FilterResult parse(final LogMessage logMessage) {
        final int length = StringUtils.length(logMessage.getRequestUri());
        return FilterResult.evaluateCondition(length >= minLength && length <= maxLength);
    }
}
