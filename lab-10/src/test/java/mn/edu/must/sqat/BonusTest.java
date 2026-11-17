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

    @Test
    @DisplayName("DU: attendance 31->32(T)")
    void bonusNullAttendanceThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(null, 1));
    }

    @Test
    @DisplayName(
        "DU: attendance 31->32(F); threshold 31->32(T)"
    )
    void bonusNegativeThresholdThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> DataFlowTargets.bonus(
                        new int[]{},
                        -1));
    }

    @Test
    @DisplayName(
        "DU: present 35->50(T); best 37->53(F); "
        + "score 51->56; attendance 31->38(F)"
    )
    void bonusEmptyThresholdZeroGetsFive() {
        assertEquals(
                5,
                DataFlowTargets.bonus(
                        new int[]{},
                        0));
    }

    @Test
    @DisplayName(
        "DU: present 35->50(F); score 49->56"
    )
    void bonusEmptyThresholdOneGetsZero() {
        assertEquals(
                0,
                DataFlowTargets.bonus(
                        new int[]{},
                        1));
    }

    @Test
    @DisplayName(
        "DU: score 51->56; best 43->53(F)"
    )
    void bonusOnePresentMeetsThreshold() {
        assertEquals(
                5,
                DataFlowTargets.bonus(
                        new int[]{1},
                        1));
    }

    @Test
    @DisplayName(
        "DU: streak 46->41; best 43->42(F)"
    )
    void bonusResetMakesBestComparisonFalse() {
        assertEquals(
                0,
                DataFlowTargets.bonus(
                        new int[]{1, 0, 1},
                        3));
    }

    @Test
    @DisplayName(
        "DU: score 49->54; score 54->56; best 43->53(T)"
    )
    void bonusFivePresentBelowThresholdGetsStreakOnly() {
        assertEquals(
                3,
                DataFlowTargets.bonus(
                        new int[]{1, 1, 1, 1, 1},
                        6));
    }

    @Test
    @DisplayName(
        "M2 kill: present count is 5 but longest streak is only 4"
    )
    void bonusSeparatedAttendanceDoesNotEarnFiveDayStreak() {
        assertEquals(
                5,
                DataFlowTargets.bonus(
                        new int[]{1, 0, 1, 1, 1, 1},
                        5));
    }
}
