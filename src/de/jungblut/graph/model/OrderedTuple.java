package de.jungblut.graph.model;

import java.util.Objects;

/**
 * A simple Tuple with a single type that ensures the bigger item is always coming first.
 *
 * @param <T> any Comparable type.
 */
public class OrderedTuple<T extends Comparable<T>> {

    private final T first;
    private final T second;

    public OrderedTuple(T first, T second) {
        if (first.compareTo(second) >= 0) {
            this.first = first;
            this.second = second;
        } else {
            this.first = second;
            this.second = first;
        }
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedTuple<?> that = (OrderedTuple<?>) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "OrderedTuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
