package zest;

import java.util.ArrayList;
import java.util.List;

public class ConvertSortedListToBinarySearchTree {
    private static final int MAX_NODE_COUNT = 20_000;
    private static final int MIN_NODE_VALUE = -100_000;
    private static final int MAX_NODE_VALUE = 100_000;

    public TreeNode sortedListToBST(ListNode head) {
        List<Integer> list = new ArrayList<>();
        Integer previous = null;
        while (head != null) {
            validateNodeValue(head.val);
            if (previous != null && head.val < previous) {
                throw new IllegalArgumentException("List must be sorted in ascending order");
            }
            list.add(head.val);
            if (list.size() > MAX_NODE_COUNT) {
                throw new IllegalArgumentException("List length must be at most 20000");
            }
            previous = head.val;
            head = head.next;
        }
        return sortedArrayToBST(list, 0, list.size() - 1);
    }

    private void validateNodeValue(int value) {
        if (value < MIN_NODE_VALUE || value > MAX_NODE_VALUE) {
            throw new IllegalArgumentException("Node values must be between -100000 and 100000");
        }
    }

    private TreeNode sortedArrayToBST(List<Integer> list, int start, int end) {
        if (start > end) {
            return null;
        }
        int mid = start + (end - start) / 2;
        TreeNode root = new TreeNode(list.get(mid));
        root.left = sortedArrayToBST(list, start, mid - 1);
        root.right = sortedArrayToBST(list, mid + 1, end);
        return root;
    }
}
