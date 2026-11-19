# Assignment 2: Combined failure log

Assignment 3 uses this log, so fill it in as you go.

- Start your timer when you start testing your operation.
- Add one row for every failing test, as soon as you find it.
- **Minutes testing** is the time from when you started testing until this failure appeared.
- **Minutes diagnosing** is the time from seeing the failure until you located the fault.
- Failure IDs use the prefix of your test IDs: `WD-Fx`, `DEP-Fx`, `TF-Fx`, `BON-Fx`.
- Add only failures you found yourself. Don't add or discuss faults in another owner's operation until they have logged them.

| Failure ID | Test that exposed it | Owner | Minutes testing before found | Minutes diagnosing |
|---|---|---|---:|---:|
| BON-F1 | BON-05, BON-06 | alessiap000 | 2-3 | <1 |
| BON-F2 | BON-03 | alessiap000 | 2-3 | <1 |
| BON-F3 | BON-10, BON-11 | alessiap000 | 28 | <1 |
| TF-F1 | TF-01 to TF-15 | juliakasperek | 64 | 18 |
| TF-F2 | TF-16 | juliakasperek | 64 | 17 |

Bonus notes: times come from the git history (testing started 15:48 on Nov 19). BON-F1 and BON-F2 appeared on the first test run. Most of the 28 minutes before BON-F3 went to other work; actual testing for it took 1-2 minutes, after a teammate's note pointed it out. All three faults were already known from A1 (S-F1, S-F2, S-F4), so diagnosing took under a minute each.

Transfer notes: testing started at 15:23 on Nov 19 (control-flow graph and basis paths first, then the tests). Both failures appeared on the first run of the transfer tests at 16:27, so both show 64 minutes. Diagnosing times are estimates (each a little under 20 minutes): every path ends at its own `feePercentage` assignment, so diagnosing meant checking the rate on each of the 16 lines against the A2 figure, confirming the 10x pattern on lines 83 to 133 (TF-F1), and confirming that line 135 does not fit that pattern (TF-F2).

## Totals

Fill these in on Dec 1, once every owner has logged their failures.

| Operation | Failures | Total minutes testing | Total minutes diagnosing |
|---|---:|---:|---:|
| Withdrawal | | | |
| Deposit | | | |
| Transfer | 2 | 64 | 35 |
| Bonus | 3 | 28 | <3 |
