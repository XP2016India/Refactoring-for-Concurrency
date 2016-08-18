package com.siq.concurrency.webapi;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

public class ApplicationContextTest {

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * <ul>
     * <li>This test will fail sporadically</li>
     * <li>This test depends on {@link ApplicationContext#getDefault()} not being called before this test runs.
     * Otherwise this test will not fail.</li>
     * <li>Other unittests that depend on the Application context should instantiate it directly rather than using the
     * default.</li>
     * </ul>
     */
    @Test
    public void shouldOnlyCreateOneApplicationContext() {
        final List<ApplicationContext> collected = IntStream.rangeClosed(1, 10) //
                .parallel() //
                .mapToObj(_i -> ApplicationContext.getDefault()) //
                .collect(toList());
        assertThat(collected, everyItem(is(sameInstance(collected.get(0)))));
    }
}
