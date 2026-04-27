package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Arrays.fill;
import net.jqwik.api.*;

public class MinCostClimbingStairsTest {

    MinCostClimbingStairs minCostClimbingStairs = new MinCostClimbingStairs();

    /* TASK 1: CODE COVERAGE TESTS */

    @Test
    void startAtIndex0alwaysJump2() {
        int[] input = new int[] { 5, 10, 15, 20, 25, 30 };
        int expected = 45;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void startAtIndex1alwaysJump2() {
        int[] input = new int[] { 50, 10, 60, 20, 70, 30 };
        int expected = 60;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void startAtIndex0mixedJumps() {
        int[] input = new int[] { 5, 50, 5, 5, 50, 5 };
        int expected = 20;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void startAtIndex1mixedJumps() {
        int[] input = new int[] { 50, 5, 5, 50, 5, 50 };
        int expected = 15;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void evenArrayLen() {
        int[] input = new int[] { 1, 10, 2, 7, 9, 30 };
        int expected = 12;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void oddArrayLen() {
        int[] input = new int[] { 5, 10, 15, 20, 25 };
        int expected = 30;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void allCostsSame() {
        int[] input = new int[] { 5, 5, 5, 5, 5, 5 };
        int expected = 15;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void allCostst0() {
        int[] input = new int[] { 0, 0, 0, 0, 0, 0 };
        int expected = 0;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void allCostsMax() {
        int[] input = new int[] { 999, 999, 999, 999, 999, 999 };
        int expected = 2997;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    @Test
    void randomCosts() {
        int[] input = new int[] { 30, 122, 0, 12, 12, 900, 999, 999, 1, 0, 0 };
        int expected = 1042;
        int actual = minCostClimbingStairs.minCostClimbingStairs(input);
        assertEquals(expected, actual);
    }

    /* TASK 3: TESTING CONTRACTS */

    @Test
    void preconditionArrayToSmall() {
        int[] input = new int[] { 5 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input);
        });
    }

    @Test
    void preconditionArrayToBig() {
        int[] input = new int[1001];
        fill(input, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input);
        });
    }

    @Test
    void preconditionArrayLen2() {
        int[] input = new int[] { 5, 10 };
        assertDoesNotThrow(() -> {
            minCostClimbingStairs.minCostClimbingStairs(input);
        });
    }

    @Test
    void preconditionArrayLen1000() {
        int[] input = new int[1000];
        fill(input, 5);
        assertDoesNotThrow(() -> {
            minCostClimbingStairs.minCostClimbingStairs(input);
        });
    }

    @Test
    void preconditionCostToSmall() {
        int[] input1 = new int[] { -1, 5, 10 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input1);
        });

        int[] input2 = new int[] { 5, -1, 10 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input2);
        });

        int[] input3 = new int[] { 5, 10, -1 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input3);
        });
    }

    @Test
    void preconditionCostToBig() {
        int[] input1 = new int[] { 1000, 5, 10 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input1);
        });

        int[] input2 = new int[] { 5, 1000, 10 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input2);
        });

        int[] input3 = new int[] { 5, 10, 1000 };
        assertThrows(IllegalArgumentException.class, () -> {
            minCostClimbingStairs.minCostClimbingStairs(input3);
        });
    }

    @Test
    void postconditionMinCostInRange() {
        int[] input1 = new int[] { 0, 0, 0, 0, 0, 0 };
        int actual1 = minCostClimbingStairs.minCostClimbingStairs(input1);
        assertTrue(actual1 >= 0 && actual1 <= input1.length * 999);

        int[] input2 = new int[] { 999, 999, 999, 999, 999, 999 };
        int actual2 = minCostClimbingStairs.minCostClimbingStairs(input2);
        assertTrue(actual2 >= 0 && actual2 <= input2.length * 999);
    }

    /* TASK 4: Property-Based Testing */

    @Property
    void validInputDoesNotThrow(
            @ForAll("validCostArray") int[] cost) {
        assertDoesNotThrow(() -> minCostClimbingStairs.minCostClimbingStairs(cost));
    }

    @Provide
    Arbitrary<int[]> validCostArray() {
        return Arbitraries.integers()
                .between(0, 999)
                .array(int[].class)
                .ofMinSize(2)
                .ofMaxSize(1000);
    }

    @Property
    void invalidInputDoesThrow(
            @ForAll("invalidCostArray") int[] cost) {
        assertThrows(IllegalArgumentException.class, () -> minCostClimbingStairs.minCostClimbingStairs(cost));
    }

    @Provide
    Arbitrary<int[]> invalidCostArray() {
        Arbitrary<int[]> tooSmall = Arbitraries.integers()
                .between(0, 999)
                .array(int[].class)
                .ofSize(1);

        Arbitrary<int[]> tooBig = Arbitraries.integers()
                .between(0, 999)
                .array(int[].class)
                .ofMinSize(1001)
                .ofMaxSize(2000);

        return Arbitraries.oneOf(tooSmall, tooBig);
    }

    @Property
    void invalidCostValuesDoThrow(
            @ForAll("invalidCostValueArray") int[] cost) {
        assertThrows(IllegalArgumentException.class, () -> minCostClimbingStairs.minCostClimbingStairs(cost));
    }

    @Provide
    Arbitrary<int[]> invalidCostValueArray() {
        return Arbitraries.integers()
                .between(0, 999)
                .array(int[].class)
                .ofMinSize(2)
                .ofMaxSize(1000)
                .flatMap(array -> Arbitraries.integers().between(0, array.length - 1)
                        .map(index -> {
                            array[index] = -1;
                            return array;
                        }));
    }

    @Property
    void postconditionMinCostInRangeProperty(
            @ForAll("validCostArray") int[] cost) {
        int minCost = minCostClimbingStairs.minCostClimbingStairs(cost);
        assertTrue(minCost >= 0 && minCost <= cost.length * 999);
            };

    @Property
    void backAndForthProperty(
            @ForAll("validCostArray") int[] cost) {
                int[] reversedCost = new int[cost.length];
                for (int i = 0; i < cost.length; i++) {
                    reversedCost[i] = cost[cost.length - 1 - i];
                }

                int resultOriginal = minCostClimbingStairs.minCostClimbingStairs(cost);
                int resultReversed = minCostClimbingStairs.minCostClimbingStairs(reversedCost);
                assertEquals(resultOriginal, resultReversed);

        
                

    }

}
