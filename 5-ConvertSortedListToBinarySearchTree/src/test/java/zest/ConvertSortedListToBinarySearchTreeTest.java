package zest;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvertSortedListToBinarySearchTreeTest {

    private final ConvertSortedListToBinarySearchTree sut = new ConvertSortedListToBinarySearchTree();

    /* TASK 1: CODE COVERAGE TESTS */

    @Test
    void example1FromProblemStatement() {
        TreeNode root = sut.sortedListToBST(list(-10, -3, 0, 5, 9));

        assertEquals(Arrays.asList(0, -10, 5, null, -3, null, 9), levelOrder(root));
        assertEquals(Arrays.asList(-10, -3, 0, 5, 9), inOrder(root));
        assertBalanced(root);
    }

    @Test
    void example2EmptyInputReturnsNull() {
        assertNull(sut.sortedListToBST(null));
    }

    @Test
    void example3SingleNodeInput() {
        TreeNode root = sut.sortedListToBST(list(0));

        assertEquals(Collections.singletonList(0), levelOrder(root));
        assertEquals(Collections.singletonList(0), inOrder(root));
        assertBalanced(root);
    }

    @Test
    void handlesEvenNumberOfNodes() {
        TreeNode root = sut.sortedListToBST(list(1, 2, 3, 4));

        assertEquals(Arrays.asList(2, 1, 3, null, null, null, 4), levelOrder(root));
        assertEquals(Arrays.asList(1, 2, 3, 4), inOrder(root));
        assertBalanced(root);
    }

    @Test
    void preservesDuplicateValues() {
        TreeNode root = sut.sortedListToBST(list(-1, -1, 0, 0, 2));

        assertEquals(Arrays.asList(-1, -1, 0, 0, 2), inOrder(root));
        assertBalanced(root);
    }

    /* TASK 2 + TASK 3: CONTRACT TESTS */

    @Test
    void acceptsBoundaryValuesAndMaximumLength() {
        int[] values = new int[20_000];
        Arrays.fill(values, 42);
        values[0] = -100_000;
        values[values.length - 1] = 100_000;

        TreeNode root = sut.sortedListToBST(list(values));

        assertEquals(values.length, inOrder(root).size());
        assertBalanced(root);
    }

    @Test
    void rejectsUnsortedInput() {
        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(1, 3, 2)));
    }

    @Test
    void rejectsValuesBelowAllowedRange() {
        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(-100_001)));
    }

    @Test
    void rejectsValuesAboveAllowedRange() {
        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(100_001)));
    }

    @Test
    void rejectsTooManyNodes() {
        int[] values = new int[20_001];
        Arrays.fill(values, 7);

        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(values)));
    }

    /* TASK 4: PROPERTY-BASED TESTING */

    @Property(tries = 250)
    @Label("Valid sorted inputs preserve values and produce balanced trees")
    void validSortedListsBecomeBalancedSearchTrees(@ForAll("validSortedLists") List<Integer> values) {
        TreeNode root = sut.sortedListToBST(list(values));

        assertEquals(values, inOrder(root));
        assertBalanced(root);
    }

    @Property(tries = 100)
    @Label("Unsorted lists violate the input contract")
    void generatedUnsortedListsAreRejected(@ForAll("unsortedLists") List<Integer> values) {
        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(values)));
    }

    @Property(tries = 100)
    @Label("Out-of-range values violate the input contract")
    void generatedOutOfRangeValuesAreRejected(@ForAll("outOfRangeValues") int value) {
        assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(value)));
    }

    /* TASK 5: FUZZ-STYLE TESTING */

    @Test
    void fuzzesManyRandomValidSortedLists() {
        Random random = new Random(123456789L);

        for (int trial = 0; trial < 500; trial++) {
            int[] values = randomSortedValues(random, random.nextInt(101));
            TreeNode root = sut.sortedListToBST(list(values));

            assertEquals(toList(values), inOrder(root));
            assertBalanced(root);
        }
    }

    @Test
    void fuzzesManyRandomInvalidLists() {
        Random random = new Random(987654321L);

        for (int trial = 0; trial < 500; trial++) {
            int high = random.nextInt(200_001) - 100_000;
            int low = high == -100_000 ? -100_000 : high - 1;

            assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(high, low)));
        }
    }

    @Test
    void fuzzesManyRandomOutOfRangeValues() {
        Random random = new Random(246813579L);

        for (int trial = 0; trial < 500; trial++) {
            int value = random.nextBoolean()
                    ? -100_001 - random.nextInt(10_000)
                    : 100_001 + random.nextInt(10_000);

            assertThrows(IllegalArgumentException.class, () -> sut.sortedListToBST(list(value)));
        }
    }

    @Provide
    Arbitrary<List<Integer>> validSortedLists() {
        return Arbitraries.integers()
                .between(-100_000, 100_000)
                .list()
                .ofMinSize(0)
                .ofMaxSize(250)
                .map(values -> {
                    Collections.sort(values);
                    return values;
                });
    }

    @Provide
    Arbitrary<List<Integer>> unsortedLists() {
        return Arbitraries.integers()
                .between(-100_000, 100_000)
                .filter(value -> value > -100_000)
                .map(value -> Arrays.asList(value, value - 1));
    }

    @Provide
    Arbitrary<Integer> outOfRangeValues() {
        return Arbitraries.oneOf(
                Arbitraries.integers().between(Integer.MIN_VALUE, -100_001),
                Arbitraries.integers().between(100_001, Integer.MAX_VALUE)
        );
    }

    private static ListNode list(List<Integer> values) {
        ListNode head = null;
        for (int i = values.size() - 1; i >= 0; i--) {
            head = new ListNode(values.get(i), head);
        }
        return head;
    }

    private static ListNode list(int... values) {
        ListNode head = null;
        for (int i = values.length - 1; i >= 0; i--) {
            head = new ListNode(values[i], head);
        }
        return head;
    }

    private static int[] randomSortedValues(Random random, int size) {
        int[] values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(200_001) - 100_000;
        }
        Arrays.sort(values);
        return values;
    }

    private static List<Integer> toList(int[] values) {
        List<Integer> list = new ArrayList<>(values.length);
        for (int value : values) {
            list.add(value);
        }
        return list;
    }

    private static List<Integer> inOrder(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        collectInOrder(root, values);
        return values;
    }

    private static void collectInOrder(TreeNode node, List<Integer> values) {
        if (node == null) {
            return;
        }
        collectInOrder(node.left, values);
        values.add(node.val);
        collectInOrder(node.right, values);
    }

    private static List<Integer> levelOrder(TreeNode root) {
        if (root == null) {
            return Collections.emptyList();
        }

        Queue<TreeNode> queue = new LinkedList<>();
        List<Integer> values = new ArrayList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                values.add(null);
            } else {
                values.add(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        trimTrailingNulls(values);
        return values;
    }

    private static void trimTrailingNulls(List<Integer> values) {
        while (!values.isEmpty() && values.get(values.size() - 1) == null) {
            values.remove(values.size() - 1);
        }
    }

    private static void assertBalanced(TreeNode root) {
        height(root);
    }

    private static int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        assertTrue(Math.abs(leftHeight - rightHeight) <= 1,
                "Tree is not height-balanced at node " + node.val);
        return Math.max(leftHeight, rightHeight) + 1;
    }
}
