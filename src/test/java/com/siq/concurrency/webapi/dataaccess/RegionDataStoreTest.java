package com.siq.concurrency.webapi.dataaccess;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.Region;

public class RegionDataStoreTest {

    @Test
    public void shouldAddRegionsInParallel() {
        final ApplicationContext applicationContext = new ApplicationContext();
        final DataStore<Region> regionDataStore = applicationContext.getRegionDataStore();

        IntStream.rangeClosed(1, 10) //
                .parallel() //
                .forEach(_i -> {
                    regionDataStore.insert(new Region(_i, "testRegion" + _i));
                });

        final List<Region> collected = IntStream.rangeClosed(1, 10) //
                .mapToObj(_i -> regionDataStore.findBy(_i)) //
                .filter(region -> region != null) //
                .collect(toList());

        assertThat(collected.size(), is(10));
    }
}
