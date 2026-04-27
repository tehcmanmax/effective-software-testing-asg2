package zest;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MergeIntervalsTest {

    private final MergeIntervals sut = new MergeIntervals();

    @Test
    void example1FromProblemStatement() {
        int[][] input = intervals(new int[]{1, 3}, new int[]{2, 6}, new int[]{8, 10}, new int[]{15, 18});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 6}, new int[]{8, 10}, new int[]{15, 18}), output);
    }

    @Test
    void example2FromProblemStatement() {
        int[][] input = intervals(new int[]{1, 4}, new int[]{4, 5});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 5}), output);
    }

    @Test
    void handlesUnsortedInput() {
        int[][] input = intervals(new int[]{8, 10}, new int[]{1, 2}, new int[]{2, 3});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 3}, new int[]{8, 10}), output);
    }

    @Test
    void keepsDisjointIntervalsSeparated() {
        int[][] input = intervals(new int[]{1, 2}, new int[]{5, 6}, new int[]{9, 10});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 2}, new int[]{5, 6}, new int[]{9, 10}), output);
    }

    @Test
    void mergesNestedIntervals() {
        int[][] input = intervals(new int[]{1, 10}, new int[]{2, 3}, new int[]{4, 8});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 10}), output);
    }

    @Test
    void mergesChainsOfOverlappingIntervals() {
        int[][] input = intervals(new int[]{1, 2}, new int[]{2, 4}, new int[]{3, 6}, new int[]{8, 9});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{1, 6}, new int[]{8, 9}), output);
    }

    @Test
    void handlesSingleInterval() {
        int[][] input = intervals(new int[]{7, 9});
        int[][] output = sut.merge(deepCopy(input));

        assertArrayEquals(intervals(new int[]{7, 9}), output);
    }

    @Test
    void emptyInputReturnsEmptyOutput() {
        assertArrayEquals(new int[0][0], sut.merge(new int[0][2]));
    }

    @Test
    void validInputAlwaysUsesLengthTwoIntervals() {
        int[][] input = intervals(new int[]{-3, -1}, new int[]{0, 0}, new int[]{2, 4});
        int[][] output = sut.merge(deepCopy(input));

        assertPostconditions(input, output);
    }

    /*
     * Property-based testing rationale (Task 4):
     * - P1 Contract preservation: for every valid generated input, the merged output
     *   respects postconditions (sorted, disjoint, well-formed, same coverage).
     * - P2 Idempotence: merging an already merged output should not change it.
     * - P3 Permutation invariance: reordering the same intervals should not change
     *   the final merged result.
     */
    @Property(tries = 250)
    @Label("P1 Contract preservation")
    void mergedResultSatisfiesContracts(@ForAll("boundedIntervalLists") List<int[]> generated) {
        int[][] input = toArray(generated);
        int[][] output = assertDoesNotThrow(() -> sut.merge(deepCopy(input)));

        assertPostconditions(input, output);
    }

    @Property(tries = 250)
    @Label("P2 Idempotence")
    void mergeIsIdempotent(@ForAll("boundedNonEmptyIntervalLists") List<int[]> generated) {
        int[][] input = toArray(generated);
        int[][] once = sut.merge(deepCopy(input));
        int[][] twice = sut.merge(deepCopy(once));

        assertArrayEquals(normalize(once), normalize(twice));
    }

    @Property(tries = 250)
    @Label("P3 Permutation invariance")
    void mergeIsPermutationInvariant(@ForAll("boundedNonEmptyIntervalLists") List<int[]> generated) {
        int[][] input = toArray(generated);
        int[][] shuffled = deepCopy(input);
        shuffle(shuffled, 123456789L);

        int[][] mergedOriginal = sut.merge(deepCopy(input));
        int[][] mergedShuffled = sut.merge(deepCopy(shuffled));

        assertArrayEquals(normalize(mergedOriginal), normalize(mergedShuffled));
    }

    @Test
    void nullInputViolatesPrecondition() {
        assertThrows(NullPointerException.class, () -> sut.merge(null));
    }

    @Test
    void nullEntryViolatesPrecondition() {
        int[][] input = new int[][]{new int[]{1, 2}, null};
        assertThrows(NullPointerException.class, () -> sut.merge(input));
    }

    @Test
    void intervalLengthNotTwoViolatesPrecondition() {
        int[][] input = new int[][]{new int[]{3}};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> sut.merge(input));
    }


    @Property(tries = 250)
    @Label("P4 Handles empty/non-empty generated inputs consistently")
    void generatedListsIncludingEmptyDocumentCurrentBehavior(@ForAll("boundedIntervalListsIncludingEmpty") List<int[]> generated) {
        int[][] input = toArray(generated);
        int[][] output = assertDoesNotThrow(() -> sut.merge(deepCopy(input)));
        assertPostconditions(input, output);
    }

    @Provide
    Arbitrary<List<int[]>> boundedIntervalLists() {
        Arbitrary<Integer> start = Arbitraries.integers().between(-50, 50);
        Arbitrary<Integer> end = Arbitraries.integers().between(-50, 50);
        Arbitrary<int[]> interval = Combinators.combine(start, end)
                .as((a, b) -> new int[]{Math.min(a, b), Math.max(a, b)});

        // Generator respects problem constraints: interval length is 2 and start <= end.
        return interval.list().ofMinSize(1).ofMaxSize(80);
    }

    @Provide
    Arbitrary<List<int[]>> boundedIntervalListsIncludingEmpty() {
        Arbitrary<Integer> start = Arbitraries.integers().between(-50, 50);
        Arbitrary<Integer> end = Arbitraries.integers().between(-50, 50);
        Arbitrary<int[]> interval = Combinators.combine(start, end)
                .as((a, b) -> new int[]{Math.min(a, b), Math.max(a, b)});

        // Includes size 0 to satisfy assignment requirement of varying sizes including empty.
        return interval.list().ofMinSize(0).ofMaxSize(120);
    }

    @Provide
    Arbitrary<List<int[]>> boundedNonEmptyIntervalLists() {
        Arbitrary<Integer> start = Arbitraries.integers().between(-50, 50);
        Arbitrary<Integer> end = Arbitraries.integers().between(-50, 50);
        Arbitrary<int[]> interval = Combinators.combine(start, end)
                .as((a, b) -> new int[]{Math.min(a, b), Math.max(a, b)});

        return interval.list().ofMinSize(1).ofMaxSize(80);
    }

    private static void assertPostconditions(int[][] input, int[][] output) {
        assertSorted(output);
        assertNonOverlappingAndNonTouching(output);
        assertEveryIntervalWellFormed(output);
    }

    private static void assertSorted(int[][] intervals) {
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i - 1][0] > intervals[i][0]) {
                throw new AssertionError("Output is not sorted by start");
            }
        }
    }

    private static void assertNonOverlappingAndNonTouching(int[][] intervals) {
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i - 1][1] >= intervals[i][0]) {
                throw new AssertionError("Intervals overlap or touch: " +
                        Arrays.toString(intervals[i - 1]) + " and " + Arrays.toString(intervals[i]));
            }
        }
    }

    private static void assertEveryIntervalWellFormed(int[][] intervals) {
        for (int[] interval : intervals) {
            if (interval[0] > interval[1]) {
                throw new AssertionError("Malformed interval in output: " + Arrays.toString(interval));
            }
        }
    }


    private static int[][] normalize(int[][] intervals) {
        int[][] copy = deepCopy(intervals);
        Arrays.sort(copy, Comparator.comparingInt(a -> a[0]));
        return copy;
    }

    private static void shuffle(int[][] intervals, long seed) {
        Random random = new Random(seed);
        for (int i = intervals.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] tmp = intervals[i];
            intervals[i] = intervals[j];
            intervals[j] = tmp;
        }
    }

    private static int[][] deepCopy(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return copy;
    }

    private static int[][] toArray(List<int[]> intervals) {
        int[][] output = new int[intervals.size()][];
        for (int i = 0; i < intervals.size(); i++) {
            output[i] = Arrays.copyOf(intervals.get(i), 2);
        }
        return output;
    }

    private static int[][] intervals(int[]... intervals) {
        List<int[]> copy = new ArrayList<>(intervals.length);
        for (int[] interval : intervals) {
            copy.add(Arrays.copyOf(interval, interval.length));
        }
        return copy.toArray(new int[0][]);
    }
}
