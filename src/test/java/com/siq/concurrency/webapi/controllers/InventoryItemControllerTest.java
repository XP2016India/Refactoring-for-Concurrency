package com.siq.concurrency.webapi.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;
import com.siq.concurrency.webapi.services.InventoryItemService;
import com.siq.concurrency.webapi.services.RegionService;

public class InventoryItemControllerTest {

    private InventoryItemController inventoryItemController;
    private InventoryItemService inventoryItemService;
    private RegionService regionService;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        final ApplicationContext applicationContext = new ApplicationContext();
        inventoryItemService = applicationContext.getInventoryItemService();
        regionService = applicationContext.getRegionService();
        objectMapper = applicationContext.getObjectMapper();

        inventoryItemController = new InventoryItemController(applicationContext);
    }

    private Region createTestRegion() {
        return createTestRegion(1);
    }

    private Region createTestRegion(final long regionId) {
        final Region expected = new Region(regionId, "testRegion");
        regionService.addRegion(expected);
        return expected;
    }

    @Test
    public void shouldAddAnInventoryItem() throws JsonParseException, JsonMappingException, IOException {
        final InventoryItem expected = new InventoryItem(1, 20, 40, createTestRegion().getId());
        inventoryItemController.addInventoryItem(objectMapper.writeValueAsString(expected));

        final InventoryItem actual = inventoryItemService.getInventoryItem(1);
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getLatitude(), is(expected.getLatitude()));
        assertThat(actual.getLongitude(), is(expected.getLongitude()));
    }

    @Test
    public void shouldAssociateInventoryItemsWithARegion()
            throws JsonParseException, JsonMappingException, IOException {
        final InventoryItem item1 = new InventoryItem(1, 20, 40, createTestRegion().getId());
        inventoryItemController.addInventoryItem(objectMapper.writeValueAsString(item1));
        final InventoryItem item2 = new InventoryItem(2, 60, 80, item1.getRegionId());
        inventoryItemController.addInventoryItem(objectMapper.writeValueAsString(item2));

        final Region region = regionService.getRegion(item1.getRegionId());
        assertThat(region.getInventoryItems(), hasItems(inventoryItemService.getInventoryItem(item1.getId()),
                inventoryItemService.getInventoryItem(item2.getId())));
    }

    @Test
    public void shouldFindAnInventoryItem() throws JsonParseException, JsonMappingException, IOException {
        final InventoryItem expected = new InventoryItem(1, 20, 40, createTestRegion().getId());
        inventoryItemService.addInventoryItem(expected);

        final String inventoryItemAsJson = inventoryItemController.findInventoryItem(expected.getId());

        final InventoryItem actual = objectMapper.readValue(inventoryItemAsJson, InventoryItem.class);
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldUpdateAnInventoryItem() throws JsonParseException, JsonMappingException, IOException {
        final InventoryItem expected = new InventoryItem(1, 20, 40, createTestRegion().getId());
        inventoryItemService.addInventoryItem(expected);

        final double newLatitude = 60;
        final double newLongitude = 80;
        expected.setLatitude(newLatitude);
        expected.setLongitude(newLongitude);
        inventoryItemController.updateInventoryItem(objectMapper.writeValueAsString(expected));

        final InventoryItem actual = inventoryItemService.getInventoryItem(1);
        assertThat(actual.getLatitude(), is(newLatitude));
        assertThat(actual.getLongitude(), is(newLongitude));
    }

    @Test
    public void shouldUpdateAnInventoryItemsRegion() throws JsonParseException, JsonMappingException, IOException {
        inventoryItemService.addInventoryItem(new InventoryItem(1, 20, 40, createTestRegion(1).getId()));

        final InventoryItem item = objectMapper.readValue(inventoryItemController.findInventoryItem(1),
                InventoryItem.class);
        item.setRegionId(createTestRegion(2).getId());
        inventoryItemController.updateInventoryItem(objectMapper.writeValueAsString(item));

        assertThat(regionService.getRegion(1).getInventoryItems().size(), is(0));
        assertThat(regionService.getRegion(2).getInventoryItems(),
                hasItems(inventoryItemService.getInventoryItem(item.getId())));
    }

    @Test
    public void shouldDeleteAnInventoryItem() throws JsonParseException, JsonMappingException, IOException {
        final InventoryItem expected = new InventoryItem(1, 20, 40, createTestRegion().getId());
        inventoryItemService.addInventoryItem(expected);

        inventoryItemController.deleteInventoryItem(1);

        final InventoryItem actual = inventoryItemService.getInventoryItem(1);
        assertThat(actual, is(nullValue()));
    }
}
