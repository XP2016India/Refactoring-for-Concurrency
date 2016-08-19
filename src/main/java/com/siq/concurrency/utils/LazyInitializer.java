package com.siq.concurrency.utils;

public class LazyInitializer {

    // using an AtomicReference and the lazy initialization pattern

    // use double-checked locking

    // use synchronization

    // no synchronization
    private ExpensiveObject cachedExpensiveObject;

    ExpensiveObject getExpensiveObject() {
        if (cachedExpensiveObject == null) {
            final ExpensiveObject newExpensiveObject = new ExpensiveObject();
            newExpensiveObject.init();
            cachedExpensiveObject = newExpensiveObject;
        }
        return cachedExpensiveObject;
    }

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
