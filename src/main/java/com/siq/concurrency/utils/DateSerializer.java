package com.siq.concurrency.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateSerializer extends StdSerializer<Date> {

    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * A few considerations when using ThreadLocals:
     * <ul>
     * <li>If you are not re-using threads, you might as well use a new Object every time.</li>
     * <li>In a traditional web context or other thread-per-request model where your application does not spawn other
     * threads, the use of ThreadLocals in a case like this is ok but a bit icky since this component is aware of the
     * context in which it is used.</li>
     * </ul>
     * <p>
     * I am using a ThreadLocal here for the sake of example. There are better ways to do this, i.e.,
     * <ul>
     * <li>Use a {@link ZonedDateTime} and call {@link ZonedDateTime#format(DateTimeFormatter)}</li>
     * <li>Use {@link DateTimeFormatter#ofPattern(String)}</li>
     * <li>Use a {@link JsonFormat} annotation for the dates</li>
     * </ul>
     */
    private final ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(() -> {
        final DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        d.setTimeZone(TimeZone.getTimeZone("UTC"));
        return d;
    });

    public DateSerializer() {
        this(Date.class);
    }

    protected DateSerializer(final Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(final Date value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeString(dateFormat.get().format(value));
    }
}
