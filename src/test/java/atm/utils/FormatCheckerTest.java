package atm.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import atm.exceptions.InvalidPinFormatException;

/**
 * Bonus: basis path tests for {@link FormatChecker#checkPinFormat(char[])}.
 *
 * Control flow graph (line numbers are from FormatChecker.java). Java's ||
 * short-circuits, so line 35 is split into one node per condition:
 * <pre>
 * n1  line 33  if (pin.length == 4)
 * n2  line 34  i = 0
 * n3  line 34  i &lt; 4
 * n4  line 35  pin[i] &lt; '0'
 * n5  line 35  pin[i] &gt;= '9'
 * n6  line 36  throw new InvalidPinFormatException()
 * n7  line 34  i++
 * n8  line 40  throw new InvalidPinFormatException()
 * n9  exit
 *
 * Edges: n1-n2 (T), n1-n8 (F), n2-n3, n3-n4 (T), n3-n9 (F), n4-n6 (T),
 *        n4-n5 (F), n5-n6 (T), n5-n7 (F), n7-n3, n6-n9, n8-n9
 * </pre>
 *
 * V(G) = E - N + 2 = 12 - 9 + 2 = 5 (also 4 conditions + 1).
 *
 * Basis paths (B1 is the baseline, each later path flips one decision):
 * <ul>
 * <li>B1: n1, n2, (n3, n4, n5, n7) x 4, n3, n9 (four characters, all pass)</li>
 * <li>B2: n1, n8, n9 (flip n1: length is not 4)</li>
 * <li>B3: n1, n2, n3, n4, n6, n9 (flip n4: first character below '0')</li>
 * <li>B4: n1, n2, n3, n4, n5, n6, n9 (flip n5: first character '9' or above)</li>
 * <li>B5: n1, n2, n3, n4, n5, n7, n3, n4, n5, n6, n9 (one character passes,
 * then flip n5 on the second)</li>
 * </ul>
 * Flipping n3 on its first visit is infeasible: with length 4, 0 &lt; 4 is
 * always true. B5 is the fifth independent path; it takes the loop back edge
 * n7-n3 without the exit edge n3-n9, which none of B1 to B4 does.
 *
 * Expected values come from the specification: a PIN is exactly 5 digits
 * (0-9). Every input on B1 and B3 to B5 has 4 characters, so the
 * specification rejects all of them.
 */
class FormatCheckerTest {
	private final FormatChecker checker = new FormatChecker();

	@Test
	@DisplayName("BON-05 B1: 4-digit PIN 1234 is rejected")
	void bon05FourDigitPinIsRejected() {
		assertThrows(InvalidPinFormatException.class, () -> checker.checkPinFormat("1234".toCharArray()));
	}

	@Test
	@DisplayName("BON-06 B2: 5-digit PIN 12345 is accepted")
	void bon06FiveDigitPinIsAccepted() {
		assertDoesNotThrow(() -> checker.checkPinFormat("12345".toCharArray()));
	}

	@Test
	@DisplayName("BON-07 B3: PIN starting with '-' is rejected")
	void bon07PinStartingBelowZeroIsRejected() {
		assertThrows(InvalidPinFormatException.class, () -> checker.checkPinFormat("-123".toCharArray()));
	}

	@Test
	@DisplayName("BON-08 B4: 4-digit PIN 9123 is rejected")
	void bon08PinStartingWithNineIsRejected() {
		assertThrows(InvalidPinFormatException.class, () -> checker.checkPinFormat("9123".toCharArray()));
	}

	@Test
	@DisplayName("BON-09 B5: PIN with a letter in second place is rejected")
	void bon09PinWithLetterInSecondPlaceIsRejected() {
		assertThrows(InvalidPinFormatException.class, () -> checker.checkPinFormat("1a23".toCharArray()));
	}
}
