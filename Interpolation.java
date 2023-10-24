import java.util.*;

public class Interpolation {

    /**
     * Return a list of coefficients for a polynomial that evaluates to zero at specified values
     * @param zeroList List of distinct values in ascending order that the polynomial should evaluate to zero at
     * @return A list of coefficients for a polynomial that evaluates to zero at specified values
     */
    public static List<Integer> polynomialCoefficients(List<Integer> zeroList) {
        if (containsNullValues(Objects.requireNonNull(zeroList)))
            throw new IllegalArgumentException("List cannot contain zero values");
        zeroList.stream().reduce((element, nextElement) -> {
            if (element.compareTo(nextElement) >= 0)
                throw new IllegalArgumentException("Elements must be distinct and in ascending order");
            return nextElement;
        });

        var zeroIt = zeroList.iterator();
        var coefficients = newBinomial(zeroIt.next());

        while (zeroIt.hasNext())
            coefficients = product(coefficients, newBinomial(zeroIt.next()));

        return coefficients;
    }

    /**
     * Calculate and return a list of coefficients representing the product of a growing polynomial with a binomial
     * @param coefficients Growing polynomial that will be multiplied with the next binomial
     * @param nextBinomial Binomial that evaluates to zero at next value
     * @return A list of coefficients representing the product of a growing polynomial with a binomial
     */
    private static List<Integer> product(List<Integer> coefficients, List<Integer> nextBinomial) {
        assertNotNull(coefficients, nextBinomial);
        assert coefficients.stream().noneMatch(Objects::isNull);
        assert nextBinomial.stream().noneMatch(Objects::isNull);
        var results = new ArrayList<>(Collections.nCopies(coefficients.size() + 1, 0));
        var resultsIt = results.listIterator();

        for (Integer coef : coefficients) {
            for (Integer otherCoef : nextBinomial)
                resultsIt.set(coef * otherCoef + resultsIt.next());
            resultsIt.previous();
        }

        return results;
    }

    /**
     * Return a list of values representing a binomial that evaluates to zero at given value
     * @param zeroValue Value at which this binomial evaluates to zero
     * @return A list of values representing a binomial that evaluates to zero at given value
     */
    private static List<Integer> newBinomial(Integer zeroValue) {
        assert zeroValue != null;
        return Arrays.asList(1, -zeroValue);
    }

    /**
     * Check that list does not contain any null values
     * @param toCheck List to check for null values
     * @return Same list as parameter provided that it contains no null values
     */
    private static boolean containsNullValues(List<Integer> toCheck) {
        assert toCheck != null;
        return toCheck.stream().anyMatch(Objects::isNull);
    }

    /**
     * Assert method used for asserting arguments are not null
     * @param args Arguments to check if equivalent to null values
     */
    private static void assertNotNull(Object... args) {
        assert Arrays.stream(args).noneMatch(Objects::isNull);
    }


}
