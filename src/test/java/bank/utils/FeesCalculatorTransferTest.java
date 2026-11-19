package bank.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Assignment 2: white box tests for {@link FeesCalculator#calculateTransferFee}.
 *
 * <p>Technique: basis path testing. The control-flow graph of calculateTransferFee has
 * 15 decisions (D1 to D15), 34 nodes and 48 edges, so V(G) = 16 and there are 16 basis
 * paths, P1 to P16. Each test runs exactly one basis path and names it. The graph, the
 * paths, the assumptions and the expected-value derivation are in docs/assignment2/transfer.md.
 *
 * <p>Inputs only steer the decisions: amount $50 (&lt; $100) or $500 (&gt;= $100), balances
 * $500 (&lt; $1,000) or $5,000 (&gt;= $1,000), kept away from the edges on purpose (T-A5).
 *
 * <p>Oracle: the "Transfer" section of the Assignment 2 handout (rules S1-S8, N1-N8), never the code.
 */
class FeesCalculatorTransferTest {

	/** Fees are compared as doubles; the tolerance is far smaller than one cent. */
	private static final double DELTA = 1e-6;

	/** Steers "amount &lt; 100.00" to true. */
	private static final double SMALL_AMOUNT = 50.00;

	/** Steers "amount &lt; 100.00" to false. */
	private static final double LARGE_AMOUNT = 500.00;

	/** Steers a "balance &lt; 1000.00" decision to true. */
	private static final double LOW_BALANCE = 500.00;

	/** Steers a "balance &lt; 1000.00" decision to false. */
	private static final double HIGH_BALANCE = 5000.00;

	private final FeesCalculator calculator = new FeesCalculator();

	/** Covers basis path P1: D1 T, D2 T, D3 T, D4 T, ends at line 83. */
	@Test
	@DisplayName("TF-01 (P1): student, amount < $100, from < $1,000, to < $1,000 -> 0.1% of amount")
	void tf01StudentSmallFromLowToLow() {
		// spec S1: 0.1% of 50 = 0.05
		double expected = 0.05;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, LOW_BALANCE, LOW_BALANCE, true), DELTA);
	}

	/** Covers basis path P2: D1 T, D2 T, D3 T, D4 F, ends at line 85. */
	@Test
	@DisplayName("TF-02 (P2): student, amount < $100, from < $1,000, to >= $1,000 -> 0.05% of amount")
	void tf02StudentSmallFromLowToHigh() {
		// spec S2: 0.05% of 50 = 0.025
		double expected = 0.025;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, LOW_BALANCE, HIGH_BALANCE, true), DELTA);
	}

	/** Covers basis path P3: D1 T, D2 T, D3 F, D5 T, ends at line 89. */
	@Test
	@DisplayName("TF-03 (P3): student, amount < $100, from >= $1,000, to < $1,000 -> 0.5% of amount")
	void tf03StudentSmallFromHighToLow() {
		// spec S3: 0.5% of 50 = 0.25
		double expected = 0.25;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, HIGH_BALANCE, LOW_BALANCE, true), DELTA);
	}

	/** Covers basis path P4: D1 T, D2 T, D3 F, D5 F, ends at line 91. */
	@Test
	@DisplayName("TF-04 (P4): student, amount < $100, from >= $1,000, to >= $1,000 -> 0.25% of amount")
	void tf04StudentSmallFromHighToHigh() {
		// spec S4: 0.25% of 50 = 0.125
		double expected = 0.125;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, HIGH_BALANCE, HIGH_BALANCE, true), DELTA);
	}

	/** Covers basis path P5: D1 T, D2 F, D6 T, D7 T, ends at line 97. */
	@Test
	@DisplayName("TF-05 (P5): student, amount >= $100, from < $1,000, to < $1,000 -> 0.05% of amount")
	void tf05StudentLargeFromLowToLow() {
		// spec S5: 0.05% of 500 = 0.25
		double expected = 0.25;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, LOW_BALANCE, LOW_BALANCE, true), DELTA);
	}

	/** Covers basis path P6: D1 T, D2 F, D6 T, D7 F, ends at line 99. */
	@Test
	@DisplayName("TF-06 (P6): student, amount >= $100, from < $1,000, to >= $1,000 -> 0.025% of amount")
	void tf06StudentLargeFromLowToHigh() {
		// spec S6: 0.025% of 500 = 0.125
		double expected = 0.125;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, LOW_BALANCE, HIGH_BALANCE, true), DELTA);
	}

	/** Covers basis path P7: D1 T, D2 F, D6 F, D8 T, ends at line 103. */
	@Test
	@DisplayName("TF-07 (P7): student, amount >= $100, from >= $1,000, to < $1,000 -> 0.25% of amount")
	void tf07StudentLargeFromHighToLow() {
		// spec S7: 0.25% of 500 = 1.25
		double expected = 1.25;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, HIGH_BALANCE, LOW_BALANCE, true), DELTA);
	}

	/** Covers basis path P8: D1 T, D2 F, D6 F, D8 F, ends at line 105. */
	@Test
	@DisplayName("TF-08 (P8): student, amount >= $100, from >= $1,000, to >= $1,000 -> 0.125% of amount")
	void tf08StudentLargeFromHighToHigh() {
		// spec S8: 0.125% of 500 = 0.625
		double expected = 0.625;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, HIGH_BALANCE, HIGH_BALANCE, true), DELTA);
	}

	/** Covers basis path P9: D1 F, D9 T, D10 T, D11 T, ends at line 113. */
	@Test
	@DisplayName("TF-09 (P9): non-student, amount < $100, from < $1,000, to < $1,000 -> 0.2% of amount")
	void tf09NonStudentSmallFromLowToLow() {
		// spec N1: 0.2% of 50 = 0.10
		double expected = 0.10;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, LOW_BALANCE, LOW_BALANCE, false), DELTA);
	}

	/** Covers basis path P10: D1 F, D9 T, D10 T, D11 F, ends at line 115. */
	@Test
	@DisplayName("TF-10 (P10): non-student, amount < $100, from < $1,000, to >= $1,000 -> 0.1% of amount")
	void tf10NonStudentSmallFromLowToHigh() {
		// spec N2: 0.1% of 50 = 0.05
		double expected = 0.05;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, LOW_BALANCE, HIGH_BALANCE, false), DELTA);
	}

	/** Covers basis path P11: D1 F, D9 T, D10 F, D12 T, ends at line 119. */
	@Test
	@DisplayName("TF-11 (P11): non-student, amount < $100, from >= $1,000, to < $1,000 -> 1% of amount")
	void tf11NonStudentSmallFromHighToLow() {
		// spec N3: 1% of 50 = 0.50
		double expected = 0.50;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, HIGH_BALANCE, LOW_BALANCE, false), DELTA);
	}

	/** Covers basis path P12: D1 F, D9 T, D10 F, D12 F, ends at line 121. */
	@Test
	@DisplayName("TF-12 (P12): non-student, amount < $100, from >= $1,000, to >= $1,000 -> 0.5% of amount")
	void tf12NonStudentSmallFromHighToHigh() {
		// spec N4: 0.5% of 50 = 0.25
		double expected = 0.25;
		assertEquals(expected,
				calculator.calculateTransferFee(SMALL_AMOUNT, HIGH_BALANCE, HIGH_BALANCE, false), DELTA);
	}

	/** Covers basis path P13: D1 F, D9 F, D13 T, D14 T, ends at line 127. */
	@Test
	@DisplayName("TF-13 (P13): non-student, amount >= $100, from < $1,000, to < $1,000 -> 0.1% of amount")
	void tf13NonStudentLargeFromLowToLow() {
		// spec N5: 0.1% of 500 = 0.50
		double expected = 0.50;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, LOW_BALANCE, LOW_BALANCE, false), DELTA);
	}

	/** Covers basis path P14: D1 F, D9 F, D13 T, D14 F, ends at line 129. */
	@Test
	@DisplayName("TF-14 (P14): non-student, amount >= $100, from < $1,000, to >= $1,000 -> 0.05% of amount")
	void tf14NonStudentLargeFromLowToHigh() {
		// spec N6: 0.05% of 500 = 0.25
		double expected = 0.25;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, LOW_BALANCE, HIGH_BALANCE, false), DELTA);
	}

	/** Covers basis path P15: D1 F, D9 F, D13 F, D15 T, ends at line 133. */
	@Test
	@DisplayName("TF-15 (P15): non-student, amount >= $100, from >= $1,000, to < $1,000 -> 0.5% of amount")
	void tf15NonStudentLargeFromHighToLow() {
		// spec N7: 0.5% of 500 = 2.50
		double expected = 2.50;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, HIGH_BALANCE, LOW_BALANCE, false), DELTA);
	}

	/** Covers basis path P16: D1 F, D9 F, D13 F, D15 F, ends at line 135. */
	@Test
	@DisplayName("TF-16 (P16): non-student, amount >= $100, from >= $1,000, to >= $1,000 -> 0.25% of amount")
	void tf16NonStudentLargeFromHighToHigh() {
		// spec N8: 0.25% of 500 = 1.25
		double expected = 1.25;
		assertEquals(expected,
				calculator.calculateTransferFee(LARGE_AMOUNT, HIGH_BALANCE, HIGH_BALANCE, false), DELTA);
	}
}
