package com.siq.concurrency.webapi.dataaccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import com.siq.concurrency.utils.SequenceGenerator;
import com.siq.concurrency.webapi.entities.Entity;

abstract class SimpleDataStore<E extends Entity> implements DataStore<E> {

    private final HashMap<Long, E> storage = new HashMap<>();
    private final SequenceGenerator sequenceGenerator = new SequenceGenerator();

    @Override
    public void insert(final E e) {
        if (e.getId() == 0) {
            e.setId(sequenceGenerator.next());
        } else if (storage.containsKey(e.getId())) {
            throw new IllegalArgumentException("Duplicate entry");
        }
        e.setCreatedAt(new Date());
        storage.put(e.getId(), e);
    }

    @Override
    public E update(final E e) {
        final E existingE = storage.get(e.getId());
        if (existingE == null) {
            throw new IllegalArgumentException("Entry not found");
        }
        if (!Objects.equals(e.getUpdatedAt(), existingE.getUpdatedAt())) {
            throw new IllegalArgumentException("Stale data");
        }
        e.setUpdatedAt(new Date());
        return storage.put(e.getId(), e);
    }

    @Override
    public E findBy(final long id) {
        return storage.get(id);
    }

    @Override
    public void delete(final long id) {
        storage.remove(id);
    }
}