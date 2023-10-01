package edu.cwru.sxc1782;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Indexes (int row, int column) implements Comparable<Indexes> {

    @Override
    public int compareTo(@NotNull Indexes o) {
        return row() != Objects.requireNonNull(o).row() ? row() - o.row() : column() - o.column();
    }

    /**
     * Return matrix entry corresponding to this index
     * @param matrix Matrix to read entry from
     * @return Matrix entry at this index
     */
    public <S> S value(S[][] matrix) {
        return Objects.requireNonNull(matrix)[row()][column()];
    }

    /**
     * Return MatrixMap entry corresponding to this index
     * @param matrixMap MatrixMap to read entry from
     * @return MatrixMap entry corresponding to this index
     */
    public <S> S value(MatrixMap<S> matrixMap){ return Objects.requireNonNull(matrixMap).value(this);}

    /**
     * Return if this Index lies on diagonal of matrix
     * @return If this Index lies on diagonal of matrix
     */
    public boolean areDiagonal() {
        return row() == column();
    }

    /**
     * Generate all Indexes in sequential order starting at from-Indexes and ending at to-Indexes
     * @param from Starting Indexes to create a sequential stream from
     * @param to Ending Indexes to stream Indexes to
     * @return A stream of Indexes starting at from-Indexes and ending at to-Indexes
     * @precondition from's row and column coordinates must be lower than to's coordinates
     */
    public static Stream<Indexes> stream(Indexes from, Indexes to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.row() >= to.row() || from.column() >= to.column())
            return Stream.empty();
        return IntStream
                .range(from.row(), to.row() + 1)
                .mapToObj(row -> IntStream.range(0, to.column() + 1)
                        .mapToObj(column -> new Indexes(row, column)))
                .flatMap(Function.identity());
    }

    /**
     * Overloaded method of stream that takes the last index to stream sequential Indexes to
     * @param size Last index to stream indexes to
     * @return A stream of Indexes beginning from 0,0 to the last Index
     */
    public static Stream<Indexes> stream(Indexes size) {
        return Indexes.stream(new Indexes(0, 0), size);
    }

    /**
     * Overloaded method of stream that takes the row-column values of the last Index
     * @param rows Row value of last Indexes to stream to
     * @param columns Column value of last Indexes to stream to
     * @return A stream of sequential Indexes from 0,0 to row,column
     */
    public static Stream<Indexes> stream(int rows, int columns) {
        return Indexes.stream(new Indexes(rows, columns));
    }
}
