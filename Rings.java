package edu.cwru.sxc1782;

import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

public final class Rings {
    /**
     * Return a reduced value of a list
     *
     * @param args List of elements to reduce
     * @param zero Initializing value for reduce
     * @param accumulator Method applied to reduce with every element of args
     * @return A reduced value of a list
     */
    public static<T> T reduce(List<T> args, T zero, BinaryOperator<T> accumulator) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(zero);
        Objects.requireNonNull(accumulator);

        return args
                .stream()
                .reduce(accumulator::apply)  // .reduce((x, y) -> accumulator.apply(x, y))
                .orElse(zero);               // When something is described as a functional interface like BinaryOperator
                                             // or BiFunction, try using syntax like above
    }

    /**
     * Add all elements of args according to implementation of sum in ring
     *
     * @param args List of values for ring to operate on
     * @param ring Ring that will operate on args values
     * @return Final value accumulated
     */
    public static<T> T sum (List<T> args, Ring<T> ring) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(ring);

        return reduce(args, ring.zero(), ring::sum);
    }

    /**
     * Multiply all elements of args according to implementation of product in ring
     *
     * @param args List of values for ring to operate on
     * @param ring Ring that will operate on args values
     * @return Final value accumulated
     */
    public static<T> T product(List<T> args, Ring<T> ring) {
        Objects.requireNonNull(args);
        Objects.requireNonNull(ring);

        return reduce(args, ring.identity(), ring::product);
    }

}
