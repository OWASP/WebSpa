package net.seleucus.wsp.server.request.filter;

import com.google.common.collect.ImmutableList;
import net.seleucus.wsp.server.request.domain.LogMessage;
import org.junit.Test;

import static net.seleucus.wsp.server.request.filter.FilterResult.ACCEPT;
import static net.seleucus.wsp.server.request.filter.FilterResult.DROP;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChainingWebLogValidationServiceTest {

    @Test
    public void shouldAcceptIfSingleFilterReturnsAccept() throws Exception {
        assertThat(createService(createFilter(ACCEPT))).isEqualTo(ACCEPT);
    }

    @Test
    public void shouldDropIfSingleFilterReturnsAccept() throws Exception {
        assertThat(createService(createFilter(DROP))).isEqualTo(DROP);
    }

    @Test
    public void shouldAcceptIfMultipleAndAllFiltersReturnAccept(){
        assertThat(createService(createFilter(ACCEPT), createFilter(ACCEPT))).isEqualTo(ACCEPT);
    }

    @Test
    public void shouldDropIfMultipleAndAllFiltersReturnAccept(){
        assertThat(createService(createFilter(DROP), createFilter(DROP))).isEqualTo(DROP);
    }

    @Test
    public void shouldDropIfAnyFilterReturnsDrop(){
        assertThat(createService(createFilter(ACCEPT), createFilter(DROP))).isEqualTo(DROP);
        assertThat(createService(createFilter(DROP), createFilter(ACCEPT))).isEqualTo(DROP);
    }

    private FilterResult createService(WebLogFilter... filters){
        final WebLogFilterService filterService = new ChainingWebLogFilterService(ImmutableList.copyOf(filters));
        return filterService.isFiltered(mock(LogMessage.class));
    }

    private WebLogFilter createFilter(FilterResult desiredResult){
        final WebLogFilter webLogFilter = mock(WebLogFilter.class);
        when(webLogFilter.parse(any(LogMessage.class))).thenReturn(desiredResult);
        return webLogFilter;
    }
}