package com.siq.concurrency.webapi.services;

import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.dataaccess.DataStore;
import com.siq.concurrency.webapi.entities.InventoryItem;
import com.siq.concurrency.webapi.entities.Region;

public class RegionService {

    private final ApplicationContext applicationContext;

    public RegionService() {
        this(ApplicationContext.getDefault());
    }

    public RegionService(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addRegion(final Region region) {
        getRegionDataStore().insert(region);
    }

    public Region getRegion(final long id) {
        return getRegionDataStore().findBy(id);
    }

    public void updateRegion(final Region region) {
        getRegionDataStore().update(region);
    }

    public void deleteRegion(final long id) {
        final Region region = getRegionDataStore().findBy(id);
        if (region == null) {
            return;
        }
        final DataStore<InventoryItem> inventoryItemDataStore = getInventoryItemDataStore();
        region.getInventoryItems().stream() //
                .map(InventoryItem::getId) //
                .forEach(inventoryItemDataStore::delete);
        getRegionDataStore().delete(id);
    }

    private DataStore<InventoryItem> getInventoryItemDataStore() {
        return applicationContext.getInventoryItemDataStore();
    }

    private DataStore<Region> getRegionDataStore() {
        return applicationContext.getRegionDataStore();
    }

}
