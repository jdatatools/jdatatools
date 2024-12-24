package com.ainouss.jdatatools.batch.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Wrapper holding a collection of data, usually the chunk is a partition  between {@link #start} and {@link #end}
 * of bigger chunk
 *
 * @param <R> type
 */
@Data
@NoArgsConstructor
public class Chunk<R> {

    private Collection<R> data = new ArrayList<>();

    private Integer start = 0;

    private Integer end;

    private String error;

    public Chunk(Collection<R> data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean isNotEmpty() {
        return !data.isEmpty();
    }

    public int getSize() {
        return data.size();
    }

    public static <T> Chunk<T> of(T element) {
        return new Chunk<>(List.of(element), 0, 1);
    }

    public static <T> Chunk<T> of(Collection<T> elements) {
        return new Chunk<>(elements, 0, elements.size());
    }
}
