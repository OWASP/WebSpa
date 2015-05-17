package net.seleucus.wsp.server.request.filter;

import com.google.common.collect.ImmutableList;
import net.seleucus.wsp.server.request.domain.LogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;

public class ChainingWebLogFilterService implements WebLogFilterService {

    private final Logger logger = LoggerFactory.getLogger(ChainingWebLogFilterService.class);

    private final List<WebLogFilter> filters;

    public ChainingWebLogFilterService(final List<WebLogFilter> validators) {
        this.filters = ImmutableList.copyOf(validators);
    }

    @Override
    public FilterResult isFiltered(LogMessage logMessage) {
        return filters
            .stream()
            .map(v -> v.parse(logMessage))
            .filter(FilterResult::resultIsDrop)
            .findFirst()
            .orElse(ACCEPT);
    }
}
