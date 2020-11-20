package org.example.tennis.domain;

import java.util.Optional;

public interface AllMatches {
    Match save(Match match);

    Optional<Match> findMatch(Long matchId);

    void updateMatch(Match match);
}
