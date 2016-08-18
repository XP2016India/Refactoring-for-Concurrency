package com.siq.concurrency.webapi.dataaccess;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.siq.concurrency.utils.SequenceGenerator;
import com.siq.concurrency.webapi.entities.Entity;

abstract class SimpleDataStore<E extends Entity> implements DataStore<E> {

    private final ConcurrentMap<Long, E> storage = new ConcurrentHashMap<>();
    private final SequenceGenerator sequenceGenerator = new SequenceGenerator();

    @Override
    public void insert(final E e) {
        if (e.getId() == 0) {
            e.setId(sequenceGenerator.next());
        }
        e.setCreatedAt(new Date());
        if (storage.putIfAbsent(e.getId(), e) != null) {
            throw new IllegalArgumentException("Duplicate entry");
        }
    }

    /**
     * <p>
     * <strong>Notes:</strong>
     * <p>
     * This algorithm is optimistic in that it assumes that in the time between getting the existing entity, checking
     * its discriminator, updating the new entity's discriminator and storing it, no other updates will have occurred.
     */
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
        if (storage.replace(e.getId(), existingE, e)) {
            return existingE;
        }
        throw new IllegalArgumentException("Stale data");
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