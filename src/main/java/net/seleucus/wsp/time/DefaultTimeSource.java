package net.seleucus.wsp.time;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DefaultTimeSource implements TimeSource {

    /**
     * Uses the system's current time (which is expected to be correct)
     *
     * @return the number of seconds since the epoch
     */
    @Override
    public long getTimestamp() {
        return this.getTimestamp(SECONDS);
    }

    @Override
    public long getTimestamp(TimeUnit timeUnit) {
        return timeUnit.convert(System.currentTimeMillis(), MILLISECONDS);
    }
}
