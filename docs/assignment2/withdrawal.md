# Assignment 2: Withdrawal fees (program slicing + statement coverage)

Target: `bank.utils.FeesCalculator.calculateWithdrawalFee(double amount, double accountBalance, boolean student, int dayOfWeek)`
Technique: backward static program slice on `FinalUse(fee)`, then statement coverage of the slice
JUnit class: `src/test/java/bank/utils/FeesCalculatorWithdrawalTest.java`

## Section 1: Process and assumptions

### Specification under test (oracle)

| Rule | Condition | Fee |
|---|---|---|
| S1 | Student, transaction on the weekend (Saturday or Sunday) | no fee |
| S2 | Student, otherwise (Monday to Friday) | 0.1% of amount |
| N1 | Not a student, balance < $1,000 | 0.2% of amount |
| N2 | Not a student, $1,000 <= balance < $10,000 | 0.1% of amount |
| N3 | Not a student, balance > $10,000 | no fee |

The expected fee for every test case is `amount x rate` with the rate taken from this table. The table is the oracle; the code is used only to choose which statements a test must execute, never to decide the expected value.

### Code under analysis

Line numbers are those of `src/main/java/bank/utils/FeesCalculator.java`.

```java
 6	public double calculateWithdrawalFee(double amount, double accountBalance, boolean student, int dayOfWeek) {
 7		// Calculate fee percentage
 8		double feePercentage;
 9		// Check if student
10		if (student) {
11			// Check if it's weekend
12			if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
13				feePercentage = 0.0;
14			} else {
15				feePercentage = 0.001;
16			}
17		} else {
18			if (accountBalance < 1000.00) {
19				feePercentage = 0.002;
20			} else if (accountBalance < 10000.00) {
21				feePercentage = 0.001;
22			} else {
23				feePercentage = 0.0;
24			}
25		}
26
27		// Calculate fee
28		double fee = amount * feePercentage;
29
30		return fee;
31	}
```

### Slicing criterion

`C = <30, {fee}>`: the value of `fee` at its final use, the statement `return fee` on line 30.

### Process

1. **Start at the criterion.** Line 30 uses `fee`, so line 30 is in the slice.
2. **Data dependencies.** Follow every variable used by a statement in the slice back to the statements that define it (def-use chains):
   - `fee` (used at 30) is defined only at line 28.
   - Line 28 uses `amount` and `feePercentage`. `amount` is defined only by the method entry (line 6, parameter). `feePercentage` reaches line 28 from five definitions: 13, 15, 19, 21 and 23.
   - Lines 12, 18 and 20 use `dayOfWeek`, `student` (line 10) and `accountBalance`, all defined by the method entry (line 6).
3. **Control dependencies.** Every statement in the slice also pulls in the predicates that decide whether it runs:
   - 13 and 15 are control dependent on 12; 12 is control dependent on 10 (true branch).
   - 19 is control dependent on 18; 20 is control dependent on 18 (false branch); 21 and 23 are control dependent on 20; 18 is control dependent on 10 (false branch).
   - 28 and 30 run on every path, so they depend only on the method entry.
4. **Repeat until no new statement is added.** Adding the predicates 10, 12, 18 and 20 only adds uses of parameters, which are already defined at line 6, so the slice is closed.

### The slice

`S(C) = {6, 10, 12, 13, 15, 18, 19, 20, 21, 23, 28, 30}` (12 statements).

| Line | Statement | Why it is in the slice |
|---|---|---|
| 6 | method entry: parameters `amount`, `accountBalance`, `student`, `dayOfWeek` | Defines every input. `amount` is used at 28 (data); `student`, `dayOfWeek`, `accountBalance` are used by the predicates 10, 12, 18, 20 (data). |
| 10 | `if (student)` | Control: 12, 13, 15 run only on its true branch; 18 to 23 only on its false branch. |
| 12 | `if ((dayOfWeek == Calendar.SATURDAY) \|\| (dayOfWeek == Calendar.SUNDAY))` | Control: decides between the definitions of `feePercentage` at 13 and 15. |
| 13 | `feePercentage = 0.0;` | Data: a definition of `feePercentage` that reaches 28. |
| 15 | `feePercentage = 0.001;` | Data: a definition of `feePercentage` that reaches 28. |
| 18 | `if (accountBalance < 1000.00)` | Control: decides between 19 and the `else if` at 20. |
| 19 | `feePercentage = 0.002;` | Data: a definition of `feePercentage` that reaches 28. |
| 20 | `else if (accountBalance < 10000.00)` | Control: decides between 21 and 23. |
| 21 | `feePercentage = 0.001;` | Data: a definition of `feePercentage` that reaches 28. |
| 23 | `feePercentage = 0.0;` | Data: a definition of `feePercentage` that reaches 28. |
| 28 | `double fee = amount * feePercentage;` | Data: the only definition of `fee` that reaches 30. |
| 30 | `return fee;` | The slicing criterion. |

### Statements left out of the slice

| Line(s) | Content | Why it is left out |
|---|---|---|
| 7, 9, 11, 27 | comments | Not executable. |
| 8 | `double feePercentage;` | A declaration with no initializer: it defines no value, so nothing flows from it to `fee` (see W-A2). |
| 14, 16, 17, 22, 24, 25, 31 | `} else {`, closing braces | Syntax only; the branch they belong to is represented by its predicate (10, 12, 18, 20). |
| 26, 29 | blank lines | Not executable. |
| 33 to 145 | `calculateDepositInterest`, `calculateTransferFee` | Other methods; they neither define nor use any variable of this method. |

Every executable statement of `calculateWithdrawalFee` is in the slice: the method computes a single output, so the slice on its final use is the whole method body minus the declaration on line 8.

### Assumptions

1. **W-A1. No round call in the code under test.** The assignment says the call to the round function may be ignored. `calculateWithdrawalFee` on `dev` contains no rounding call (the fee is `amount * feePercentage`, returned as is), so there is nothing to ignore inside the slice. Expected values are compared unrounded, with the tolerance in W-A6.
2. **W-A2. Declarations without initializers are not definitions.** `double feePercentage;` (line 8) assigns no value; Java's definite assignment rule guarantees that one of 13, 15, 19, 21 or 23 always defines `feePercentage` before line 28 uses it. Line 8 is therefore not in the slice.
3. **W-A3. The method entry is a slice node.** The parameters are defined at the method entry (line 6), which is the source of every input data dependency, so line 6 is included. Statement coverage of line 6 is trivial: every test calls the method.
4. **W-A4. Day encoding.** `dayOfWeek` uses `java.util.Calendar` constants (`SUNDAY = 1`, ..., `SATURDAY = 7`), because the caller `bank.transactions.BankWithdrawal` passes `Calendar.DAY_OF_WEEK`. Weekend means `Calendar.SATURDAY` or `Calendar.SUNDAY`; Monday to Friday is a weekday.
5. **W-A5. Balance of exactly $10,000.** The specification says "less than $10,000" for 0.1% and "more than $10,000" for no fee, and gives no fee for exactly $10,000. No test depends on that value: the test that reaches line 23 uses $20,000, which is clearly "more than $10,000".
6. **W-A6. Oracle tolerance.** Fees are `double`s, compared with a tolerance of 1e-6, far smaller than the smallest difference between two specified fees in this suite ($0.20), so no fault can hide inside the tolerance.
7. **W-A7. Irrelevant inputs held at nominal.** The specification makes the student fee independent of the balance and the non-student fee independent of the day. Student tests hold the balance at $5,000.00; non-student tests hold the day at `Calendar.WEDNESDAY`. Amounts differ per test (all are positive, whole-dollar withdrawals) so that a wrong rate always produces a visibly wrong fee.

## Section 2: Test cases

Coverage criterion: every statement of `S(C)` is executed by at least one test. The slice has five definitions of `feePercentage` (13, 15, 19, 21, 23) on mutually exclusive paths, so at least five tests are needed, and five are enough.

| ID | Amount | Balance | Student | Day of week | Spec rule | Expected fee | Slice statements covered |
|---|---|---|---|---|---|---|---|
| WD-01 | $200.00 | $5,000.00 | yes | 7 SATURDAY | S1: weekend, no fee | 0.00 | 6, 10 (T), 12 (T), 13, 28, 30 |
| WD-02 | $400.00 | $5,000.00 | yes | 4 WEDNESDAY | S2: 0.1% x 400 | 0.40 | 6, 10 (T), 12 (F), 15, 28, 30 |
| WD-03 | $300.00 | $500.00 | no | 4 WEDNESDAY | N1: 0.2% x 300 | 0.60 | 6, 10 (F), 18 (T), 19, 28, 30 |
| WD-04 | $700.00 | $5,000.00 | no | 4 WEDNESDAY | N2: 0.1% x 700 | 0.70 | 6, 10 (F), 18 (F), 20 (T), 21, 28, 30 |
| WD-05 | $800.00 | $20,000.00 | no | 4 WEDNESDAY | N3: > $10,000, no fee | 0.00 | 6, 10 (F), 18 (F), 20 (F), 23, 28, 30 |

(T) / (F): the outcome of the predicate on that test's path.

### Coverage check

| Slice statement | WD-01 | WD-02 | WD-03 | WD-04 | WD-05 | Covered |
|---|---|---|---|---|---|---|
| 6 | x | x | x | x | x | yes |
| 10 | x | x | x | x | x | yes |
| 12 | x | x | | | | yes |
| 13 | x | | | | | yes |
| 15 | | x | | | | yes |
| 18 | | | x | x | x | yes |
| 19 | | | x | | | yes |
| 20 | | | | x | x | yes |
| 21 | | | | x | | yes |
| 23 | | | | | x | yes |
| 28 | x | x | x | x | x | yes |
| 30 | x | x | x | x | x | yes |

All 12 slice statements are covered (100% statement coverage of `S(C)`).

## Test results

| ID | Expected fee | Actual fee | Result |
|---|---|---|---|
| WD-01 | 0.00 | 0.00 | Pass |
| WD-02 | 0.40 | 0.40 | Pass |
| WD-03 | 0.60 | 0.60 | Pass |
| WD-04 | 0.70 | 0.70 | Pass |
| WD-05 | 0.00 | 0.00 | Pass |

All five tests passed in the CI run on [PR #1](https://github.com/aacruzgon/WesternCS4472-ATM-Assignment2/pull/1): every branch of the slice assigns the rate its specification rule gives (13: S1 0%, 15: S2 0.1%, 19: N1 0.2%, 21: N2 0.1%, 23: N3 0%), and line 28 multiplies it by the amount.

## Fault localization

No test failed, so the slice exposes no fault in `calculateWithdrawalFee`.

## Failure log (keep for Assignment 3)

No withdrawal failures were found, so there are no entries.
