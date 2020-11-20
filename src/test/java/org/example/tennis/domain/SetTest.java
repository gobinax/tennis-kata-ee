package org.example.tennis.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SetTest {

    static Player A = new Player("ALICE");
    static Player B = new Player("BOB");

    static Stream<Arguments> params_no_winner() {
        return Stream.of(
                arguments("0-0", new Player[]{}),

                arguments("1-0", new Player[]{A}),
                arguments("5-0", new Player[]{A, A, A, A, A}),

                arguments("0-1", new Player[]{B}),
                arguments("0-5", new Player[]{B, B, B, B, B}),

                arguments("6-5", new Player[]{B, B, B, B, B, A, A, A, A, A, A}),
                arguments("5-6", new Player[]{B, B, B, B, B, A, A, A, A, A, B})
        );
    }

    @ParameterizedTest
    @MethodSource("params_no_winner")
    void should_not_detect_winner(String expectedPrintedScore, Player... games) {
        // GIVEN
        Set set = new Set(A, B);

        // WHEN
        for (Player player : games) {
            takeGame(set, player);
        }

        // THEN
        assertThat(set.printScore())
                .isEqualTo(expectedPrintedScore);

        assertThat(set.winner())
                .as("The winner is not the expected one.")
                .isEmpty();
    }

    static Stream<Arguments> params_detect_winner() {
        return Stream.of(
                arguments("6-0", A, new Player[]{A, A, A, A, A, A}),
                arguments("0-6", B, new Player[]{B, B, B, B, B, B}),

                arguments("6-4", A, new Player[]{A, A, A, A, B, B, B, B, A, A}),
                arguments("4-6", B, new Player[]{A, A, A, A, B, B, B, B, B, B}),

                arguments("7-5", A, new Player[]{A, A, A, A, A, B, B, B, B, B, A, A}),
                arguments("5-7", B, new Player[]{A, A, A, A, A, B, B, B, B, B, B, B})
        );
    }

    @ParameterizedTest
    @MethodSource("params_detect_winner")
    void should_detect_winner(String expectedPrintedScore, Player expectedWinner, Player... games) {
        // GIVEN
        Set set = new Set(A, B);

        // WHEN
        for (Player player : games) {
            takeGame(set, player);
        }

        // THEN
        assertThat(set.printScore())
                .isEqualTo(expectedPrintedScore);

        assertThat(set.winner())
                .as("The winner is not the expected one.")
                .contains(expectedWinner);
    }

    @Test
    void should_not_score_more_point_when_set_is_won() {
        // GIVEN
        Set set = new Set(A, B);

        // WHEN
        doNTimes(6, () -> takeGame(set, A));

        // THEN
        assertThatThrownBy(() -> set.point(A))
                .as("should not keep playing a game that is already won")
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void should_detect_tie_break_winner() {
        // GIVEN
        Set set = new Set(A, B);
        doNTimes(5, () -> takeGame(set, A));
        doNTimes(5, () -> takeGame(set, B));
        takeGame(set, A);
        takeGame(set, B); // 6-6

        // WHEN
        doNTimes(6, () -> set.point(A));

        // THEN
        assertThat(set.printScore())
                .isEqualTo("7-6");

        assertThat(set.winner())
                .as("The winner is not the expected one.")
                .contains(A);

        assertThatThrownBy(() -> set.point(A))
                .as("should not keep playing a game that is already won")
                .isInstanceOf(IllegalStateException.class);

    }

    /**
     * for convenience: player scores 4 Aces and take the game
     */
    private Set takeGame(Set set, Player player) {
        return set.point(player).point(player).point(player).point(player);
    }

    private void doNTimes(int until, Callable todo) {
        try {
            for (int i = 0; i < until; i++) {
                todo.call();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Arguments> valid_restore_data() {
        Player A = new Player("A");
        Player B = new Player("B");
        return Stream.of(
                arguments(Set.of(A, B, 6, 5, 3, 0), "6-5|40-0"),
                arguments(Set.of(A, B, 6, 6, 5, 0), "6-6|5-0"),
                arguments(Set.of(A, B, 7, 5, 0, 0), "7-5|0-0"),
                arguments(Set.of(A, B, 5, 7, 0, 0), "5-7|0-0"),
                arguments(Set.of(A, B, 7, 6, 0, 0), "7-6|0-0"),
                arguments(Set.of(A, B, 6, 7, 0, 0), "6-7|0-0")
        );
    }

    @ParameterizedTest
    @MethodSource("valid_restore_data")
    void should_restore_set_from_data(Set set, String expectedScore) {
        assertThat(set.printScore() + "|" + set.currentGame().printScore()).isEqualTo(expectedScore);
    }

    @Test
    void should_not_restore_set_from_invalid_data() {
        assertThatThrownBy(() -> Set.of(A, B, 7, 4, 0, 0))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Set.of(A, B, 4, 7, 0, 0))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Set.of(A, B, 8, 6, 0, 0))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Set.of(A, B, 6, 8, 0, 0))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Set.of(A, B, 7, 5, 1, 0))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Set.of(A, B, 5, 7, 1, 0))
                .isInstanceOf(IllegalStateException.class);
    }

}