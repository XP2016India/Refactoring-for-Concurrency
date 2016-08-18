package com.siq.concurrency.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateSerializer extends StdSerializer<Date> {

    private static final long serialVersionUID = 1L;

    private final DateFormat dateFormat;

    public DateSerializer() {
        this(Date.class);
    }

    protected DateSerializer(final Class<Date> t) {
        super(t);
        final DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        d.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat = d;
    }

    @Override
    public void serialize(final Date value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeString(dateFormat.format(value));
    }

}
