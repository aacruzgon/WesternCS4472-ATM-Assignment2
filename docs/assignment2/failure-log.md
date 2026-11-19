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

Bonus notes: times come from the git history (testing started 15:48 on Nov 19). BON-F1 and BON-F2 appeared on the first test run. Most of the 28 minutes before BON-F3 went to other work; actual testing for it took 1-2 minutes, after a teammate's note pointed it out. All three faults were already known from A1 (S-F1, S-F2, S-F4), so diagnosing took under a minute each.

## Totals

Fill these in on Dec 1, once every owner has logged their failures.

| Operation | Failures | Total minutes testing | Total minutes diagnosing |
|---|---:|---:|---:|
| Withdrawal | | | |
| Deposit | | | |
| Transfer | | | |
| Bonus | 3 | 28 | <3 |
