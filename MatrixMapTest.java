package edu.cwru.sxc1782;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MatrixMapTest {

    @Test
    void size() {
        MatrixMap<Integer> m = MatrixMap.constant(2, 3);
        assertEquals(new Indexes(2, 2), m.size());
    }

    @Test
    void value() {
        MatrixMap<Integer> m = MatrixMap.instance(new Indexes(2, 2), new Function<Indexes, Integer>() {
            int i = 0;
            @Override
            public Integer apply(Indexes indexes) {
                if (indexes.column() != 3)
                    return i++;
                return i;
            }
        });
        assertEquals(4, m.value(new Indexes(1, 1)));
        assertThrows(NullPointerException.class, () -> m.value(null));
    }

    @Test
    void value2() {
        MatrixMap<Integer> m = MatrixMap.instance(new Indexes(2, 2), new Function<Indexes, Integer>() {
            int i = 0;
            @Override
            public Integer apply(Indexes indexes) {
                if (indexes.column() != 3)
                    return i++;
                return i;
            }
        });
        assertEquals(6, m.value(2, 0));
    }

    @Test
    void instance() {
        MatrixMap<Integer> m = MatrixMap.instance(2, 2, new Function<Indexes, Integer>() {
            @Override
            public Integer apply(Indexes indexes) {
                return indexes.row() + 1;
            }
        });
        assertEquals("1 1 1 2 2 2 3 3 3 ", m.toString());
        assertThrows(NullPointerException.class, () -> MatrixMap.instance(3, 2, null));
    }

    @Test
    void instance2() {
        MatrixMap<Integer> m = MatrixMap.instance(new Indexes(2, 2), new Function<Indexes, Integer>() {
            int i = 0;
            @Override
            public Integer apply(Indexes indexes) {
                if (indexes.column() != 3)
                    return i++;
                return i;
            }
        });
        assertEquals("0 1 2 3 4 5 6 7 8 ", m.toString());
        assertThrows(NullPointerException.class, () -> MatrixMap.instance(null, new Function<Indexes, Integer>() {
            int i = 0;
            @Override
            public Integer apply(Indexes indexes) {
                if (indexes.column() != 3)
                    return i++;
                return i;
            }
        }));
        assertThrows(NullPointerException.class, () -> MatrixMap.instance(new Indexes(2, 2), null));
    }

    @Test
    void constant() {
        MatrixMap<Integer> m = MatrixMap.constant(2, 3);
        assertEquals("3 3 3 3 3 3 3 3 3 ", m.toString());
        assertThrows(NullPointerException.class, () -> MatrixMap.constant(7, null));
    }

    @Test
    void identity() {
        MatrixMap<Integer> m = MatrixMap.identity(2, 0, 1);
        assertEquals("1 0 0 0 1 0 0 0 1 ", m.toString());
        assertThrows(NullPointerException.class, () -> MatrixMap.identity(2, null, 1));
        assertThrows(NullPointerException.class, () -> MatrixMap.identity(2, 1, null));
    }

    @Test
    void from() {
        Integer[][] arr = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};
        MatrixMap<Integer> m = MatrixMap.from(arr);
        assertEquals("0 1 2 3 4 5 6 7 8 ", m.toString());
        assertThrows(NullPointerException.class, () -> MatrixMap.from(null));
    }

    @Test
    void plus() {
        MatrixMap<BigInteger> m1 = MatrixMap.constant(1, new BigInteger("111111111111111"));
        MatrixMap<BigInteger> m2 = MatrixMap.constant(1, new BigInteger("333333333333333"));
        assertEquals("444444444444444 444444444444444 444444444444444 444444444444444 ", m1.plus(m2, (indexes, otherIndexes) -> new BigIntegerRing().sum(indexes, otherIndexes)).toString());

        List<BigInteger> l1 = List.of(new BigInteger("111111111111111"), new BigInteger("111111111111111"), new BigInteger("111111111111111"));
        List<BigInteger> l2 = List.of(new BigInteger("333333333333333"), new BigInteger("333333333333333"), new BigInteger("333333333333333"));
        Polynomial<BigInteger> p1 = Polynomial.from(l1);
        Polynomial<BigInteger> p2 = Polynomial.from(l2);
        MatrixMap<Polynomial<BigInteger>> m3 = MatrixMap.constant(1, p1);
        MatrixMap<Polynomial<BigInteger>> m4 = MatrixMap.constant(1, p2);
        Ring<Polynomial<BigInteger>> r = PolynomialRing.from(new BigIntegerRing());
        assertEquals("[444444444444444, 444444444444444, 444444444444444] [444444444444444, 444444444444444, 444444444444444] [444444444444444, 444444444444444, 444444444444444] [444444444444444, 444444444444444, 444444444444444] ", m3.plus(m4, r::sum).toString());
    }

    @Test
    void times() {
        MatrixMap<BigInteger> m1 = MatrixMap.constant(1, new BigInteger("111111111111111"));
        MatrixMap<BigInteger> m2 = MatrixMap.constant(1, new BigInteger("333333333333333"));
        assertEquals("74074074074073925925925925926 74074074074073925925925925926 74074074074073925925925925926 74074074074073925925925925926 ",
                m1.times(m2, new BigIntegerRing()).toString());

        List<BigInteger> l1 = List.of(new BigInteger("111111111111111"), new BigInteger("111111111111111"), new BigInteger("111111111111111"));
        List<BigInteger> l2 = List.of(new BigInteger("333333333333333"), new BigInteger("333333333333333"), new BigInteger("333333333333333"));
        Polynomial<BigInteger> p1 = Polynomial.from(l1);
        Polynomial<BigInteger> p2 = Polynomial.from(l2);
        MatrixMap<Polynomial<BigInteger>> m3 = MatrixMap.constant(1, p1);
        MatrixMap<Polynomial<BigInteger>> m4 = MatrixMap.constant(1, p2);
        Ring<Polynomial<BigInteger>> r = PolynomialRing.from(new BigIntegerRing());
        assertEquals("[74074074074073925925925925926, 148148148148147851851851851852, 222222222222221777777777777778, 148148148148147851851851851852, 74074074074073925925925925926] [74074074074073925925925925926, 148148148148147851851851851852, 222222222222221777777777777778, 148148148148147851851851851852, 74074074074073925925925925926] [74074074074073925925925925926, 148148148148147851851851851852, 222222222222221777777777777778, 148148148148147851851851851852, 74074074074073925925925925926] [74074074074073925925925925926, 148148148148147851851851851852, 222222222222221777777777777778, 148148148148147851851851851852, 74074074074073925925925925926] ",
                m3.times(m4, r).toString());
    }

    @Test
    void testExceptionThrown() {
        try {
            MatrixMap<Integer> m = MatrixMap.identity(0, 6, 8);
        }
        catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof MatrixMap.InvalidLengthException);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            MatrixMap<BigInteger> m1 = MatrixMap.constant(1, new BigInteger("111111111111111"));
            MatrixMap<BigInteger> m2 = MatrixMap.constant(2, new BigInteger("333333333333333"));
            MatrixMap<BigInteger> m3 = m1.plus(m2, new BigIntegerRing()::sum);
        }
        catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof MatrixMap.InconsistentSizeException);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            MatrixMap<BigInteger> m1 = MatrixMap.constant(1, new BigInteger("111111111111111"));
            MatrixMap<BigInteger> m2 = MatrixMap.constant(2, new BigInteger("333333333333333"));
            MatrixMap<BigInteger> m3 = m1.times(m2, new BigIntegerRing());
        }
        catch (IllegalArgumentException i) {
            assertTrue(i.getCause() instanceof MatrixMap.InconsistentSizeException);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        try {
            MatrixMap<BigInteger> m1 = MatrixMap.instance(2, 3, indexes -> new BigInteger("111111111111111"));
            MatrixMap<BigInteger> m2 = MatrixMap.constant(2, new BigInteger("333333333333333"));;
            MatrixMap<BigInteger> m3 = m1.times(m2, new BigIntegerRing());
        }
        catch (IllegalStateException i) {
            assertTrue(i.getCause() instanceof MatrixMap.NonSquareException);
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
}
