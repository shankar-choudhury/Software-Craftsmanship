package edu.cwru.sxc1782;

import java.util.Objects;

public class IntegerRing implements Ring<Integer>{
    /**
     * Return additive identity 0 of T such that a*0 = 0;
     *
     * @return additive identity 0
     */
    @Override
    public Integer zero() {
        return 0;
    }

    /**
     * Return multiplicative identity 1 of T such that a*1 = a
     *
     * @return multiplicative identity 0
     */
    @Override
    public Integer identity() {
        return 1;
    }

    /**
     * Return sum of x and y
     *
     * @param x first value involved in summation
     * @param y second value involved in summation
     * @return sum of x and y
     */
    @Override
    public Integer sum(Integer x, Integer y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x + y;
    }

    /**
     * Return product ofg x and y
     *
     * @param x first value involved in product
     * @param y second value involved in product
     * @return product of x and y
     */
    @Override
    public Integer product(Integer x, Integer y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x * y;
    }

}
