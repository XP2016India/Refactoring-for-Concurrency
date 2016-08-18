package com.siq.concurrency.webapi.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siq.concurrency.webapi.ApplicationContext;
import com.siq.concurrency.webapi.entities.Region;

public class RegionControllerTest {

    private RegionController regionController;

    @Before
    public void setup() {
        regionController = new RegionController();
    }

    @Test
    public void shouldFindARegion() throws JsonParseException, JsonMappingException, IOException {
        final Region expected = new Region(1, "testRegion");
        ApplicationContext.getDefault().getRegionDataStore().insert(expected);

        final String regionAsJson = regionController.findRegion(expected.getId());

        final ObjectMapper objectMapper = new ObjectMapper();
        final Region actual = objectMapper.readValue(regionAsJson, Region.class);
        assertThat(actual, is(expected));
    }
}
