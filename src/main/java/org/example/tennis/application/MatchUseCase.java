package org.example.tennis.application;

import org.example.tennis.domain.Match;
import org.example.tennis.domain.AllMatches;
import org.example.tennis.domain.Player;

import java.util.List;
import java.util.Optional;

public class MatchUseCase {

    private final AllMatches allMatches;

    public MatchUseCase(AllMatches allMatches) {
        this.allMatches = allMatches;
    }

    public Match createMatch(Player player1, Player player2) {
        return allMatches.save(new Match(player1, player2));
    }

    public Optional<String> showMatchScore(Long matchId) {
        return allMatches.findMatch(matchId).map(Match::printScore);
    }

    public Optional<String> scorePoints(long matchId, List<Player> points) {
        Optional<Match> maybeMatch = allMatches.findMatch(matchId)
                .map(match -> {
                    points.forEach(match::point);
                    return match;
                });
        maybeMatch.ifPresent(allMatches::updateMatch);
        return maybeMatch.map(Match::printScore);
    }
}
