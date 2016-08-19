package com.siq.concurrency.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class LazyInitializerTest {

    private LazyInitializer lazyInitializer;

    @Before
    public void setup() {
        lazyInitializer = new LazyInitializer();
    }

    // canary
    @Test
    public void shouldConstructAnInstance() {
        assertThat(lazyInitializer.getExpensiveObject(), is(notNullValue()));
    }

    // simple case with 2 calls to get the instance
    @Test
    public void shouldOnlyConstruct1Instance() {
        fail("Satisfy me.");
    }

    // multiple calls to get the instance
    @Test
    public void shouldOnlyConstruct1InstanceEver() {
        fail("Satisfy me.");
    }

    // multiple calls in parallel using the Executors
    @Test
    public void shouldReallyOnlyConstruct1InstanceEver() throws InterruptedException {
        fail("Satisfy me.");
    }

    // multiple calls in parallel using the stream API
    @Test
    public void shouldReallyReallyOnlyConstruct1InstanceEver() {
        fail("Satisfy me.");
    }
}
