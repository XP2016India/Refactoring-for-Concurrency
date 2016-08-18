package com.siq.concurrency.webapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.utils.Lazy;
import com.siq.concurrency.webapi.dataaccess.DataStore;
import com.siq.concurrency.webapi.dataaccess.InventoryItemDataStore;
import com.siq.concurrency.webapi.dataaccess.RegionDataStore;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;
import com.siq.concurrency.webapi.services.InventoryItemService;
import com.siq.concurrency.webapi.services.RegionService;

public class ApplicationContext {

    private final Lazy<DataStore<InventoryItem>> inventoryItemDataStore;
    private final Lazy<DataStore<Region>> regionDataStore;
    private final Lazy<InventoryItemService> inventoryItemService;
    private final Lazy<RegionService> regionService;
    private final Lazy<ObjectMapper> objectMapper;

    public ApplicationContext() {
        inventoryItemDataStore = new Lazy<>(InventoryItemDataStore::new);
        regionDataStore = new Lazy<>(RegionDataStore::new);
        inventoryItemService = new Lazy<>(InventoryItemService::new);
        regionService = new Lazy<>(RegionService::new);
        objectMapper = new Lazy<>(ObjectMapper::new);
    }

    public static ApplicationContext getDefault() {
        return DefaultHolder.INSTANCE;
    }

    public DataStore<InventoryItem> getInventoryItemDataStore() {
        return inventoryItemDataStore.get();
    }

    public DataStore<Region> getRegionDataStore() {
        return regionDataStore.get();
    }

    public InventoryItemService getInventoryItemService() {
        return inventoryItemService.get();
    }

    public RegionService getRegionService() {
        return regionService.get();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper.get();
    }

    private static final class DefaultHolder {
        private static final ApplicationContext INSTANCE;

        /**
         * When using the lazy singleton pattern, consider the following situation.... When an error occurs, this class
         * is not valid and any calls to get the instance will throw a java.lang.NoClassDefFoundError
         */
        static {
            final ApplicationContext context = new ApplicationContext();
            // do some initialization that causes an exception
            // if (new Date().getTime() % 2 == 0) {
            // throw new IllegalStateException();
            // }
            INSTANCE = context;
        }
    }
}
