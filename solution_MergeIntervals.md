# Solution: MergeIntervals

## Problem Understanding

The goal is to merge overlapping intervals and return a set of non-overlapping intervals that cover exactly the same range as the input  
From the problem constraints:
- number of intervals is in `[0 10^4]`
- every interval is `[start end]` with `start <= end`
- each interval has length `2`

The implementation sorts intervals by start and then iterates once either:
- extending the current merged interval when overlap/touch is found or
- starting a new merged interval when there is no overlap

## Bugs Found

### Bug 1: Empty input is allowed by constraints but currently crashes

`READMEmd` allows number of intervals in `[0 10^4]` but the current implementation directly accesses `intervals[0]`

```java
int[] newInterval = intervals[0];
```

For `[]` this caused `ArrayIndexOutOfBoundsException`

### Fix for Bug 1

Added a guard clause at the beginning of `merge`:

```java
if (intervalslength == 0) {
    return new int[0][0];
}
```

This now aligns implementation behavior with the input size constraint (`0` is valid)

___

### Bug 2: Preconditions are not enforced in production code

The implementation assumes valid inputs but does not explicitly validate:
- `intervals != null`
- each entry is non-null and length `2`
- `start <= end`

Contract tests document current failure modes (runtime exceptions or postcondition violations) for invalid inputs
This bug was intentionally **documented** through tests (Task 3 style) and not fully fixed in production code because the assignment focus is on testing and bug reporting

___

## Code Coverage
| Element | Line Coverage | Missed Lines | Branch Coverage | Missed Branches |
|---|---|---|---|---|
| MergeIntervals | 100% | 0 | 100% | 0 |

### Reasoning of Code Coverage
For class `MergeIntervals` all lines and both outcomes of `if (interval[0] <= newInterval[1])` are covered by deterministic tests:
- true branch via overlapping/touching intervals
- false branch via disjoint intervals

Coverage was achieved by combining:
- example-driven tests from the README,
- branch-targeted structural tests,
- contract checks on valid and invalid data,
- and property-based generated inputs

## Structural Testing Strategy (Chapter 3)

Deterministic cases were selected to force distinct control-flow paths:

- `[[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]`  
  Validates standard overlap behavior
- `[[1,4],[4,5]] -> [[1,5]]`  
  Validates touching-boundary merge
- `[[8,10],[1,2],[2,3]] -> [[1,3],[8,10]]`  
  Validates sorting effects on unsorted input
- `[[1,10],[2,3],[4,8]] -> [[1,10]]`  
  Validates nested intervals
- chain merge example  
  Validates repeated extension of the active merged interval
- disjoint intervals example  
  Validates repeated entry into the non-overlap branch
- single-interval input  
  Validates minimal non-empty behavior
- empty input  
  Previously revealed bug; now validated as returning empty output after fix

## Contracts

### Pre-conditions
- `intervals != null`
- each interval entry is non-null
- each interval has length `2`
- for each interval: `start <= end`

### Post-conditions
- output intervals are sorted by start
- output intervals are pairwise non-overlapping and non-touching (`prevEnd < nextStart`)
- output coverage equals input coverage (no loss no extra coverage)
- every output interval is well-formed (`start <= end`)

### Invariants
This class is stateless and has no object-level mutable state so no class invariant beyond method contracts was defined

## Testing Contracts

### Valid-input contract tests
- deterministic valid cases assert expected merged output
- reusable postcondition assertions verify:
  - sorted by start,
  - pairwise non-overlap/non-touch,
  - output well-formedness,
  - coverage equivalence between input and output

### Invalid-input contract tests
- `null` array -> `NullPointerException` (current behavior)
- `null` interval entry -> `NullPointerException` (current behavior)
- malformed interval length (`!= 2`) -> runtime exception (current behavior)
- `start > end` -> documented as contract violation (postconditions fail)

## Property-Based Testing

`jqwik` was used to automate generated tests

Properties tested:
- **P1 Contract preservation**: for generated valid intervals output satisfies all postconditions
- **P2 Idempotence**: `merge(merge(x)) == merge(x)`
- **P3 Permutation invariance**: reordering input intervals does not change the final merged result
- **P4 Empty/non-empty generated behavior**: generated lists include size `0`; empty input now returns empty output and still satisfies global postcondition checks

Generator rationale:
- generated intervals always satisfy `start <= end`
- interval shape is always length `2`
- sizes vary from empty to larger lists to improve confidence over broad input space

