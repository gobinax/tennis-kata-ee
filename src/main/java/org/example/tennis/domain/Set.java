package org.example.tennis.domain;

import org.example.tennis.domain.game.Game;
import org.example.tennis.domain.game.RegularGame;
import org.example.tennis.domain.game.TieBreak;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Set {
    public static final int NB_GAMES_TO_WIN = 6;

    ////////////////////////
    // FIELD, CONSTRUCTOR //
    ////////////////////////

    private final List<Player> players;

    private final int[] gameCount = {0, 0};

    private Game currentGame;

    public Set(Player player1, Player player2) {
        players = Arrays.asList(player1, player2);
        currentGame = new RegularGame(player1, player2);
    }

    ////////////////
    // PUBLIC API //
    ////////////////

    public Set point(Player player) {
        if (winner().isPresent()) {
            throw new IllegalStateException("Cannot play more point: this set is over");
        }
        currentGame.point(player);

        currentGame.winner()
                .ifPresent(winner -> {
                    int playerIdx = players.indexOf(winner);
                    increaseGameCount(playerIdx);
                });

        return this;
    }


    public Optional<Player> winner() {
        int winnerIdx = winnerIdx();
        if (winnerIdx != -1) {
            return Optional.of(players.get(winnerIdx));
        }
        return Optional.empty();
    }

    public String printScore() {
        return "" + gameCount[0] + "-" + gameCount[1];
    }

    public Game currentGame() {
        return currentGame;
    }

    @Override
    public String toString() {
        return printScore();
    }

    /////////////////////
    // PRIVATE METHODS //
    /////////////////////

    /**
     * @return 0: player1 wins, 1: player2 wins, -1: no winner yet
     */
    private int winnerIdx() {
        if (isWinner(0)) return 0;
        if (isWinner(1)) return 1;
        return -1;
    }

    /**
     * @return true if playerIdx won this game
     */
    private boolean isWinner(int playerIdx) {
        return (haveEnoughPoint(playerIdx) && has2PointsAdvantage(playerIdx))
                || isTieBreakWinner(playerIdx);
    }

    private boolean isTieBreakWinner(int playerIdx) {
        return gameCount[playerIdx] == NB_GAMES_TO_WIN + 1
                && gameCount[otherPlayerIdx(playerIdx)] == NB_GAMES_TO_WIN;
    }

    /**
     * @return true if player has enought point to win the game or being in deuce situation
     */
    private boolean haveEnoughPoint(int playerIdx) {
        return gameCount[playerIdx] >= NB_GAMES_TO_WIN;
    }

    /**
     * @return true if player has 2 more points than other player
     */
    private boolean has2PointsAdvantage(int playerIdx) {
        return gameCount[playerIdx] - gameCount[otherPlayerIdx(playerIdx)] > 1;
    }

    /**
     * @return the index of the other player
     */
    private int otherPlayerIdx(int playerIdx) {
        return (playerIdx == 0) ? 1 : 0;
    }

    private void increaseGameCount(int playerIdx) {
        gameCount[playerIdx]++;
        currentGame = isTieBreakNecessary()
                ? new TieBreak(players.get(0), players.get(1))
                : new RegularGame(players.get(0), players.get(1));
    }

    private boolean isTieBreakNecessary() {
        return gameCount[0] == NB_GAMES_TO_WIN && gameCount[1] == NB_GAMES_TO_WIN;
    }
}
