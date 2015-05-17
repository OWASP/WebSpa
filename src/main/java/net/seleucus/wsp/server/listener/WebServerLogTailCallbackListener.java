package net.seleucus.wsp.server.listener;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.WebLogFilterService;
import org.apache.commons.io.input.TailerListenerAdapter;

import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;

public class WebServerLogTailCallbackListener extends TailerListenerAdapter {

    private final WebLogFilterService webLogFilterService;

    public WebServerLogTailCallbackListener(WebLogFilterService webLogFilterService) {
        this.webLogFilterService = webLogFilterService;
    }

    @Override
    public void handle(final String line) {
        final LogMessage logMessage = null;
        if(webLogFilterService.isFiltered(logMessage) == ACCEPT){
            // send the logMessage for further processing
        }
    }
}