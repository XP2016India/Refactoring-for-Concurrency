package com.siq.concurrency.utils;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.siq.concurrency.utils.LazyInitializer;
import com.siq.concurrency.utils.LazyInitializer.ExpensiveObject;

public class LazyInitializerTest {

    private LazyInitializer lazyInitializer;

    @Before
    public void setup() {
        lazyInitializer = new LazyInitializer();
    }

    @Test
    public void shouldConstructAnInstance() {
        assertThat(lazyInitializer.getExpensiveObject(), is(notNullValue()));
    }

    @Test
    public void shouldOnlyConstruct1Instance() {
        final ExpensiveObject theFirstInstance = lazyInitializer.getExpensiveObject();
        final ExpensiveObject anotherInstance = lazyInitializer.getExpensiveObject();
        assertThat(theFirstInstance, is(sameInstance(anotherInstance)));
    }

    @Test
    public void shouldOnlyConstruct1InstanceEver() {
        final List<ExpensiveObject> collected = IntStream.rangeClosed(1, 10) //
                .mapToObj(_i -> lazyInitializer.getExpensiveObject()) //
                .collect(toList());
        assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    }

    // @Test
    // public void shouldReallyOnlyConstruct1InstanceEver() throws InterruptedException {
    // final int numberOfConcurrentRequests = 1000000;
    // final List<Callable<ExpensiveObject>> callables = IntStream.rangeClosed(1, numberOfConcurrentRequests) //
    // .<Callable<ExpensiveObject>>mapToObj(_i -> lazyInitializer::getExpensiveObject) //
    // .collect(toList());
    // final List<ExpensiveObject> collected = Executors.newFixedThreadPool(3) //
    // .invokeAll(callables).stream() //
    // .map(t -> {
    // try {
    // return t.get();
    // } catch (final InterruptedException e) {
    // throw new IllegalStateException(e);
    // } catch (final ExecutionException e) {
    // throw new IllegalStateException(e);
    // }
    // }) //
    // .collect(toList());
    // assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    // }

    @Test
    public void shouldReallyReallyOnlyConstruct1InstanceEver() {
        final int numberOfConcurrentRequests = 1000000;
        final List<ExpensiveObject> collected = IntStream.rangeClosed(1, numberOfConcurrentRequests) //
                .parallel() //
                .mapToObj(_i -> lazyInitializer.getExpensiveObject()) //
                .collect(toList());
        assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    }
}
