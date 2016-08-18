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

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * <ul>
     * <li>This test will fail sporadically.</li>
     * <li>If you remove the `parallel` call for inserts, this test will always succeed.</li>
     * </ul>
     */
    @Test
    public void shouldAddRegionsInParallel() {
        final ApplicationContext applicationContext = new ApplicationContext();
        final RegionService regionService = applicationContext.getRegionService();

        IntStream.rangeClosed(1, 10) //
                .parallel() //
                .forEach(_i -> {
                    regionService.addRegion(new Region(_i, "testRegion" + _i));
                });

        final List<Region> collected = IntStream.rangeClosed(1, 10) //
                .mapToObj(_i -> regionService.getRegion(_i)) //
                .filter(region -> region != null) //
                .collect(toList());

        assertThat(collected.size(), is(10));
    }
}
