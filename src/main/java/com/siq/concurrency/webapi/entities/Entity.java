package com.siq.concurrency.webapi.entities;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.siq.concurrency.utils.DateSerializer;

public interface Entity {

    long getId();

    @JsonSerialize(using = DateSerializer.class)
    Date getCreatedAt();

    @JsonSerialize(using = DateSerializer.class)
    Date getUpdatedAt();

    void setId(long id);

    void setCreatedAt(Date d);

    void setUpdatedAt(Date d);
}
