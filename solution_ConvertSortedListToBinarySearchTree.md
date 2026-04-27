# Solution: ConvertSortedListToBinarySearchTree

## Problem Understanding

The method receives the head of a singly linked list whose values are sorted in ascending order and must return a height-balanced binary search tree containing the same values.

From the README constraints:
- the linked list length is in `[0, 20000]`
- every value is in `[-100000, 100000]`
- the input list must be sorted in ascending order

The implementation first copies the linked list into an array-style list, then recursively selects the middle element as the root of each subtree. This preserves the in-order sequence and keeps subtree heights balanced.

## Bugs Found

No functional bug was found for valid inputs from the README examples.

However, the original implementation did not enforce the documented input contract. It accepted unsorted lists, out-of-range values, and lists longer than the maximum constraint. These contract violations could produce a tree that does not represent a valid conversion of the required input domain.

## Fix

Input validation was added while traversing the linked list:
- values below `-100000` or above `100000` throw `IllegalArgumentException`
- a value smaller than the previous node throws `IllegalArgumentException`
- more than `20000` nodes throws `IllegalArgumentException`

The empty list remains valid and returns `null`.

## Task 1: Code Coverage

The deterministic JUnit tests were selected to execute all branches in `ConvertSortedListToBinarySearchTree`:
- empty list
- single-node list
- odd-length example from the README
- even-length input
- duplicate values
- boundary values and maximum valid length
- unsorted input
- values below and above the allowed range
- too many nodes

JaCoCo was configured in `pom.xml` and `mvn test` generates the coverage report. The latest run reports 100% line and branch coverage for `ConvertSortedListToBinarySearchTree`.

## Task 2: Designing Contracts

### Pre-conditions
- `head` may be `null`
- if non-null, the list length must be at most `20000`
- values must be in `[-100000, 100000]`
- values must be sorted in ascending order

### Post-conditions
- the output contains exactly the same values as the input
- an in-order traversal of the output equals the input sequence
- the output tree is height-balanced
- empty input returns `null`

### Invariants

The class is stateless, so there is no object-level invariant beyond the method contract.

These contracts were implemented in `sortedListToBST` while traversing the linked list. Invalid inputs throw `IllegalArgumentException`.

## Task 3: Testing Contracts

Valid-input tests assert that normal inputs do not throw and that the postconditions hold.

Invalid-input tests assert that contract violations are rejected with `IllegalArgumentException`:
- unsorted list
- value too small
- value too large
- list longer than `20000`

Postconditions are checked by helper assertions:
- `inOrder(root)` must equal the original sorted input
- `assertBalanced(root)` verifies that every subtree differs in height by at most `1`
- empty input must return `null`

## Task 4: Property-Based Testing

`jqwik` was used for generated tests.

Properties tested:
- **P1 In-order preservation:** every generated sorted valid list appears unchanged in an in-order traversal of the output tree
- **P2 Height balance:** every generated valid list produces a height-balanced tree
- **P3 Contract enforcement:** generated unsorted lists and generated out-of-range values are rejected

Generator rationale:
- valid generated lists use values inside the README bounds and are sorted before conversion
- generated sizes include `0` to check the empty-list case
- invalid generated inputs isolate one contract violation at a time

## Task 5: Fuzz-Style Testing

In addition to jqwik properties, the test file includes deterministic fuzz-style tests using fixed random seeds. Fixed seeds make failures reproducible.

Fuzz-style tests added:
- `fuzzesManyRandomValidSortedLists`: generates many random valid lists, sorts them, converts them, and checks in-order preservation plus balance
- `fuzzesManyRandomInvalidLists`: generates many descending two-node lists and checks that the sorted-order precondition is enforced
- `fuzzesManyRandomOutOfRangeValues`: generates many values outside `[-100000, 100000]` and checks that the value-range precondition is enforced

These tests give broader input variation than the hand-written examples while still remaining repeatable during grading.
