package edu.cwru.sxc1782;

import java.math.BigInteger;
import java.util.Objects;

public class BigIntegerRing implements Ring<BigInteger>{
    /**
     * Return additive identity 0 of T such that a+0 = 0;
     *
     * @return additive identity 0
     */
    @Override
    public BigInteger zero() {
        return BigInteger.ZERO;
    }

    /**
     * Return multiplicative identity 1 of T such that a*1 = a
     *
     * @return multiplicative identity 0
     */
    @Override
    public BigInteger identity() {
        return BigInteger.ONE;
    }

    /**
     * Return sum of x and y
     *
     * @param x first value involved in summation
     * @param y second value involved in summation
     * @return sum of x and y
     */
    @Override
    public BigInteger sum(BigInteger x, BigInteger y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x.add(y);
    }

    /**
     *return product of x and y
     *
     * @param x first value involved in product
     * @param y second value involved in product
     * @return product of x and y
     */
    @Override
    public BigInteger product(BigInteger x, BigInteger y) {
        Objects.requireNonNull(x);
        Objects.requireNonNull(y);
        return x.multiply(y);
    }

}
