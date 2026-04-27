package zest;

import java.util.ArrayList;
import java.util.List;

public class FindAllDuplicatesInArray {
    public List<Integer> findDuplicates(int[] nums) {

        if (nums == null || nums.length == 0 || nums.length > 100000) {
            throw new IllegalArgumentException("Input array is invalid.");
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 1 || nums[i] > nums.length) {
                throw new IllegalArgumentException("Array elements must be in the range 1 to n.");
            }
        }

        List<Integer> duplicates = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {

            int index = Math.abs(nums[i]) - 1;
            if (nums[index] < 0) {
                duplicates.add(Math.abs(nums[i]));
            } else {
                nums[index] = -nums[index];
            }
        }

        if (duplicates.size() > nums.length / 2) {
            throw new IllegalStateException("Output list is too long.");
        }

        for (int d : duplicates) {
            if (d < 1 || d > nums.length) {
                throw new IllegalStateException("Output value out of range.");
            }
        }

        return duplicates;
    }
}
