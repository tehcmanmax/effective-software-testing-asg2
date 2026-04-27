# Solution: NumberOfLongestIncreasingSubsequence

## Bugs Found
    
### Bug 1: Mistake in dynamic programming strategiy by starting with filling lengths array as well as counts array with all values 0 instead of 1

This bug leads to the fact that all outputs are 1 under the actual value.
```
        Arrays.fill(lengths, 1);
        Arrays.fill(counts, 1);
```
___

## Code Coverage
| Element | Line Coverage | Missed Lines | Branch Coverage | Missed Branches |
|---|---|---|---|---|
| NumberOfLongestIncreasingSubsequence | 85% | 2 | 83% | 2 |

### Reasoning of Code Coverage
After fixing the bug, a line & branch coverage of 85%/83% was reached. We analized the branches and added the following tests: 
- twoSubsequencesOfSameLegth
- LongestSubsequenceAtTheBeginning
- LongestSubsequenceAtTheEnd

With these additional tests in place a line and code coverage of 100% was reached.

## Contracts
### Pre-conditions
- input Array length has to be from 1 to 2000.
- every value of the input Array has to be an int from -1000000 to 1000000.


### Post-conditions
- output Int must be at least 1.

### Invariants
- lengths[i] has always to be greater or equal to 1
- counts[i] has always to be greater or equal to 1


## Testing Contracts

I'm adding pre- as well as post-condition checks inside the method. The input array length may only be from 1 to 2000 and every element must be between -10^6 and 10^6. After the calculation, the output must be at least 1 and at most `nums.length`.
Invariants are checked after the inner loop for each index `i`: `lengths[i]` must always be ≥ 1 and `counts[i]` must always be ≥ 1.

### Preconditons: Input validation
```
if(nums == null || nums.length == 0 || nums.length > 2000) {
            throw new IllegalArgumentException("Input array is invalid.");
        }

for (int num : nums) {
    if (num < -1000000 || num > 1000000) {
        throw new IllegalArgumentException("Array elements must be in the range [-10^4, 10^4].");
    }
}
```
___

### Postconditions: Output validation
```
if (result < 1) {
            throw new IllegalStateException("The result is out of valid range.");
        }
```
___

### Invariants (Loop): Array validation
```
private boolean checkLoopInvariants(int[] nums, int[] lengths, int[] counts, int currentIndex, int innerIndex) {
        for (int k = 0; k <= currentIndex; k++) {
            if (lengths[k] < 1 || counts[k] < 1) {
                return false;
            }
        }
        return true;
    }
```
___

## Property-Based Testing
- **Result is always at least one:** For any valid input array, the result output is always at least one.
- **All Equal Numbers Return Array Length:** For any array with length n, the result output will be that number n.
- **Strictly Increasing Returns One:** If the numbers in the input are strictly increasing, the result output should be 1.
- **Strictly Decreasing Returns Array Length:** If the numbers in the input are strictly decreasing, the result output should be n.
