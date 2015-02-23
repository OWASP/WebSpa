package net.seleucus.wsp.time;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FixedTimeSource implements TimeSource {

    private final long timestampInSeconds;

    public FixedTimeSource(long timestampInSeconds) {
        this.timestampInSeconds = timestampInSeconds;
    }

    /**
     * Returns a fixed number of seconds since the epoch
     *
     * @return timestamp
     */
    @Override
    public long getTimestamp() {
        return timestampInSeconds;
    }

    @Override
    public long getTimestamp(TimeUnit timeUnit) {
        return timeUnit.convert(timestampInSeconds, SECONDS);
    }
}
