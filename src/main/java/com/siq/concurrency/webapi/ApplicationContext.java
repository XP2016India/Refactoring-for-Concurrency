package com.siq.concurrency.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.webapi.dataaccess.DataStore;
import com.siq.concurrency.webapi.dataaccess.InventoryItemDataStore;
import com.siq.concurrency.webapi.dataaccess.RegionDataStore;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;
import com.siq.concurrency.webapi.services.InventoryItemService;
import com.siq.concurrency.webapi.services.RegionService;

public class ApplicationContext {

    private static ApplicationContext INSTANCE;
    private DataStore<InventoryItem> inventoryItemDataStore;
    private DataStore<Region> regionDataStore;
    private InventoryItemService inventoryItemService;
    private RegionService regionService;
    private ObjectMapper objectMapper;

    public static ApplicationContext getDefault() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationContext();
        }
        return INSTANCE;
    }

    public DataStore<InventoryItem> getInventoryItemDataStore() {
        if (inventoryItemDataStore == null) {
            inventoryItemDataStore = new InventoryItemDataStore();
        }
        return inventoryItemDataStore;
    }

    public DataStore<Region> getRegionDataStore() {
        if (regionDataStore == null) {
            regionDataStore = new RegionDataStore();
        }
        return regionDataStore;
    }

    public InventoryItemService getInventoryItemService() {
        if (inventoryItemService == null) {
            inventoryItemService = new InventoryItemService(this);
        }
        return inventoryItemService;
    }

    public RegionService getRegionService() {
        if (regionService == null) {
            regionService = new RegionService(this);
        }
        return regionService;
    }

    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            final ObjectMapper o = new ObjectMapper();
            objectMapper = o;
        }
        return objectMapper;
    }
}
