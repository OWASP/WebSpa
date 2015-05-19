package net.seleucus.wsp.server.request.filter.filters;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.FilterResult;
import net.seleucus.wsp.server.request.filter.WebLogFilter;
import org.apache.commons.codec.binary.Base64;

public class Base64EncodedUrlWebLogFilter implements WebLogFilter {

    @Override
    public FilterResult parse(LogMessage logMessage) {
        return FilterResult.evaluateCondition(Base64.isBase64(logMessage.getRequestUri()));
    }
}
