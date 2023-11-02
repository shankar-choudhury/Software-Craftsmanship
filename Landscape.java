package Geology;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Landscape {

    public enum Operation { RAISE, DEPRESS, HILL, VALLEY, FOO }

    public record Modification(int x1, int x2, Operation operation) {}

    private static final Logger logger = Logger.getLogger(Landscape.class.getName()); // Changed to Landscape so logger can be logged for this landscape's log
    private final Map<Integer, Integer> landscape = new HashMap<>();

    private Map<Integer, Integer> get() {
        return landscape;
    }

    public void modify(int x1, int x2, Operation operation) {
        Optional.of(x1 <= x2) // Switched ">" to "<=" to ensure correct boolean value is being evaluated
                .filter(bool -> !bool)
                .ifPresent(bool -> logger.log(Level.WARNING, "x1 cannot be greater than x2; returning early"));
        Objects.requireNonNull(operation);

        switch (operation) { // Refactored with enhanced switch statement
            case RAISE -> IntStream.range(x1, x2 + 1).forEach(num -> {
                landscape.put(num, landscape.getOrDefault(num, 0) + 1);
            });
            case DEPRESS -> IntStream.range(x1, x2 + 1).forEach(num -> {
                landscape.put(num, landscape.getOrDefault(num, 0) - 1);
            });
            case HILL -> createHillOrValley(x1, x2, Operation.HILL);
            case VALLEY -> createHillOrValley(x1, x2, Operation.VALLEY);
            default -> logger.log(Level.WARNING, "Unknown operation, skipping");
        }
    }

    public static void main(String[] args) {
        Landscape l = new Landscape();
        for ( String command : args) {
            switch (command) {
                case "raise" -> l.modify(0, 6, Operation.RAISE);
                case "depress" -> l.modify(0, 6, Operation.DEPRESS);
                case "hill" -> l.modify(1, 5, Operation.HILL);
                case "valley" -> l.modify(2, 4, Operation.VALLEY);
                default -> System.out.println("Unknown command");
            }
        }
        System.out.println(l.get());
    }

    private void createHillOrValley(int x1, int x2, Operation o) { // Added general method to remove duplicate code for createHill and createValley
        assert x1 < x2;
        assert o != null;
        int leftPtr = x1;
        int rightPtr = x2;
        int amount = initAmt(o);
        while (leftPtr <= rightPtr) {
            landscape.put(leftPtr, landscape.getOrDefault(leftPtr, 0) + amount);
            if (rightPtr != leftPtr)
                landscape.put(rightPtr, landscape.getOrDefault(rightPtr, 0) + amount);
            leftPtr++;
            rightPtr--;
            amount = adjust(amount, o);
        }
    }

    private int initAmt(Operation o) {
        assert o != null;
        return switch (o) {
            case HILL -> 1;
            case VALLEY -> -1;
            default -> throw new AssertionError("Cannot use this operation");
        };
    }

    private int adjust(int value, Operation o) {
        assert o != null;
        return switch (o) {
            case HILL -> ++value;
            case VALLEY -> --value;
            default -> throw new AssertionError("Cannot use this operation");
        };
    }

    public boolean verify(Collection<Modification> modifications, List<Integer> heights) {
        try {
            Objects.requireNonNull(modifications);
        }
        catch (NullPointerException e) {
            logger.log(Level.WARNING, "modifications is null");
            return false;
        }

        try {
            Objects.requireNonNull(heights);
        }
        catch (NullPointerException e) {
            logger.log(Level.WARNING, "heights is null");
            return false;
        }

        modifications.forEach(modification -> {
            Optional.ofNullable(modification)
                    .ifPresentOrElse(modification1 ->
                            modify(modification1.x1, modification1.x2, modification1.operation), // Switched x1 with x2
                            () -> logger.log(Level.WARNING, "modification is null, skipping"));
        });

        for (int i = 0; i < heights.size(); i++) {
            if (!Objects.equals(get().get(i), heights.get(i)))
                return false;
        }

        return true;
    }

    /**
     * Internal testing class for testing private methods.
     * You can assume there are no errors here.
     */
    class TestHook {
        Map<Integer, Integer> get() {
            return Landscape.this.get();
        }

        void createHill(int x1, int x2) {
            Landscape.this.createHillOrValley(x1, x2, Operation.HILL);
        }

        void createValley(int x1, int x2) { Landscape.this.createHillOrValley(x1, x2, Operation.VALLEY); }

        void createHillOrValley(int x1, int x2, Operation o) {Landscape.this.createHillOrValley(x1, x2, o); }

        int initAmount(Operation o) { return Landscape.this.initAmt(o); }

        int adjust(int value, Operation o) { return Landscape.this.adjust(value, o); }
    }
}
