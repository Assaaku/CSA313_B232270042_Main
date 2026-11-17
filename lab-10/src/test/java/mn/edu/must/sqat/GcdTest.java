package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GcdTest {

    @Test
    @DisplayName("DU: x 13->20; y 13->15(F)")
    void gcdZeroSecondOperand() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName(
        "DU: x 13->16,17->16,17->20; "
        + "y 13->15(T),13->16,13->17,18->15(T/F),18->16,18->17; "
        + "tmp 16->18"
    )
    void gcdMultipleIterations() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
