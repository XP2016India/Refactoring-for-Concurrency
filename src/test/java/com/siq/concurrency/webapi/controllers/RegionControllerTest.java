package com.siq.concurrency.webapi.controllers;

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

public class RegionControllerTest {

    private RegionController regionController;
    private InventoryItemService inventoryItemService;
    private RegionService regionService;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        final ApplicationContext applicationContext = new ApplicationContext();
        regionService = applicationContext.getRegionService();
        inventoryItemService = applicationContext.getInventoryItemService();
        objectMapper = applicationContext.getObjectMapper();

        regionController = new RegionController(applicationContext);
    }

    @Test
    public void shouldAddARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        regionController.addRegion(objectMapper.writeValueAsString(expected));

        final Region actual = regionService.getRegion(1);
        assertThat(actual.getId(), is(expected.getId()));
        assertThat(actual.getName(), is(expected.getName()));
    }

    @Test
    public void shouldFindARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        regionService.addRegion(expected);

        final String regionAsJson = regionController.findRegion(expected.getId());

        final Region actual = objectMapper.readValue(regionAsJson, Region.class);
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldUpdateARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        regionService.addRegion(expected);

        final String newName = "aDifferentTestRegion";
        expected.setName(newName);
        regionController.updateRegion(objectMapper.writeValueAsString(expected));

        final Region actual = regionService.getRegion(1);
        assertThat(actual.getName(), is(newName));
    }

    @Test
    public void shouldDeleteARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        regionService.addRegion(expected);

        regionController.deleteRegion(1);

        final Region actual = regionService.getRegion(1);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldDeleteAllInventoryItemsInARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        regionService.addRegion(expected);
        inventoryItemService.addInventoryItem(new InventoryItem(1, 20, 40, expected.getId()));
        inventoryItemService.addInventoryItem(new InventoryItem(2, 60, 80, expected.getId()));

        regionController.deleteRegion(1);

        assertThat(inventoryItemService.getInventoryItem(1), is(nullValue()));
        assertThat(inventoryItemService.getInventoryItem(2), is(nullValue()));
    }
}
