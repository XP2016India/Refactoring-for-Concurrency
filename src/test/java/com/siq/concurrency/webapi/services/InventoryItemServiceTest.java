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

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * <ul>
     * <li>This test will fail sporadically.</li>
     * <li>If you remove the `parallel` call for inserts, this test will always succeed.</li>
     * <li>By now, we already know that not all InventoryItems will be added to the data store because of a different
     * bug. What other implications are there in this case?</li>
     * </ul>
     */
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

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * <ul>
     * <li>This test will fail sporadically with a ConcurrentModificationException.</li>
     * <li>If you remove the `parallel` call for inserts/reads, this test will always succeed.</li>
     * </ul>
     */
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
