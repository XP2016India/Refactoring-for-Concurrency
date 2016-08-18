package com.siq.concurrency.webapi.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Region implements Entity {

    private long id;
    private Date createdAt;
    private Date updatedAt;

    private String name;
    private final ConcurrentSkipListSet<InventoryItem> inventoryItems = new ConcurrentSkipListSet<>();

    public Region() {
    }

    public Region(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public void setCreatedAt(final Date d) {
        createdAt = d;
    }

    @Override
    public void setUpdatedAt(final Date d) {
        updatedAt = d;
    }

    public void setName(final String s) {
        name = s;
    }

    public void addInventoryItem(final InventoryItem inventoryItem) {
        inventoryItems.add(inventoryItem);
    }

    public void removeInventoryItem(final InventoryItem inventoryItem) {
        inventoryItems.remove(inventoryItem);
    }

    public Collection<InventoryItem> getInventoryItems() {
        return new ArrayList<>(inventoryItems);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}