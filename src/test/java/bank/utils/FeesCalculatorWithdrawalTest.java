package bank.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Assignment 2: white box tests for {@link FeesCalculator#calculateWithdrawalFee}.
 *
 * <p>Technique: statement coverage of the backward program slice for the criterion
 * FinalUse(fee), that is, the statement {@code return fee} (line 30 of FeesCalculator.java).
 * The slice is S = {6, 10, 12, 13, 15, 18, 19, 20, 21, 23, 28, 30}. Each test names the
 * slice statements it was built to execute. The slice, the assumptions and the
 * expected-value derivation are in docs/assignment2/withdrawal.md.
 *
 * <p>Oracle: the "Withdrawal" section of the Assignment 2 handout, never the code.
 */
class FeesCalculatorWithdrawalTest {

	/** Fees are compared as doubles; the smallest spec difference between two fees here is $0.20. */
	private static final double DELTA = 1e-6;

	/** Nominal balance for students: the spec makes the student fee independent of the balance. */
	private static final double STUDENT_BALANCE = 5000.00;

	/** Nominal day for non-students: the spec makes the non-student fee independent of the day. */
	private static final int NON_STUDENT_DAY = Calendar.WEDNESDAY;

	private final FeesCalculator calculator = new FeesCalculator();

	/** Covers slice statements 6, 10 (true), 12 (true), 13, 28, 30. */
	@Test
	@DisplayName("WD-01: student, Saturday -> no fee")
	void wd01StudentWeekend() {
		// spec: student, weekend -> no fee = 0.00
		double expected = 0.00;
		assertEquals(expected,
				calculator.calculateWithdrawalFee(200.00, STUDENT_BALANCE, true, Calendar.SATURDAY), DELTA);
	}

	/** Covers slice statements 6, 10 (true), 12 (false), 15, 28, 30. */
	@Test
	@DisplayName("WD-02: student, Wednesday -> 0.1% of amount")
	void wd02StudentWeekday() {
		// spec: student, not weekend -> 0.1% of 400 = 0.40
		double expected = 0.40;
		assertEquals(expected,
				calculator.calculateWithdrawalFee(400.00, STUDENT_BALANCE, true, Calendar.WEDNESDAY), DELTA);
	}

	/** Covers slice statements 6, 10 (false), 18 (true), 19, 28, 30. */
	@Test
	@DisplayName("WD-03: non-student, balance $500 -> 0.2% of amount")
	void wd03NonStudentBalanceBelow1000() {
		// spec: not a student, balance < $1,000 -> 0.2% of 300 = 0.60
		double expected = 0.60;
		assertEquals(expected,
				calculator.calculateWithdrawalFee(300.00, 500.00, false, NON_STUDENT_DAY), DELTA);
	}

	/** Covers slice statements 6, 10 (false), 18 (false), 20 (true), 21, 28, 30. */
	@Test
	@DisplayName("WD-04: non-student, balance $5,000 -> 0.1% of amount")
	void wd04NonStudentBalanceBetween1000And10000() {
		// spec: not a student, $1,000 <= balance < $10,000 -> 0.1% of 700 = 0.70
		double expected = 0.70;
		assertEquals(expected,
				calculator.calculateWithdrawalFee(700.00, 5000.00, false, NON_STUDENT_DAY), DELTA);
	}

	/** Covers slice statements 6, 10 (false), 18 (false), 20 (false), 23, 28, 30. */
	@Test
	@DisplayName("WD-05: non-student, balance $20,000 -> no fee")
	void wd05NonStudentBalanceAbove10000() {
		// spec: not a student, balance > $10,000 -> no fee = 0.00
		double expected = 0.00;
		assertEquals(expected,
				calculator.calculateWithdrawalFee(800.00, 20000.00, false, NON_STUDENT_DAY), DELTA);
	}
}
