package com.bobin.somemapapp.infrastructure;

public class SystemClock implements Clock {
    @Override
    public long currentTimeInMillis() {
        return System.currentTimeMillis();
    }
}
