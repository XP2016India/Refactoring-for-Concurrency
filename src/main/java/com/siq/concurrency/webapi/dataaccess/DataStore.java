package com.siq.concurrency.webapi.dataaccess;

import com.siq.concurrency.webapi.entities.Entity;

public interface DataStore<E extends Entity> {
    public void insert(final E e);

    public E update(final E e);

    public E findBy(final long id);

    public void delete(final long id);
}
