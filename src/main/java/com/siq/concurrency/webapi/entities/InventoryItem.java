package com.siq.concurrency.webapi.entities;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>
 * <strong>Notes:</strong>
 * <p>
 * Because the {@link ConcurrentSkipListSet} requires its contents to implement {@link Comparable}, we must implement
 * this interface as well.
 */
public class InventoryItem implements Entity, Comparable<InventoryItem> {
    private long id;
    private Date createdAt;
    private Date updatedAt;

    private double latitude;
    private double longitude;
    private long regionId;

    public InventoryItem() {
    }

    public InventoryItem(final long id, final double latitude, final double longitude, final long regionId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionId = regionId;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getRegionId() {
        return regionId;
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

    public void setLatitude(final double d) {
        latitude = d;
    }

    public void setLongitude(final double d) {
        longitude = d;
    }

    public void setRegionId(final long id) {
        regionId = id;
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public int compareTo(final InventoryItem other) {
        return Long.valueOf(getId()).compareTo(other.getId());
    }
}