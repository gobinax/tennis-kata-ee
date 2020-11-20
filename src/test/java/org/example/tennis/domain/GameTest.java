package org.example.tennis.domain;

import org.assertj.core.api.ThrowableAssert;
import org.example.tennis.domain.game.Game;
import org.example.tennis.domain.game.RegularGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class GameTest {

    static Player A = new Player("ALICE");
    static Player B = new Player("BOB");

    static Stream<Arguments> params_keep_score() {
        return Stream.of(
                arguments("0-0", new Player[]{}),

                arguments("15-0", new Player[]{A}),
                arguments("30-0", new Player[]{A, A}),
                arguments("40-0", new Player[]{A, A, A}),
                arguments("Game: ALICE", new Player[]{A, A, A, A}),

                arguments("0-15", new Player[]{B}),
                arguments("0-30", new Player[]{B, B}),
                arguments("0-40", new Player[]{B, B, B}),
                arguments("Game: BOB", new Player[]{B, B, B, B})
        );
    }

    @ParameterizedTest
    @MethodSource("params_keep_score")
    void should_keep_game_score_from_0_to_win(String expectedPrintedScore, Player... points) {
        // GIVEN
        Game game = new RegularGame(A, B);

        // WHEN
        for (Player player : points) {
            game.point(player);
        }

        // THEN
        assertThat(game.printScore())
                .isEqualTo(expectedPrintedScore);
    }

    @Test
    void should_not_score_more_point_when_game_is_won() {
        // GIVEN
        Game game = new RegularGame(A, B);

        // WHEN
        game.point(A);
        game.point(A);
        game.point(A);
        game.point(A); // WIN

        // THEN
        assertThatThrownBy(() -> game.point(A))
                .as("should not keep playing a game that is already won")
                .isInstanceOf(IllegalStateException.class);
    }

    static Stream<Arguments> params_deuce_rule() {
        return Stream.of(
                arguments("Deuce", new Player[]{}),
                arguments("Deuce", new Player[]{A, B}),

                arguments("Advantage: ALICE", new Player[]{A}),
                arguments("Advantage: ALICE", new Player[]{A, B, A}),
                arguments("Advantage: ALICE", new Player[]{B, A, A}),

                arguments("Advantage: BOB", new Player[]{B}),
                arguments("Advantage: BOB", new Player[]{B, A, B}),
                arguments("Advantage: BOB", new Player[]{A, B, B}),

                arguments("Game: ALICE", new Player[]{A, A}),
                arguments("Game: ALICE", new Player[]{B, A, A, A}),
                arguments("Game: BOB", new Player[]{B, B}),
                arguments("Game: BOB", new Player[]{A, B, B, B})
        );
    }

    @ParameterizedTest
    @MethodSource("params_deuce_rule")
    void should_handle_deuce_rule(String expectedPrintedScore, Player... points) {
        // GIVEN
        Game game = new RegularGame(A, B);
        game.point(A).point(A).point(A);
        game.point(B).point(B).point(B); //DEUCE

        // WHEN
        for (Player player : points) {
            game.point(player);
        }

        // THEN
        assertThat(game.printScore())
                .isEqualTo(expectedPrintedScore);
    }

    private static Stream<Arguments> valid_restore_data() {
        Player A = new Player("A");
        Player B = new Player("B");
        return Stream.of(
                arguments(Game.of(false, A, B, 3, 0), "40-0"),
                arguments(Game.of(false, A, B, 8, 8), "Deuce"),
                arguments(Game.of(false, A, B, 9, 8), "Advantage: A"),
                arguments(Game.of(false, A, B, 8, 9), "Advantage: B"),
                arguments(Game.of(false, A, B, 4, 2), "Game: A"),
                arguments(Game.of(false, A, B, 2, 4), "Game: B"),
                arguments(Game.of(true, A, B, 0, 5), "0-5"),
                arguments(Game.of(true, A, B, 8, 8), "8-8"),
                arguments(Game.of(true, A, B, 9, 8), "9-8"),
                arguments(Game.of(true, A, B, 8, 9), "8-9"),
                arguments(Game.of(true, A, B, 6, 4), "Game: A"),
                arguments(Game.of(true, A, B, 4, 6), "Game: B")
        );
    }

    @ParameterizedTest
    @MethodSource("valid_restore_data")
    void should_restore_game_from_data(Game game, String expectedScore) {
        assertThat(game.printScore()).isEqualTo(expectedScore);
    }

    @Test
    void should_not_restore_game_from_invalid_data() {
        assertThatThrownBy(() -> Game.of(false, new Player("A"), new Player("B"), 5, 2))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Game.of(false, new Player("A"), new Player("B"), 2, 5))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Game.of(true, new Player("A"), new Player("B"), 9, 6))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> Game.of(true, new Player("A"), new Player("B"), 6, 9))
                .isInstanceOf(IllegalStateException.class);
    }
}