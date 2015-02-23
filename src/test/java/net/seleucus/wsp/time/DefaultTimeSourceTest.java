package net.seleucus.wsp.time;

import org.junit.Test;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertTrue;

public class DefaultTimeSourceTest  {

    @Test
    public void testGetTimestamp() throws Exception {
        DefaultTimeSource defaultTimeSource = new DefaultTimeSource();
        assertTrue(defaultTimeSource.getTimestamp() >= System.currentTimeMillis() / 1000);
        assertTrue(defaultTimeSource.getTimestamp() / defaultTimeSource.getTimestamp(MINUTES) >= 1);
    }
}