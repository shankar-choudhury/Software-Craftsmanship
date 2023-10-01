package edu.cwru.sxc1782;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PolynomialRing<T> implements Ring<Polynomial<T>> {
    private final Ring<T> baseRing;

    private PolynomialRing(Ring<T> ring) {
        baseRing = ring;
    }

    /**
     * Create a new PolynomialRing from a ring
     * @param ring Ring to make a PolynomialRing from
     * @return A new PolynomialRing
     */
    public static final <T> PolynomialRing<T> from(Ring<T> ring) {
        return new PolynomialRing<>(Objects.requireNonNull(ring));
    }

    /**
     * Return additive identity 0 of T such that a+0 = 0;
     *
     * @return additive identity 0
     */
    @Override
    public Polynomial<T> zero() {
        return Polynomial.from(List.of());
    }

    /**
     * Return multiplicative identity a of T such that a*1 = a
     *
     * @return multiplicative identity 0
     */
    @Override
    public Polynomial<T> identity() {
        return Polynomial.from(List.of(baseRing.identity()));
    }

    /**
     * Return sum of x and y
     *
     * @param x first value involved in summation
     * @param y second value involved in summation
     * @return sum of x and y
     */
    @Override
    public Polynomial<T> sum(Polynomial<T> x, Polynomial<T> y) {
        return Objects.requireNonNull(x).plus(y, baseRing);
    }

    /**
     * Return product of x and y
     *
     * @param x first value involved in product
     * @param y second value involved in product
     * @return Product of x and y
     */
    @Override
    public Polynomial<T> product(Polynomial<T> x, Polynomial<T> y) {
        return Objects.requireNonNull(x).times(y, baseRing);
    }
}
