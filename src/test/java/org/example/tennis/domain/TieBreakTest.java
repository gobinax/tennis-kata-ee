package org.example.tennis.domain;

import org.example.tennis.domain.game.Game;
import org.example.tennis.domain.game.TieBreak;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TieBreakTest {

    static Player A = new Player("ALICE");
    static Player B = new Player("BOB");

    static Stream<Arguments> params_no_winner() {
        return Stream.of(
                arguments("0-0", new Player[]{}),

                arguments("5-0", new Player[]{A, A, A, A, A}),
                arguments("0-5", new Player[]{B, B, B, B, B}),

                arguments("6-5", new Player[]{A, A, A, A, A, B, B, B, B, B, A}),
                arguments("5-6", new Player[]{A, A, A, A, A, B, B, B, B, B, B}),

                arguments("8-7", new Player[]{A, A, A, A, A, B, B, B, B, B, A, B, A, B, A}),
                arguments("7-8", new Player[]{A, A, A, A, A, B, B, B, B, B, A, B, A, B, B})
        );
    }

    @ParameterizedTest
    @MethodSource("params_no_winner")
    void should_not_detect_winner(String expectedPrintedScore, Player... points) {
        // GIVEN
        Game game = new TieBreak(A, B);

        // WHEN
        for (Player player : points) {
            game.point(player);
        }

        // THEN
        assertThat(game.printScore())
                .isEqualTo(expectedPrintedScore);

        assertThat(game.winner())
                .as("There should not be a winner to this game")
                .isEmpty();
    }

    static Stream<Arguments> params_detect_winner() {
        return Stream.of(
                arguments("Game: ALICE", A, new Player[]{A, A}), // 6-4
                arguments("Game: BOB", B, new Player[]{B, B}), // 4-6
                arguments("Game: ALICE", A, new Player[]{A, B, A, A}), // 7-5
                arguments("Game: BOB", B, new Player[]{A, B, B, B}), // 5-7
                arguments("Game: ALICE", A, new Player[]{A, B, A, B, A, A}), // 8-6
                arguments("Game: BOB", B, new Player[]{A, B, A, B, B, B}) // 6-8
        );
    }

    @ParameterizedTest
    @MethodSource("params_detect_winner")
    void should_detect_winner(String expectedPrintedScore, Player expectedWinner, Player... points) {
        // GIVEN
        Game game = new TieBreak(A, B);
        game.point(A).point(A).point(A).point(A);
        game.point(B).point(B).point(B).point(B); // 4-4

        // WHEN
        for (Player player : points) {
            game.point(player);
        }

        // THEN
        assertThat(game.printScore())
                .isEqualTo(expectedPrintedScore);

        assertThat(game.winner())
                .as("The winner is not the expected one.")
                .contains(expectedWinner);

        assertThatThrownBy(() -> game.point(A))
                .as("should not keep playing a game that is already won")
                .isInstanceOf(IllegalStateException.class);
    }

}