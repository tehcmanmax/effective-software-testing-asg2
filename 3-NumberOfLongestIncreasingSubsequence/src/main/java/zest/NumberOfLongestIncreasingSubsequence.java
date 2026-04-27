package zest;

import java.util.Arrays;

public class NumberOfLongestIncreasingSubsequence {
    public int findNumberOfLIS(int[] nums) {

        if (nums == null || nums.length == 0 || nums.length > 2000) {
            throw new IllegalArgumentException("Input array is invalid.");
        }

        for (int num : nums) {
            if (num < -1000000 || num > 1000000) {
                throw new IllegalArgumentException("Array elements must be in the range [-10^4, 10^4].");
            }
        }

        int n = nums.length;
        int[] lengths = new int[n];
        int[] counts = new int[n];
        Arrays.fill(lengths, 1);
        Arrays.fill(counts, 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    if (lengths[j] + 1 > lengths[i]) {
                        lengths[i] = lengths[j] + 1;
                        counts[i] = counts[j];
                    } else if (lengths[j] + 1 == lengths[i]) {
                        counts[i] += counts[j];
                    }
                }
            }
            if (!checkLoopInvariants(nums, lengths, counts, i, -1)) {
                throw new IllegalStateException("Loop invariants are violated at outer loop index: " + i);
            }
        }

        int maxLength = 0;
        for (int length : lengths) {
            if (length > maxLength) {
                maxLength = length;
            }
        }

        int result = 0;
        for (int i = 0; i < n; i++) {
            if (lengths[i] == maxLength) {
                result += counts[i];
            }
        }

        if (result < 1) {
            throw new IllegalStateException("The result is out of valid range.");
        }

        return result;
    }

    private boolean checkLoopInvariants(int[] nums, int[] lengths, int[] counts, int currentIndex, int innerIndex) {
        for (int k = 0; k <= currentIndex; k++) {
            if (lengths[k] < 1 || counts[k] < 1) {
                return false;
            }
        }
        return true;
    }
}
