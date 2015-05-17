package net.seleucus.wsp.server.request.filter.filters;

import net.seleucus.wsp.server.request.domain.LogMessage;
import net.seleucus.wsp.server.request.filter.WebLogFilter;
import org.junit.Test;

import static net.seleucus.wsp.server.request.domain.LogMessage.LogMessageBuilder.createLogMessage;
import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;
import static net.seleucus.wsp.server.request.filter.FilterResult.DROP;
import static org.apache.commons.lang3.RandomStringUtils.randomAscii;
import static org.fest.assertions.api.Assertions.assertThat;

public class UriLengthWebLogFilterTest {

    private static final int MIN_ALLOWED_LENGTH = 10;
    private static final int MAX_ALLOWED_LENGTH = 20;

    private final WebLogFilter filter = new UriLengthWebLogFilter(MIN_ALLOWED_LENGTH, MAX_ALLOWED_LENGTH);

    @Test
    public void shouldReturnAcceptWhereLengthIsBetweenMinAndMax() throws Exception {
        final LogMessage logMessage = createLogMessage().withRequestUri(randomAscii(MAX_ALLOWED_LENGTH - 1)).build();
        assertThat(filter.parse(logMessage)).isEqualTo(ACCEPT);
    }

    @Test
    public void shouldReturnDropWhereLengthIsLessThanMin() throws Exception {
        final LogMessage logMessage = createLogMessage().withRequestUri(randomAscii(MIN_ALLOWED_LENGTH - 1)).build();
        assertThat(filter.parse(logMessage)).isEqualTo(DROP);
    }

    @Test
    public void shouldReturnDropWhereLengthIsGreaterThanMax() throws Exception {
        final LogMessage logMessage = createLogMessage().withRequestUri(randomAscii(MAX_ALLOWED_LENGTH + 1)).build();
        assertThat(filter.parse(logMessage)).isEqualTo(DROP);
    }
}