package net.seleucus.wsp.server.request.filter.filters;

import com.google.common.collect.ImmutableSet;
import net.seleucus.wsp.server.request.domain.HttpMethod;
import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.FilterResult;
import net.seleucus.wsp.server.request.filter.WebLogFilter;

import java.util.Set;

public class HttpMethodWebLogFilter implements WebLogFilter {

    private final Set<HttpMethod> allowedHttpMethods;

    public HttpMethodWebLogFilter(Set<HttpMethod> allowedHttpMethods) {
        this.allowedHttpMethods = ImmutableSet.copyOf(allowedHttpMethods);
    }

    @Override
    public FilterResult parse(LogMessage logMessage) {
        return FilterResult.evaluateCondition(allowedHttpMethods.contains(logMessage.getHttpMethod()));
    }
}
