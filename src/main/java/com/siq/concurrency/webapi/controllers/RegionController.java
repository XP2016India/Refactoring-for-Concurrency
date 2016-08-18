package com.siq.concurrency.webapi.controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.Region;
import com.siq.concurrency.webapi.services.RegionService;

public class RegionController {

    private final ApplicationContext applicationContext;

    public RegionController() {
        this(ApplicationContext.getDefault());
    }

    RegionController(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void addRegion(final String regionJson) {
        getRegionService().addRegion(readRegionJson(regionJson));
    }

    public void updateRegion(final String regionJson) {
        getRegionService().updateRegion(readRegionJson(regionJson));
    }

    public String findRegion(final long id) {
        final Region region = getRegionService().getRegion(id);
        try {
            return getObjectMapper().writeValueAsString(region);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("Unable to convert a region to JSON", e);
        }
    }

    public void deleteRegion(final long id) {
        getRegionService().deleteRegion(id);
    }

    private Region readRegionJson(final String regionJson) {
        try {
            return getObjectMapper().readValue(regionJson, Region.class);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Invalid json", e);
        }
    }

    private ObjectMapper getObjectMapper() {
        return applicationContext.getObjectMapper();
    }

    private RegionService getRegionService() {
        return applicationContext.getRegionService();
    }
}
