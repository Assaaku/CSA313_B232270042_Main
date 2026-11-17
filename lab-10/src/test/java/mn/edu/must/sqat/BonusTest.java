package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BonusTest {

    @Test
    @DisplayName(
        "all-defs: present 35/40; streak 36/41; "
        + "best 37/43; score 51/54"
    )
    void allDefsLongStreak() {
        assertEquals(
                8,
                DataFlowTargets.bonus(
                        new int[]{1, 1, 1, 1, 1},
                        5));
    }

    @Test
    @DisplayName(
        "all-defs: streak 46->41; score 49->56"
    )
    void allDefsResetThenPresent() {
        assertEquals(
                0,
                DataFlowTargets.bonus(
                        new int[]{0, 1},
                        2));
    }
}
