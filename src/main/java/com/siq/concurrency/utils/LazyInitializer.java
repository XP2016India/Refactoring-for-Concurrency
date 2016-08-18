package com.siq.concurrency.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LazyInitializer {

    // using a Lazy Delegating class: 0.170
    static class Lazy<T> implements Supplier<T> {
        private final AtomicReference<T> cachedReference = new AtomicReference<>();
        private final Supplier<T> delegate;

        Lazy(final Supplier<T> delegate) {
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

    private final Supplier<ExpensiveObject> lazyExpensiveObject = new Lazy<>(() -> {
        final ExpensiveObject newExpensiveObject = new ExpensiveObject();
        newExpensiveObject.init();
        return newExpensiveObject;
    });

    ExpensiveObject getExpensiveObject() {
        return lazyExpensiveObject.get();
    }

    // using an AtomicReference: 0.170
    // private final AtomicReference<ExpensiveObject> cachedExpensiveObjectReference = new AtomicReference<>();
    //
    // ExpensiveObject getExpensiveObject() {
    // final ExpensiveObject existingExpensiveObject = cachedExpensiveObjectReference.get();
    // if (existingExpensiveObject != null) {
    // return existingExpensiveObject;
    // }
    // final ExpensiveObject newExpensiveObject = new ExpensiveObject();
    // newExpensiveObject.init();
    // if (cachedExpensiveObjectReference.compareAndSet(null, newExpensiveObject)) {
    // return newExpensiveObject;
    // }
    // return cachedExpensiveObjectReference.get();
    // }

    // double-checked locking: 0.170
    // private volatile ExpensiveObject cachedExpensiveObject;
    //
    // ExpensiveObject getExpensiveObject() {
    // if (cachedExpensiveObject == null) {
    // final ExpensiveObject newExpensiveObject = new ExpensiveObject();
    // newExpensiveObject.init();
    // synchronized (this) {
    // if (cachedExpensiveObject == null) {
    // cachedExpensiveObject = newExpensiveObject;
    // }
    // }
    // }
    // return cachedExpensiveObject;
    // }

    // simplest synchronization approach: 0.227
    // private ExpensiveObject cachedExpensiveObject;
    //
    // synchronized ExpensiveObject getExpensiveObject() {
    // if (cachedExpensiveObject == null) {
    // final ExpensiveObject newExpensiveObject = new ExpensiveObject();
    // newExpensiveObject.init();
    // cachedExpensiveObject = newExpensiveObject;
    // }
    // return cachedExpensiveObject;
    // }

    static class ExpensiveObject {
        void init() {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
