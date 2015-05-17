package net.seleucus.wsp.server.request.filter.filters;

import com.google.common.collect.ImmutableSet;
import net.seleucus.wsp.server.request.filter.WebLogFilter;
import org.junit.Test;

import static net.seleucus.wsp.server.request.domain.HttpMethod.*;
import static net.seleucus.wsp.server.request.domain.LogMessage.LogMessageBuilder.createLogMessage;
import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;
import static net.seleucus.wsp.server.request.filter.FilterResult.DROP;
import static org.fest.assertions.api.Assertions.assertThat;

public class HttpMethodWebLogFilterTest {

    @Test
    public void testParse() throws Exception {
        final WebLogFilter filter = new HttpMethodWebLogFilter(ImmutableSet.of(GET, POST));
        assertThat(filter.parse(createLogMessage().withHttpMethod(GET).build())).isEqualTo(ACCEPT);
        assertThat(filter.parse(createLogMessage().withHttpMethod(POST).build())).isEqualTo(ACCEPT);
        assertThat(filter.parse(createLogMessage().withHttpMethod(PUT).build())).isEqualTo(DROP);
        assertThat(filter.parse(createLogMessage().withHttpMethod(DELETE).build())).isEqualTo(DROP);
    }
}