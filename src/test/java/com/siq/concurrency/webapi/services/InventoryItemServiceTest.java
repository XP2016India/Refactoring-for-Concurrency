package com.siq.concurrency.webapi.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;

public class InventoryItemServiceTest {

    private ApplicationContext applicationContext;
    private RegionService regionService;
    private InventoryItemService inventoryItemService;

    @Before
    public void setup() {
        applicationContext = new ApplicationContext();
        regionService = applicationContext.getRegionService();
        inventoryItemService = applicationContext.getInventoryItemService();
    }

    @Test
    public void shouldAddInventoryItemsToARegionInParallel() {
        regionService.addRegion(new Region(1, "testRegion"));

        IntStream.rangeClosed(1, 10) //
                .parallel() //
                .forEach(i -> {
                    inventoryItemService.addInventoryItem(new InventoryItem(i, i + 100, i + 200, 1));
                });

        final Region region = regionService.getRegion(1);

        assertThat(region.getInventoryItems().size(), is(10));
    }

    @Test
    public void shouldBeAbleToAddInventoryItemsAndRetrieveThemInParallel() {
        final Region region = new Region(1, "testRegion");
        regionService.addRegion(region);

        IntStream.rangeClosed(1, 10) //
                .parallel() //
                .forEach(i -> {
                    inventoryItemService.addInventoryItem(new InventoryItem(i, i + 100, i + 200, 1));
                    region.getInventoryItems();
                });

        // there should be no exceptions
    }
}
