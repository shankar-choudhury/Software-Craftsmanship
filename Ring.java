package edu.cwru.sxc1782;

public interface Ring <T>{

    /**
     * Return additive identity 0 of T such that a+0 = 0;
     *
     * @return additive identity 0
     */
    T zero();

    /**
     * Return multiplicative identity a of T such that a*1 = a
     *
     * @return multiplicative identity 0
     */
    T identity();

    /**
     * Return sum of x and y
     *
     * @param x first value involved in summation
     * @param y second value involved in summation
     * @return sum of x and y
     */
    T sum(T x, T y);

    /**
     * Return product of x and y
     *
     * @param x first value involved in product
     * @param y second value involved in product
     * @return Product of x and y
     */
    T product(T x, T y);

}
