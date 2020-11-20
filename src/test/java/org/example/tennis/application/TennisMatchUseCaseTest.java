package org.example.tennis.application;

import org.example.tennis.domain.Match;
import org.example.tennis.domain.AllMatches;
import org.example.tennis.domain.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TennisMatchUseCaseTest {

    // GIVEN
    private final AllMatches allMatches = mock(AllMatches.class);

    @Test
    void should_create_match() {
        Player N = new Player("Nadal");
        Player D = new Player("Djokovic");
        when(allMatches.save(new Match(N, D))).thenReturn(new Match(1L, N, D));
        MatchUseCase matchUseCase = new MatchUseCase(allMatches);

        // WHEN
        Match createdMatch = matchUseCase.createMatch(N, D);

        // THEN
        assertThat(createdMatch).isEqualTo(new Match(1L, N, D));
    }

    @Test
    void should_show_match_score() {
        MatchUseCase matchUseCase = new MatchUseCase(allMatches);
        when(allMatches.findMatch(42L)).thenReturn(Optional.of(Match.of(42L, "Nadal", "Djokovic", 5, 3, 3, 3)));

        assertThat(matchUseCase.showMatchScore(42L))
                .contains("Nadal-Djokovic\n5-3\nDeuce\n");
        assertThat(matchUseCase.showMatchScore(-1L))
                .isEmpty();
    }

    @Test
    void should_score_points() {
        MatchUseCase matchUseCase = new MatchUseCase(allMatches);
        when(allMatches.findMatch(42L)).thenReturn(Optional.of(Match.of(42L, "Nadal", "Djokovic", 5, 3, 3, 3)));
        Player N = new Player("Nadal");
        Player D = new Player("Djokovic");

        Optional<String> newScore = matchUseCase.scorePoints(42L, List.of(D, D, N));

        assertThat(newScore).contains("Nadal-Djokovic\n5-4\n15-0\n");
        verify(allMatches).updateMatch(Match.of(42L, "Nadal", "Djokovic", 5, 4, 1, 0));
    }

}
