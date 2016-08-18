package com.siq.concurrency.webapi.controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.services.InventoryItemService;

public class InventoryItemController {

    private final ApplicationContext applicationContext;

    public InventoryItemController() {
        this(ApplicationContext.getDefault());
    }

    InventoryItemController(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addInventoryItem(final String inventoryItemJson) {
        getInventoryItemService().addInventoryItem(readInventoryItemJson(inventoryItemJson));
    }

    public void updateInventoryItem(final String inventoryItemJson) {
        getInventoryItemService().updateInventoryItem(readInventoryItemJson(inventoryItemJson));
    }

    public String findInventoryItem(final long id) {
        final InventoryItem inventoryItem = getInventoryItemService().getInventoryItem(id);
        try {
            return getObjectMapper().writeValueAsString(inventoryItem);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("Unable to convert an inventory item to JSON", e);
        }
    }

    public void deleteInventoryItem(final long id) {
        getInventoryItemService().deleteInventoryItem(id);
    }

    private InventoryItem readInventoryItemJson(final String inventoryItemJson) {
        try {
            return getObjectMapper().readValue(inventoryItemJson, InventoryItem.class);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Invalid json", e);
        }
    }

    private ObjectMapper getObjectMapper() {
        return applicationContext.getObjectMapper();
    }

    private InventoryItemService getInventoryItemService() {
        return applicationContext.getInventoryItemService();
    }
}
