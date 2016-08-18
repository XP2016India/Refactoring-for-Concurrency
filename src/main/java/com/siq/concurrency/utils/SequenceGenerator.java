package com.siq.concurrency.utils;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator {

    // using atomic reference
    private final AtomicLong value = new AtomicLong(1L);

    public long next() {
        return value.getAndIncrement();
    }

    // simply threadsafe
    // private long value = 1;
    //
    // public synchronized long next() {
    // return value++;
    // }

    // not threadsafe
    // private long value = 1;
    //
    // public long next() {
    // return value++;
    // }
}
