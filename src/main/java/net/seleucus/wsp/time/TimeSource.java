package net.seleucus.wsp.time;

import java.util.concurrent.TimeUnit;

public interface TimeSource {

    /**
     * Returns the number of seconds (UTC) since the epoch (1970-01-01T00:00:00Z)
     *
     * @return timestamp as a long representing seconds since epoch
     */
    long getTimestamp();

    long getTimestamp(final TimeUnit timeUnit);
}
