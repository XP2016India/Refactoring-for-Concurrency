package com.siq.concurrency.webapi.services;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.dataaccess.DataStore;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;

public class InventoryItemService {

    private final ApplicationContext applicationContext;

    public InventoryItemService() {
        this(ApplicationContext.getDefault());
    }

    public InventoryItemService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addInventoryItem(final InventoryItem inventoryItem) {
        getInventoryItemDataStore().insert(inventoryItem);
        final Region region = getRegionDataStore().findBy(inventoryItem.getRegionId());
        if (region == null) {
            getInventoryItemDataStore().delete(inventoryItem.getId());
            throw new IllegalArgumentException("Unknown region");
        }
        region.addInventoryItem(inventoryItem);
    }

    public InventoryItem getInventoryItem(final long id) {
        return getInventoryItemDataStore().findBy(id);
    }

    public void updateInventoryItem(final InventoryItem inventoryItem) {
        final Region newRegion = getRegionDataStore().findBy(inventoryItem.getRegionId());
        if (newRegion == null) {
            throw new IllegalArgumentException("Unknown region");
        }
        final InventoryItem priorValue = getInventoryItemDataStore().update(inventoryItem);
        if (priorValue.getRegionId() != inventoryItem.getRegionId()) {
            final Region priorRegion = getRegionDataStore().findBy(priorValue.getRegionId());
            priorRegion.removeInventoryItem(priorValue);
            newRegion.addInventoryItem(inventoryItem);
        }
    }

    public void deleteInventoryItem(final long id) {
        getInventoryItemDataStore().delete(id);
    }

    private DataStore<Region> getRegionDataStore() {
        return applicationContext.getRegionDataStore();
    }

    private DataStore<InventoryItem> getInventoryItemDataStore() {
        return applicationContext.getInventoryItemDataStore();
    }

}
