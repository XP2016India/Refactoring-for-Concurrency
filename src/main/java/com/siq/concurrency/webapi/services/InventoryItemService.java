package com.siq.concurrency.webapi.services;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;

public class InventoryItemService {

    private final ApplicationContext applicationContext;

    public InventoryItemService() {
        this(ApplicationContext.getDefault());
    }

    InventoryItemService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addInventoryItem(final InventoryItem inventoryItem) {
        applicationContext.getInventoryItemDataStore().insert(inventoryItem);
        final Region region = applicationContext.getRegionDataStore().findBy(inventoryItem.getRegionId());
        if (region == null) {
            applicationContext.getInventoryItemDataStore().delete(inventoryItem.getId());
            throw new IllegalArgumentException("Unknown region");
        }
        region.addInventoryItem(inventoryItem);
    }

    public InventoryItem getInventoryItem(final long id) {
        return applicationContext.getInventoryItemDataStore().findBy(id);
    }

    public void updateInventoryItem(final InventoryItem inventoryItem) {
        final Region newRegion = applicationContext.getRegionDataStore().findBy(inventoryItem.getRegionId());
        if (newRegion == null) {
            throw new IllegalArgumentException("Unknown region");
        }
        final InventoryItem priorValue = applicationContext.getInventoryItemDataStore().update(inventoryItem);
        if (priorValue.getRegionId() != inventoryItem.getRegionId()) {
            final Region priorRegion = applicationContext.getRegionDataStore().findBy(priorValue.getRegionId());
            priorRegion.removeInventoryItem(priorValue);
            newRegion.addInventoryItem(inventoryItem);
        }
    }

    public void deleteInventoryItem(final long id) {
        applicationContext.getInventoryItemDataStore().delete(id);
    }
}
