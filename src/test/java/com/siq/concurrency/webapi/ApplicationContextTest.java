package com.siq.concurrency.webapi;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import com.siq.concurrency.webapi.dataaccess.DataStore;
import com.siq.concurrency.webapi.entities.Region;

public class ApplicationContextTest {

    @Test
    public void shouldOnlyCreateOneApplicationContext() {
        final List<ApplicationContext> collected = IntStream.rangeClosed(1, 10) //
                .parallel() //
                .mapToObj(_i -> ApplicationContext.getDefault()) //
                .collect(toList());
        assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    }

    @Test
    public void shouldOnlyCreateOneRegionDataStore() {
        final ApplicationContext applicationContext = new ApplicationContext();

        final List<DataStore<Region>> collected = IntStream.rangeClosed(1, 10) //
                .parallel() //
                .mapToObj(_i -> applicationContext.getRegionDataStore()) //
                .collect(toList());
        assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    }
}
