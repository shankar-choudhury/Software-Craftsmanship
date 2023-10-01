package edu.cwru.sxc1782;

import java.util.Objects;

public class DoubleRing implements Ring<Double>{

    /**
     * Return additive identity 0 of T such that a+0 = 0;
     *
     * @return additive identity 0
     */
    @Override
    public Double zero() {
        return 0.0;
    }

    /**
     * Return multiplicative identity 1 of T such that a*1 = a
     *
     * @return multiplicative identity 0
     */
    @Override
    public Double identity() {
        return 1.0;
    }

    /**
     * Return sum of x and y
     *
     * @param x first value involved in summation
     * @param y second value involved in summation
     * @return sum of x and y
     */
    @Override
    public Double sum(Double x, Double y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x + y;
    }

    /**
     * Return product of x and y
     *
     * @param x first value involved in product
     * @param y second value involved in product
     * @return Product of x and y
     */
    @Override
    public Double product(Double x, Double y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x * y;
    }

}
