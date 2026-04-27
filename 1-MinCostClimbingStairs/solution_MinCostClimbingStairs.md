# Solution: MinCostClimbingStairs

## Bugs Found

### Bug 1: Mistake in dynamic programming strategiy by starting with 0 at index 0 => instead of cost[0]

This bug leads to a wrong calculation of min cost beacus the index 0 will always be at cost 0.

```
        dp[0] = cost[0];
```
___

## Code Coverage
| Element | Line Coverage | Missed Lines | Branch Coverage | Missed Branches |
|---|---|---|---|---|
| MinCostClimbingStairs | 100% | 0 | 100% | 0 |

### Reasoning of Code Coverage
After fixing the bug, a line & branch coverage of 100% was reached. No further tests were implemented.

## Contracts
### Pre-conditions
- input Array length has to be from 2 to 1000.
- every cost of the input Array has to be an int from 0 to 999.

*Note: The called method does not implement any state, therefore I couldnt formulate pre-conditions for the method state.*

### Post-conditions
- output Int must be a number from 0 (if there is a path consisting only of zeroes) to max. 1000\*999 (max length of array multiplied with max amount of cost per index.). More accurately the max. value can be described in relation to the input Array length: number from 0 to input.length\*999.

*Note: The called method does not implement any state, therefore I couldnt formulate post-conditions for the method state.*


### Invariants

*Note: The called method does not implement any state, therefore I couldnt formulate invariants for internal method state.*

## Testing Contracts

I'm adding pre- as well as post-condition checks inside the method. The input array len may only be from 2 to 1000 and every cost of it mustn't be smaller than 0 or higher than 999. After the calculation, the output must be from 0 to costarray.length\*999.

### Preconditons: Input validation
```
if (cost.length < 2 || cost.length > 1000){
            throw new IllegalArgumentException("Array length must be between 2 and 1000");
        }

        for (int i : cost){
            if(i < 0 || i > 999){
                throw new IllegalArgumentException("Cost must be between 0 and 999");
            }
        }
```
___

### Postconditions: Output validation
```
if(minCost < 0 || minCost > cost.length * 999){
            throw new IllegalArgumentException("Minimum cost must be between 0 and " + (cost.length * 999));
        }
```
___

*Note: Could not implement invariants due to missing internal state*

## Property-Based Testing
- **Valid input does not throw:** For any valid input array, no exception is thrown.
- **Invalid array size throws:** For any array with length < 2 or > 1000, an IllegalArgumentException is thrown.
- **Invalid cost value throws:** For any array containing a value < 0 or > 999, an IllegalArgumentException is thrown.
- **Output in range:** For any valid input, the output is always between 0 and input.length * 999.
- **Symmetry:** Reversing the input array always yields the same result, because the staircase can be climbed in both directions with the same step structure.

