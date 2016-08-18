package com.siq.concurrency.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private final AtomicReference<T> cachedReference = new AtomicReference<>();
    private final Supplier<T> delegate;

    public Lazy(final Supplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        final T existingT = cachedReference.get();
        if (existingT != null) {
            return existingT;
        }
        final T newT = delegate.get();
        if (cachedReference.compareAndSet(null, newT)) {
            return newT;
        }
        return cachedReference.get();
    }
}