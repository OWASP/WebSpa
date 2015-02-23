package net.seleucus.wsp.time;

import org.junit.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class FixedTimeSourceTest {

    public static final long fixedPoint = System.currentTimeMillis() / 1000;

    private final FixedTimeSource fixedTimeSource = new FixedTimeSource(fixedPoint);

    @Test
    public void testGetTimestamp() throws Exception {
        long timestampSeconds = fixedTimeSource.getTimestamp();
        long timestampMilliseconds = fixedTimeSource.getTimestamp(MILLISECONDS);
        long timestampMinutes = fixedTimeSource.getTimestamp(MINUTES);

        assertEquals(timestampSeconds, fixedPoint);
        assertTrue(timestampSeconds == timestampMilliseconds / 1000);
        assertTrue(timestampMinutes == timestampSeconds / 60);
    }

}