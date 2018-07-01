package com.bobin.somemapapp.stub;

import com.bobin.somemapapp.infrastructure.Clock;

public class TestClock implements Clock {
    private long current;

    public TestClock(long current) {
        this.current = current;
    }

    public long getCurrent() {
        return current;
    }

    public void addMillis(long value) {
        current += value;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    @Override
    public long currentTimeInMillis() {
        return current;
    }
}
