package net.seleucus.wsp.server.request.filter.filters;

import org.junit.Test;

import static net.seleucus.wsp.server.request.domain.LogMessage.LogMessageBuilder.createLogMessage;
import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;
import static net.seleucus.wsp.server.request.filter.FilterResult.DROP;
import static org.fest.assertions.api.Assertions.assertThat;

public class Base64EncodedUrlWebLogFilterTest {

    private final Base64EncodedUrlWebLogFilter filter = new Base64EncodedUrlWebLogFilter();

    @Test
    public void shouldAcceptBase64Uri() throws Exception {
        assertThat(filter.parse(createLogMessage().withRequestUri("/TWFuIGlz").build())).isEqualTo(ACCEPT);
    }

    @Test
    public void shouldDropNonBase64Uri() throws Exception {
        assertThat(filter.parse(createLogMessage().withRequestUri("/***").build())).isEqualTo(DROP);
    }

}