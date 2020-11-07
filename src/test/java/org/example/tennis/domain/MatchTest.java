package org.example.tennis.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchTest {
    @Test
    void should_start_match() {
        // GIVEN
        Player A = new Player("ALICE");
        Player B = new Player("BOB");
        Match match = new Match(A, B);

        // WHEN
        match.point(A);

        //THEN
        assertThat(match.winner()).isEmpty();
        assertThat(match.printScore())
                .isEqualTo(
                        "ALICE-BOB" + "\n" +
                                "0-0" + "\n" +
                                "15-0" + "\n");
    }
}