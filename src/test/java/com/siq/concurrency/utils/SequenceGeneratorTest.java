package com.siq.concurrency.utils;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.LongStream;

import org.junit.Test;

import com.siq.concurrency.utils.SequenceGenerator;

public class SequenceGeneratorTest {

    @Test
    public void shouldStartAt1() {
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        assertThat(sequenceGenerator.next(), is(1L));
    }

    @Test
    public void shouldIncrementThePreviousValueBy1() {
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        sequenceGenerator.next();
        assertThat(sequenceGenerator.next(), is(2L));
    }

    @Test
    public void shouldGenerateValuesInOrder() {
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        final List<Long> values = LongStream.rangeClosed(1, 100) //
                .sequential() //
                .map(_i -> sequenceGenerator.next()) //
                .boxed() //
                .collect(toList());
        assertThat(values.toArray(new Long[values.size()]),
                is(LongStream.rangeClosed(1, 100).boxed().toArray(Long[]::new)));
    }

    @Test
    public void shouldNotDuplicateOrSkipValues() {
        final SequenceGenerator sequenceGenerator = new SequenceGenerator();
        final List<Long> values = LongStream.rangeClosed(1, 100) //
                .parallel() //
                .map(_i -> sequenceGenerator.next()) //
                .boxed() //
                .collect(toList());
        assertThat(values, hasItems(LongStream.rangeClosed(1, 100).boxed().toArray(Long[]::new)));
    }
}
