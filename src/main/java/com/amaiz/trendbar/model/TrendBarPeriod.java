package com.amaiz.trendbar.model;

import java.util.concurrent.TimeUnit;

/**
 * There are types of trendbars. Namely - M1 (minutely), H1 (hourly) and
 * D1 (daily).
 */
public enum TrendBarPeriod {
    M1(1, TimeUnit.MINUTES),
    H1(1, TimeUnit.HOURS),
    D1(1, TimeUnit.DAYS);

    private final long time;
    private final TimeUnit timeUnit;

    TrendBarPeriod(long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public long getTime() {
        return time;
    }

    public long getTimeInMillis() {
        return timeUnit.toMillis(time);
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}