package bank.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Assignment 2: white box tests for {@link FeesCalculator#calculateDepositInterest}.
 *
 * <p>Technique: data-flow testing on the variable {@code interestPercentage}. It is defined at
 * lines 42, 44, 48, 50, 56, 58, 62 and 64 of FeesCalculator.java and used once, at line 70
 * ({@code double interest = amount * interestPercentage}). Each definition reaches the use along
 * exactly one definition-clear path, giving eight DU-paths, DU1 to DU8. Each test executes every
 * statement of one DU-path. The DU-path table, the assumptions and the expected-value derivation
 * are in docs/assignment2/deposit.md.
 *
 * <p>Oracle: the "Deposit" section of the Assignment 2 handout, never the code.
 */
class FeesCalculatorDepositTest {

	/** Fees are compared as doubles; the smallest gap between two candidate fees here is $0.10. */
	private static final double DELTA = 1e-6;

	private final FeesCalculator calculator = new FeesCalculator();

	/** DU1 &lt;42, 70&gt;: covers statements 33, 37 (T), 39 (T), 41 (T), 42, 70, 72. */
	@Test
	@DisplayName("DEP-01: DU1, student, $200 deposit, balance $2,000 -> 1% of amount")
	void dep01StudentAmountAbove100BalanceAbove1000() {
		// spec S1: student, amount > $100, balance > $1,000 -> 1% of 200 = 2.00
		double expected = 2.00;
		assertEquals(expected, calculator.calculateDepositInterest(200.00, 2000.00, true), DELTA);
	}

	/** DU2 &lt;44, 70&gt;: covers statements 33, 37 (T), 39 (T), 41 (F), 44, 70, 72. */
	@Test
	@DisplayName("DEP-02: DU2, student, $200 deposit, balance $500 -> 0.5% of amount")
	void dep02StudentAmountAbove100BalanceAtMost1000() {
		// spec S2: student, amount > $100, balance <= $1,000 -> 0.5% of 200 = 1.00
		double expected = 1.00;
		assertEquals(expected, calculator.calculateDepositInterest(200.00, 500.00, true), DELTA);
	}

	/** DU3 &lt;48, 70&gt;: covers statements 33, 37 (T), 39 (F), 47 (T), 48, 70, 72. */
	@Test
	@DisplayName("DEP-03: DU3, student, $50 deposit, balance $6,000 -> 0.5% of amount")
	void dep03StudentAmountAtMost100BalanceAbove5000() {
		// spec S3: student, amount <= $100, balance > $5,000 -> 0.5% of 50 = 0.25
		double expected = 0.25;
		assertEquals(expected, calculator.calculateDepositInterest(50.00, 6000.00, true), DELTA);
	}

	/** DU4 &lt;50, 70&gt;: covers statements 33, 37 (T), 39 (F), 47 (F), 50, 70, 72. */
	@Test
	@DisplayName("DEP-04: DU4, student, $50 deposit, balance $1,000 -> no fee")
	void dep04StudentAmountAtMost100BalanceAtMost5000() {
		// spec S4: student, amount <= $100, balance <= $5,000 -> no fee = 0.00
		double expected = 0.00;
		assertEquals(expected, calculator.calculateDepositInterest(50.00, 1000.00, true), DELTA);
	}

	/** DU5 &lt;56, 70&gt;: covers statements 33, 37 (F), 54 (T), 55 (T), 56, 70, 72. */
	@Test
	@DisplayName("DEP-05: DU5, non-student, $1,000 deposit, balance $6,000 -> 1% of amount")
	void dep05NonStudentAmountAbove500BalanceAbove5000() {
		// spec N1: not a student, amount > $500, balance > $5,000 -> 1% of 1,000 = 10.00
		double expected = 10.00;
		assertEquals(expected, calculator.calculateDepositInterest(1000.00, 6000.00, false), DELTA);
	}

	/** DU6 &lt;58, 70&gt;: covers statements 33, 37 (F), 54 (T), 55 (F), 58, 70, 72. */
	@Test
	@DisplayName("DEP-06: DU6, non-student, $1,000 deposit, balance $2,000 -> 0.5% of amount")
	void dep06NonStudentAmountAbove500BalanceAtMost5000() {
		// spec N2: not a student, amount > $500, balance <= $5,000 -> 0.5% of 1,000 = 5.00
		double expected = 5.00;
		assertEquals(expected, calculator.calculateDepositInterest(1000.00, 2000.00, false), DELTA);
	}

	/** DU7 &lt;62, 70&gt;: covers statements 33, 37 (F), 54 (F), 61 (T), 62, 70, 72. */
	@Test
	@DisplayName("DEP-07: DU7, non-student, $200 deposit, balance $20,000 -> 0.5% of amount")
	void dep07NonStudentAmountAtMost500BalanceAbove10000() {
		// spec N3: not a student, amount <= $500, balance > $10,000 -> 0.5% of 200 = 1.00
		double expected = 1.00;
		assertEquals(expected, calculator.calculateDepositInterest(200.00, 20000.00, false), DELTA);
	}

	/** DU8 &lt;64, 70&gt;: covers statements 33, 37 (F), 54 (F), 61 (F), 64, 70, 72. */
	@Test
	@DisplayName("DEP-08: DU8, non-student, $200 deposit, balance $5,000 -> no fee")
	void dep08NonStudentAmountAtMost500BalanceAtMost10000() {
		// spec N4: not a student, amount <= $500, balance <= $10,000 -> no fee = 0.00
		double expected = 0.00;
		assertEquals(expected, calculator.calculateDepositInterest(200.00, 5000.00, false), DELTA);
	}
}
