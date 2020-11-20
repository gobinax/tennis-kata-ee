package org.example.tennis.domain;

import org.example.tennis.domain.game.Game;

import java.util.Objects;
import java.util.Optional;

public class Match {

    private final Long id;

    private final Player player1;
    private final Player player2;

    private Set currentSet;


    public Match(Player player1, Player player2) {
        this(null, player1, player2);
    }

    public Match(Long id, Player player1, Player player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.currentSet = new Set(player1, player2);
    }

    public static Match of(Long matchId, String player1Name, String player2Name,
                           int gameCount1, int gameCount2,
                           int gamePoints1, int gamePoints2) {
        Player player1 = new Player(player1Name);
        Player player2 = new Player(player2Name);
        Match match = new Match(matchId, player1, player2);
        match.currentSet = Set.of(player1, player2, gameCount1, gameCount2, gamePoints1, gamePoints2);
        return match;
    }

    public Match point(Player player) {
        if (winner().isPresent()) {
            throw new IllegalStateException("Cannot play more point: this match is over");
        }

        currentSet.point(player);
        return this;
    }

    public Optional<Player> winner() {
        return currentSet.winner();
    }

    public String printScore() {
        String matchScore = player1 + "-" + player2 + "\n"
                + currentSet.printScore() + "\n";

        Game currentGame = currentSet.currentGame();
        if (currentGame.started() && currentGame.winner().isEmpty()) {
            matchScore += currentGame.printScore() + "\n";
        }

        matchScore += winner().map(winner -> "GAME, SET, MATCH: " + winner + "\n").orElse("");

        return matchScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (!Objects.equals(id, match.id)) return false;
        if (!player1.equals(match.player1)) return false;
        if (!player2.equals(match.player2)) return false;
        return currentSet.equals(match.currentSet);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + player1.hashCode();
        result = 31 * result + player2.hashCode();
        result = 31 * result + currentSet.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return printScore();
    }
}
