package zest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Arrays.fill;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

public class NumberOfLongestIncreasingSubsequenceTest {

    NumberOfLongestIncreasingSubsequence nOLIS = new NumberOfLongestIncreasingSubsequence();

    /* TASK 1: CODE COVERAGE TESTS */

    @Test
    void minLengthArray(){
        int[] input = new int[]{1};
        int expected = 1;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

       @Test
    void maxLengthArray(){
        int[] input = new int[2000];
            fill(input, 1);
        int expected = 2000;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

        @Test
    void allNumbersTheSame(){
        int[] input = new int[20];
            fill(input, 1);
        int expected = 20;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

         @Test
    void allNumbersNegative(){
        int[] input = new int[20];
            fill(input, -1);
        int expected = 20;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

        @Test
    void allNumbersZeroes(){
        int[] input = new int[20];
            fill(input, 0);
        int expected = 20;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

        @Test
    void allNumbersRising(){
        int[] input = new int[]{3,4,5,6,7};
        int expected = 1;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }
         @Test
    void allNumbersFalling(){
        int[] input = new int[]{7,6,5,4,3};
        int expected = 5;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

           @Test
    void allNumbersRisingNegative(){
        int[] input = new int[]{-7,-6,-5,-4,-3};
        int expected = 1;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }
         @Test
    void allNumbersFallingNegative(){
        int[] input = new int[]{-3,-4,-5,-6,-7};
        int expected = 5;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }
           @Test
    void allNumbersRisingMixed(){
        int[] input = new int[]{-2,-1,0,1,2};
        int expected = 1;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }
         @Test
    void allNumbersFallingMixed(){
        int[] input = new int[]{2,1,0,-1,-2};
        int expected = 5;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

    @Test
    void twoSubsequencesOfSameLegth(){
        int[] input = new int[]{1,3,5,4,9};
        int expected = 2;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

    @Test
    void LongestSubsequenceAtTheBeginning(){
        int[] input = new int[]{1,2,3,4,5,1,1,1};
        int expected = 1;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

    @Test
    void LongestSubsequenceAtTheEnd(){
        int[] input = new int[]{1,1,1,2,3,4,5};
        int expected = 3;
        int actual = nOLIS.findNumberOfLIS(input);
        assertEquals(expected, actual);
    }

    /* TASK 3: TESTING CONTRACTS */

    @Test
    void preconditionNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            nOLIS.findNumberOfLIS(null);
        });
    }

    @Test
    void preconditionArrayToSmall() {
        int[] input = new int[] {};
        assertThrows(IllegalArgumentException.class, () -> {
            nOLIS.findNumberOfLIS(input);
        });
    }

    @Test
    void preconditionArrayToBig() {
        int[] input = new int[2001];
        fill(input, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            nOLIS.findNumberOfLIS(input);
        });
    }  

    @Test
    void preconditionArrayValuesToSmall() {
        int[] input = new int[] { -10001, 1, 2 };
        assertThrows(IllegalArgumentException.class, () -> {
            nOLIS.findNumberOfLIS(input);
        });
    }

    @Test
    void preconditionArrayValuesToBig() {
        int[] input = new int[] { 10001, 1, 2 };
        assertThrows(IllegalArgumentException.class, () -> {
            nOLIS.findNumberOfLIS(input);
        });
    }   



    /* TASK 4: Property-Based Testing */

    @Property
    void resultIsAlwaysAtLeastOne(@ForAll("validInput") int[] nums){
        int result = nOLIS.findNumberOfLIS(nums);
        assertTrue(result >= 1);
    }

    @Provide
    Arbitrary<int[]> validInput() {
        return Arbitraries.integers()
                .between(-10_000, 10_000)
                .array(int[].class)
                .ofMinSize(1)
                .ofMaxSize(100);
    }

    @Property
    void allEqualNumbersReturnArrayLength(@ForAll @IntRange(min = 1, max = 100) int n) {
        int[] nums = new int[n];
        java.util.Arrays.fill(nums, 10);
        assertEquals(n, nOLIS.findNumberOfLIS(nums));
    }

    @Property
    void strictlyIncreasingReturnsOne(@ForAll @IntRange(min = 1, max = 50) int n) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = i;
        assertEquals(1, nOLIS.findNumberOfLIS(nums));
    }

    @Property
    void strictlyDecreasingReturnsArrayLength(@ForAll @IntRange(min = 1, max = 50) int n) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = n - i;
        assertEquals(n, nOLIS.findNumberOfLIS(nums));
    }
}
