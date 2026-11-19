package atm.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Bonus: basis path tests for {@link CashValidator#validateWithdrawal(int)}.
 *
 * Control flow graph (line numbers are from CashValidator.java):
 * <pre>
 * n1  lines 21-23  result = true; newAmount = amount % 50
 * n2  line 24      if (!(newAmount == 0))        decision D1
 * n3  line 25      newAmount = newAmount % 20
 * n4  line 26      if (!(newAmount == 0))        decision D2
 * n5  line 27      result = false
 * n6  line 31      return result
 *
 * Edges: n1-n2, n2-n3 (D1 true), n2-n6 (D1 false), n3-n4,
 *        n4-n5 (D2 true), n4-n6 (D2 false), n5-n6
 * </pre>
 *
 * V(G) = E - N + 2 = 7 - 6 + 2 = 3 (also 2 decisions + 1).
 *
 * Basis paths:
 * <ul>
 * <li>P1: n1, n2, n6 (amount % 50 == 0)</li>
 * <li>P2: n1, n2, n3, n4, n6 (amount % 50 != 0, then % 20 == 0)</li>
 * <li>P3: n1, n2, n3, n4, n5, n6 (amount % 50 != 0, then % 20 != 0)</li>
 * </ul>
 *
 * The paths come from the code, but the expected values come from the
 * specification: the ATM dispenses $20 and $50 bills, so an amount is valid
 * only if it is more than $0 and can be made from some mix of the two. P3
 * gets two tests, one amount the bills can make and one they cannot, because
 * the code returns false for every input on that path. P1 also gets $0 and
 * -$100: both are multiples of $50, but neither can be withdrawn.
 */
class CashValidatorTest {

	@Test
	@DisplayName("BON-01 P1: $100 = 2 x $50 is accepted")
	void bon01AmountOfOnlyFiftiesIsAccepted() {
		assertEquals(true, CashValidator.validateWithdrawal(100));
	}

	@Test
	@DisplayName("BON-02 P2: $40 = 2 x $20 is accepted")
	void bon02AmountOfOnlyTwentiesIsAccepted() {
		assertEquals(true, CashValidator.validateWithdrawal(40));
	}

	@Test
	@DisplayName("BON-03 P3: $60 = 3 x $20 is accepted")
	void bon03DispensableAmountOnRejectPathIsAccepted() {
		assertEquals(true, CashValidator.validateWithdrawal(60));
	}

	@Test
	@DisplayName("BON-04 P3: $30 cannot be made from $20 and $50 bills")
	void bon04UndispensableAmountIsRejected() {
		assertEquals(false, CashValidator.validateWithdrawal(30));
	}

	@Test
	@DisplayName("BON-10 P1: $0 is rejected")
	void bon10ZeroAmountIsRejected() {
		assertEquals(false, CashValidator.validateWithdrawal(0));
	}

	@Test
	@DisplayName("BON-11 P1: -$100 is rejected")
	void bon11NegativeAmountIsRejected() {
		assertEquals(false, CashValidator.validateWithdrawal(-100));
	}
}
