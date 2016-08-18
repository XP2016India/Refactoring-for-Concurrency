package com.siq.concurrency.webapi.services;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.Region;

public class RegionServiceTest {

    @Test
    public void shouldAddRegionsInParallel() {
        final ApplicationContext applicationContext = new ApplicationContext();
        final RegionService regionService = applicationContext.getRegionService();

        IntStream.rangeClosed(1, 10) //
                .parallel() //
                .forEach(i -> {
                    regionService.addRegion(new Region(i, "testRegion" + i));
                });

        final List<Region> collected = IntStream.rangeClosed(1, 10) //
                .mapToObj(i -> regionService.getRegion(i)) //
                .filter(region -> region != null) //
                .collect(toList());

        assertThat(collected.size(), is(10));
    }
}
