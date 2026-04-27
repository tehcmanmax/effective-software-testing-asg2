# Solution: FindAllDuplicatesInArray

## Bugs Found
After implementing the structural tests, no Bugs were found.


## Code Coverage
| Element | Line Coverage | Missed Lines | Branch Coverage | Missed Branches |
|---|---|---|---|---|
| FindAllDuplicatesInArray | 100% | 0 | 100% | 0 |

### Reasoning of Code Coverage
After running the initial structural tests, a line & branch coverage of 100% was reached. No further tests were implemented at this point in time.

## Contracts
### Pre-conditions
- input Array length has to be from 1 to 100000.
- every entry of the input Array has to be an int from 1 to length of the array.
- every entry of the input array can only appear twice, one time or not at all.

*Note: The called method does not implement any state, therefore I couldnt formulate pre-conditions for the method state.*

### Post-conditions
- The output list length is between 0 and `nums.length / 2`.
- Every value in the output list is between 1 and `nums.length`.
- Every value in the output list appears exactly once.


### Invariants
- During the loop, the absolute value of each element must remain equal to its original value — only the sign may change. Since the problem requires constant auxiliary space, the entire array cannot be copied to verify this. Only a single value could be stored temporarily before modification for comparison, which would be somewhat artificial. Therefore, we decided to not implement this invariant in code.

## Testing Contracts

I'm adding pre- as well as post-condition checks inside the method. The input array length may only be from 1 to 10^5 and every element must be between 1 and `nums.length`. After the calculation, the output list length must be between 0 and `nums.length / 2`, every value in it must be between 1 and `nums.length`, and every value must appear exactly once. Invariants are not implemented in code due to the constant auxiliary space constraint.

### Preconditons: Input validation
```
if(nums == null || nums.length == 0 || nums.length > 100000) {
            throw new IllegalArgumentException("Input array is invalid.");
        }

for (int i = 0; i < nums.length; i++) {
    if (nums[i] < 1 || nums[i] > nums.length) {
        throw new IllegalArgumentException("Array elements must be in the range 1 to n.");
    }
}
```
Due to the constraint of using only O(1) space, I won't be able to ensure/check if there is a number appearing in the input array more then twice.

___

### Postconditions: Output validation
```
if(duplicates.size() > nums.length / 2) {
    throw new IllegalStateException("Output list is too long.");
}

for (int d : duplicates) {
    if (d < 1 || d > nums.length) {
        throw new IllegalStateException("Output value out of range.");
    }
}
```
Due to the constraint of using only O(1) space, I won't be able to ensure/check if every number is only contained exactly once in the output array.
___

*Note: Could not implement invariants due to missing flexibility in space restrictions (O(1))*

## Property-Based Testing
- **Valid input does not throw:** For any valid input array, no exception is thrown.
- **No duplicates in output:** The output list never contains the same value twice.
- **Only valid duplicates in output:** Every value in the output actually appears twice in the input.
- **Output values in range:** Every value in the output is between 1 and `nums.length`.
- **Output size bounded:** The output list size is always ≤ `nums.length / 2`.
- **Order independence:** Shuffling the input array always yields the same set of duplicates, because duplicate detection is independent of element order.

