package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Arrays.fill;
import net.jqwik.api.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;

public class FindAllDuplicatesInArrayTest {

    FindAllDuplicatesInArray findAllDuplicatesInArray = new FindAllDuplicatesInArray();

    /* TASK 1: CODE COVERAGE TESTS */

    @Test
    void emptyArray() {
        int[] input = new int[] {};
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input);
        });
    }

    @Test
    void arrayWithOneElement() {
        int[] input = new int[] { 1 };
        List<Integer> expected = new ArrayList<>();
        List<Integer> actual = findAllDuplicatesInArray.findDuplicates(input);
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    void arrayWithNoDuplicats() {
        int[] input1 = new int[] { 1, 2, 3, 4, 5 };
        List<Integer> expected = new ArrayList<>();
        List<Integer> actual1 = findAllDuplicatesInArray.findDuplicates(input1);
        assertEquals(new HashSet<>(expected), new HashSet<>(actual1));

        int[] input2 = new int[] { 5, 4, 3, 2, 1 };
        List<Integer> actual2 = findAllDuplicatesInArray.findDuplicates(input2);
        assertEquals(new HashSet<>(expected), new HashSet<>(actual2));
    }

    @Test
    void arrayWithOneDuplicate() {
        int[] input = new int[] { 1, 2, 2, 3, 4 };
        List<Integer> expected = List.of(2);
        List<Integer> actual = findAllDuplicatesInArray.findDuplicates(input);
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    @Test
    void arrayWithMoreThanOneDuplicate() {
        int[] input = new int[] { 1, 2, 2, 3, 4, 5, 6, 4 };
        List<Integer> expected = List.of(2, 4);
        List<Integer> actual = findAllDuplicatesInArray.findDuplicates(input);
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    /* TASK 3: TESTING CONTRACTS */

    @Test
    void preconditionNullInput() {
        int[] input = null;
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input);
        });
    }

    @Test
    void preconditionArrayEmpty() {
        int[] input = new int[] {};
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input);
        });
    }

    @Test
    void preconditionArrayTooLong() {
        int[] input = new int[100001];
        fill(input, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input);
        });
    }

    @Test
    void preconditionElementOutOfRangeLowerBound() {
        int[] input1 = new int[] { 0, 1, 2, 3, 4 };
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input1);
        });
    }

    @Test
    void preconditionElementOutOfRangeUpperBound() {
        int[] input2 = new int[] { 1, 2, 3, 4, 6 };
        assertThrows(IllegalArgumentException.class, () -> {
            findAllDuplicatesInArray.findDuplicates(input2);
        });
    }

    @Test
    void postconditionOutputSize() {
        int[] input = new int[] { 1, 2, 2, 3, 4, 4 };
        List<Integer> actual = findAllDuplicatesInArray.findDuplicates(input);
        assertTrue(actual.size() <= input.length / 2);
    }

    @Test
    void postconditionOutputValuesInRange() {
        int[] input = new int[] { 1, 2, 2, 3, 4, 4 };
        List<Integer> actual = findAllDuplicatesInArray.findDuplicates(input);
        for (int d : actual) {
            assertTrue(d >= 1 && d <= input.length);
        }
    }

    /* TASK 4: Property-Based Testing */

    // @Property(tries = 5)
    // void debugValidInput(@ForAll("validInputArray") int[] input) {
    //     System.out.println(Arrays.toString(input));
    //     assertTrue(true);
    // }

    @Property
    void validInputDoesNotThrow(@ForAll("validInputArray") int[] input) {
        assertDoesNotThrow(() -> findAllDuplicatesInArray.findDuplicates(input));
    }

    @Property
    void postconditionNoDuplicatesInOutput(@ForAll("validInputArray") int[] input) {
        List<Integer> duplicates = findAllDuplicatesInArray.findDuplicates(input);
        HashSet<Integer> uniqueDuplicates = new HashSet<>(duplicates);
        assertEquals(uniqueDuplicates.size(), duplicates.size());
    }

    @Property
void postconditionOnlyValidDuplicatesInOutput(@ForAll("validInputArray") int[] input) {
    int[] inputCopy = input.clone();
    List<Integer> duplicates = findAllDuplicatesInArray.findDuplicates(input);
    HashSet<Integer> inputSet = new HashSet<>();
    for (int num : inputCopy) {
        if (!inputSet.add(num)) {
            assertTrue(duplicates.contains(num));
        }
    }
}

    @Property
    void postconditionOnlyDuplicatesInRangeInOutput(@ForAll("validInputArray") int[] input) {
        List<Integer> duplicates = findAllDuplicatesInArray.findDuplicates(input);
        for (int d : duplicates) {
            assertTrue(d >= 1 && d <= input.length);
        }
    }

    @Property
    void postconditionOutputSizeProperty(@ForAll("validInputArray") int[] input) {
        List<Integer> duplicates = findAllDuplicatesInArray.findDuplicates(input);
        assertTrue(duplicates.size() <= input.length / 2);
    }

    @Property
    void postconditionInputOrderIndependence(@ForAll("validInputArray") int[] input) {
        int[] inputCopy = input.clone();
        List<Integer> duplicatesOriginal = findAllDuplicatesInArray.findDuplicates(input);
        List<Integer> shuffleList = new ArrayList<>();
        for (int num : inputCopy) {
            shuffleList.add(num);
        }
        Collections.shuffle(shuffleList);
        int[] shuffled = shuffleList.stream().mapToInt(Integer::intValue).toArray();
        List<Integer> duplicatesShuffled = findAllDuplicatesInArray.findDuplicates(shuffled);
        assertEquals(new HashSet<>(duplicatesOriginal), new HashSet<>(duplicatesShuffled));
    }

    @Provide
    Arbitrary<int[]> validInputArray() {
        return Arbitraries.integers().between(1, 1000)
                .map(n -> {
                    List<Integer> list = new ArrayList<>();
                    for (int i = 1; i <= n; i++) {
                        list.add(i);
                        list.add(i);
                    }
                    Collections.shuffle(list);
                    return list.subList(0, n).stream().mapToInt(Integer::intValue).toArray();
                });
    }
}
