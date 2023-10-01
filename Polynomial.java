package edu.cwru.sxc1782;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Polynomial<T> implements Iterable<T>{
    private final List<T> coefficients;
    private Polynomial(List<T> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Create a new Polynomial from an immutable copy of the input coefficients list
     * @param coefficients List of coefficients to copy, and use copy to construct a new Polynomial
     * @return A new polynomial
     */
    public static final <S> Polynomial<S> from(List<S> coefficients) {
        Objects.requireNonNull(coefficients);
        checkListForNull(coefficients);
        return new Polynomial<>(List.copyOf(coefficients));
    }

    /**
     * Return this Polynomial's coefficients
     * @return This Polynomial's coefficients
     */
    public List<T> getCoefficients() {
        return List.copyOf(coefficients);
    }

    /**
     * Return a string representation of this coefficient
     * @return A string representation of this coefficient
     */
    @Override
    public String toString() {
        return getCoefficients().toString();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    public Iterator<T> iterator() {
        return getCoefficients().iterator();
    }

    /**
     * Returns a ListIterator beginning at index i
     * @param i Index to start iteration at
     *
     * @return A ListIterator beginning at index i
     */
    public ListIterator<T> listIterator(int i) {
        return getCoefficients().listIterator(i);
    }

    /**
     * Return a new Polynomial from the sum of this Polynomial with another
     * @param other Other polynomial to add to this one
     * @param ring Ring to specify addition properties
     * @return A new Polynomial
     */
    public Polynomial<T> plus(Polynomial<T> other, Ring<T> ring) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(ring);
        List<T> result = new LinkedList<>();
        ListIterator<T> thisCoefIt = getCoefficients().listIterator();
        ListIterator<T> otherCoefIt = other.getCoefficients().listIterator();
        int thisCoefSize = getCoefficients().size();
        int otherCoefSize = other.getCoefficients().size();
        int min = Math.min(thisCoefSize, otherCoefSize);

        // Calculate sum of coefficients
        Stream<T> combinedStream = Stream.concat(
                IntStream.range(0, min)
                        .mapToObj(index -> ring.sum(thisCoefIt.next(), otherCoefIt.next())),
                thisCoefSize > otherCoefSize ?
                        IntStream.range(min, thisCoefSize)
                                .mapToObj(index -> thisCoefIt.next()):
                        IntStream.range(min, otherCoefSize)
                                .mapToObj(index -> otherCoefIt.next())
        );

        combinedStream.forEach(result::add);

        return Polynomial.from(result);
    }

    /**
     * Return a new Polynomial from the product of this Polynomial with another
     * @param other Other polynomial to multiply with this one
     * @param ring Ring to specify multiplication properties
     * @return A new Polynomial
     */
    public Polynomial<T> times(Polynomial<T> other, Ring<T> ring) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(ring);

        List<T> thisCoef = getCoefficients();
        List<T> otherCoef = other.getCoefficients();

        List<T> result = initRes(thisCoef.size(), otherCoef.size(), ring);

        findResultCoefs(thisCoef, otherCoef, result, ring);

        return Polynomial.from(result);
    }

    /**
     * Check input list for null element
     * @param list List to check for null element
     */
    private static <T> void checkListForNull(List<T> list) {
        if (list.stream().anyMatch(Objects::isNull))
            throw new NullPointerException("Elements in list cannot be null");
    }

    /**
     * Initialize list with appropriate length and each element having ring's zero identity
     * @param size Size of first list
     * @param otherSize Size of second list
     * @param ring Ring that determines zero identity to add to list
     * @return Initialized list of the correct length filled with zero-identities
     */
    private List<T> initRes(int size, int otherSize, Ring<T> ring) {
        assert ring != null; // Assert (assuming) ring is not null
        List<T> toInit = new ArrayList<>();
        for (int i = 0; i < size + otherSize - 1; i++)
            toInit.add(ring.zero());
        return toInit;
    }

    /**
     * Calculate the product of each coefficient of the result
     * @param coefList1 First list to calculate product from
     * @param coefList2 Second list to calculate product from
     * @param toCalc Result list to store product calculation in
     * @param ring Ring that determines product method
     */
    private void findResultCoefs(List<T> coefList1, List<T> coefList2, List<T> toCalc, Ring<T> ring) {
        assert coefList1 != null;
        assert coefList2 != null;
        assert toCalc != null;
        assert ring != null;
        int i = 0;
        for (T e1 : coefList1) {
            int k = i;
            for (T e2 : coefList2) {
                T res = toCalc.get(k);
                T sum = ring.sum(res, ring.product(e1, e2));
                toCalc.set(k, sum);
                k++;
            }
            i++;
        }
    }

    public static void main(String[] args) {
        Polynomial<Integer> p1 = Polynomial.from(List.of(1, 2, 3));
        Polynomial<Integer> p2 = Polynomial.from(List.of(2, 3));
        Polynomial<Integer> p3 = p1.times(p2, new IntegerRing());
    }

}
