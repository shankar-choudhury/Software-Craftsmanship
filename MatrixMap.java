package edu.cwru.sxc1782;

import java.io.Serial;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MatrixMap<T> {
    private final Map<Indexes, T> matrix;
    private final Indexes size;

    private MatrixMap(Map<Indexes, T> matrix, Indexes size) {
        this.matrix = matrix;
        this.size = size;
    }

    /**
     * Return this matrix map
     * @return This matrix map
     */
    private Map<Indexes, T> getMatrix() {return matrix;}

    /**
     * Return the size of this matrix in terms of the row/column values of the last Indexes of matrix
     * @return Size of this matrix in terms of the row/column values of the last Indexes of matrix
     */
    public Indexes size() {
        return size;
    }

    /**
     * Return a String representation of this matrix
     * @return A string representation of this matrix
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        getMatrix().keySet().stream()
                .sorted()
                .forEach(indexes -> s.append(getMatrix().get(indexes)).append(" "));
        return s.toString();
    }

    /**
     * Return value of this matrix at given Indexes
     * @param indexes Indexes to return value of this matrix from
     * @return Value of this matrix at given Indexes
     */
    public T value(Indexes indexes) {
        return getMatrix().get(Objects.requireNonNull(indexes));
    }

    /**
     * Return value of this matrix at Indexes corresponding to given row and column
     * @param row Row value of this Indexes to return value at this matrix from
     * @param column Column value of this Indexes to return value at this matrix from
     * @return Value of this matrix at Indexes corresponding to given row and column
     */
    public T value(int row, int column) {
        if (row < 0 || column < 0)
            throw new IllegalArgumentException("Indexes coordinates must be at least 0");
        return value(new Indexes(row, column));
    }

    /**
     * Return a new matrix with a specific number of Indexes from given rows and columns,
     * and values at each Indexes from given valueMapper method
     * @param rows Number of rows this matrix will have
     * @param columns Number of columns this matrix will have
     * @param valueMapper Method to map indexes to specific values to put into this matrix
     * @return A new matrix with a specific number of Indexes from given rows and columns
     */
    public static <S> MatrixMap<S> instance(int rows, int columns, Function<Indexes, S> valueMapper) {

        return new MatrixMap<>( Map.copyOf(newMatrix(Indexes.stream(
                InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.ROW, rows),
                InvalidLengthException.requireNonEmpty(InvalidLengthException.Cause.COLUMN, columns))
                , Objects.requireNonNull(valueMapper))), new Indexes(rows, columns));
    }

    /**
     * Return a new matrix with a specific number of Indexes from given Indexes size,
     * and values at each Indexes from given valueMapper method
     * @param size Size of matrix from given last Indexes row/column values
     * @param valueMapper Method to map indexes to specific values to put into this matrix
     * @return A new matrix with a specific number of Indexes from given Indexes size
     */
    public static <S> MatrixMap<S> instance(Indexes size, Function<Indexes, S> valueMapper) {
        return MatrixMap.instance(size.row(), size.column(), valueMapper);
    }

    /**
     * Return a new square matrix of the given size, filled with given value
     * @param size Size of this matrix
     * @param value Value to fill this matrix with
     * @return A new matrix of given size, filled with given value
     */
    public static <S> MatrixMap<S> constant(int size, S value) {
        return MatrixMap.instance(size, size, indexes -> value);
    }

    /**
     * Return a new Square matrix with Indexes on the diagonal holding the identity value, and the rest of the Indexes
     * holding the zero value
     * @param size Dimensions of this square matrix
     * @param zero Zero value to fill non-diagonal Indexes with
     * @param identity Identity value to fill diagonal Indexes with
     * @return A new Square matrix with Indexes on the diagonal holding the identity value, and the rest of the Indexes
     * holding the zero value
     */
    public static <S> MatrixMap<S> identity(int size, S zero, S identity) {
        Objects.requireNonNull(zero);
        Objects.requireNonNull(identity);
        return MatrixMap.instance(size, size, indexes -> indexes.areDiagonal() ? identity : zero);
    }

    /**
     * Return a new matrix from given 2D-array matrix
     * @param matrix 2D-array representation of a matrix
     * @return A new matrix from given 2D-array matrix
     */
    public static <S> MatrixMap<S> from(S[][] matrix) {
        Objects.requireNonNull(matrix);
        return MatrixMap.instance(matrix[0].length - 1, matrix.length - 1, indexes -> indexes.value(matrix));
    }

    /**
     * Return a new MatrixMap representing the sum of this MatrixMap with another MatrixMap
     * @param other Other MatrixMap used in this summation
     * @param plus Sum method used to add values at each indexes
     * @return A new MatrixMap representing the sum of this MatrixMap with another MatrixMap
     */
    public MatrixMap<T> plus(MatrixMap<T> other, BinaryOperator<T> plus) {
        Objects.requireNonNull(plus);
        Indexes size = InconsistentSizeException.requireMatchingSize(this, Objects.requireNonNull(other));
        return MatrixMap.instance(size.row(), size.column(), indexes -> plus.apply(value(indexes), other.value(indexes)));
    }

    /**
     * Return a new MatrixMap representing the product of this MatrixMap with another MatrixMap
     * @param other Other MatrixMap used in this multiplication
     * @param ring Ring to define multiplication method to use for each indexes in either MatrixMap
     * @return A new MatrixMap representing the product of this MatrixMap with another MatrixMap
     */
    public MatrixMap<T> times(MatrixMap<T> other, Ring<T> ring) {
        Indexes otherSize = NonSquareException.requireDiagonal(
                InconsistentSizeException.requireMatchingSize(this, Objects.requireNonNull(other)));
        Objects.requireNonNull(ring);

        return MatrixMap.instance(otherSize.row(), otherSize.column(),
                indexes -> IntStream
                .range(0, otherSize.row() + 1)
                .mapToObj(n -> ring.product(value(indexes.row(), n), other.value(n, indexes.column())))
                .reduce(ring.zero(), ring::sum));
    }

    /**
     * Instantiate a new Map from a stream of Indexes as keys and values generated by valueMapper method
     * @param indexesStream Stream of Indexes to put into this map as keys
     * @param valueMapper Method to generate values from Indexes to put into this map
     * @return A new Map of type HashMap from a stream of Indexes as keys and values generated by valueMapper method
     */
    private static <S> Map<Indexes, S> newMatrix(Stream<Indexes> indexesStream, Function<Indexes, S> valueMapper) {
        assert indexesStream != null;
        assert valueMapper != null;
        return indexesStream
                .collect(Collectors.toMap(Function.identity(), valueMapper));
    }

    /**
     * Instantiate a new Map of type HashMap of a specified size filled with given value that represents a square matrix (rows = columns)
     * @param size Number of Indexes this
     * @param value Value to fill this map with
     * @return A new Map of type HashMap of a specified size filled with given value that represents a square matrix
     */
    private static <S> Map<Indexes, S> newSquareMatrix(int size, S value) {
        assert size > 0;
        assert value != null;
        return newMatrix(Indexes.stream(new Indexes(size, size)), indexes -> value);
    }

    public static class InvalidLengthException extends Exception {
        private enum Cause {
            ROW,
            COLUMN;
        }
        private final Cause cause;
        private final int length;
        @Serial
        private static final long serialVersionUID = 2281997L;

        public InvalidLengthException(Cause cause, int length) {
            this.cause = cause;
            this.length = length;
        }

        public Cause cause() {
            return cause;
        }
        public int length() {
            return length;
        }

        public static int requireNonEmpty(Cause cause, int length) {
            if (length < 1)
                throw new IllegalArgumentException(new InvalidLengthException(cause, length));
            return length;
        }
    }

    public static class InconsistentSizeException extends Exception {
        private final Indexes thisIndex;
        private final Indexes otherIndex;

        public InconsistentSizeException(Indexes thisIndex, Indexes otherIndex) {
            this.thisIndex = thisIndex;
            this.otherIndex = otherIndex;
        }

        public Indexes getThisIndex() {
            return thisIndex;
        }
        public Indexes getOtherIndex() {
            return otherIndex;
        }

        // Should I return a copy of this Matrix's size indexes?
        public static <T> Indexes requireMatchingSize(MatrixMap<T> thisMatrix, MatrixMap<T> otherMatrix) {
            if (!thisMatrix.size().equals(otherMatrix.size()))
                throw new IllegalArgumentException(new InconsistentSizeException(thisMatrix.size(), otherMatrix.size()));
            return otherMatrix.size();
        }
    }

    public static class NonSquareException extends Exception {
        private final Indexes indexes;

        public NonSquareException(Indexes indexes) {
            this.indexes = indexes;
        }

        public Indexes getIndexes() {
            return indexes;
        }

        public static Indexes requireDiagonal(Indexes indexes) {
            if (!indexes.areDiagonal())
                throw new IllegalStateException(new NonSquareException(indexes));
            return indexes;
        }
    }
}
