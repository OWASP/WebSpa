package net.seleucus.wsp.server.listener;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.WebLogFilterService;
import net.seleucus.wsp.server.request.parser.LogLineParser;
import net.seleucus.wsp.server.request.parser.LogParseException;
import org.apache.commons.io.input.TailerListenerAdapter;

import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;

public class WebServerLogTailCallbackListener extends TailerListenerAdapter {

    private final LogLineParser logParser;
    private final WebLogFilterService webLogFilterService;

    public WebServerLogTailCallbackListener(final LogLineParser logParser, final WebLogFilterService webLogFilterService) {
        this.logParser = logParser;
        this.webLogFilterService = webLogFilterService;
    }

    @Override
    public void handle(final String line) {
        try {
            final LogMessage logMessage = logParser.parse(line);
            if(webLogFilterService.isFiltered(logMessage) == ACCEPT){
                // send the logMessage for further processing
            }
        } catch (LogParseException e) {
            e.printStackTrace();
        }
    }
}