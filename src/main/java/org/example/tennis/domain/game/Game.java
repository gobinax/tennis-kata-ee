package org.example.tennis.domain.game;

import org.example.tennis.domain.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Game {

    ////////////////////////
    // FIELD, CONSTRUCTOR //
    ////////////////////////

    protected final List<Player> players;
    protected int points[] = {0, 0};

    public Game(Player player1, Player player2) {
        players = Arrays.asList(player1, player2);
    }

    public static Game of(boolean tieBreak, Player player1, Player player2, int gamePoints1, int gamePoints2) {
        Game game = tieBreak ? new TieBreak(player1, player2) : new RegularGame(player1, player2);
        game.points[0] = gamePoints1;
        game.points[1] = gamePoints2;
        if(game.hasInvalidState()) throw new IllegalStateException();
        return game;
    }

    ////////////////
    // PUBLIC API //
    ////////////////

    public Game point(Player scorer) {
        if (winner().isPresent()) {
            throw new IllegalStateException("Cannot play more point: this game is already won by " + winner().get());
        }

        int scorerIdx = players.indexOf(scorer);
        if (scorerIdx == -1) {
            throw new IllegalArgumentException(
                    String.format("Game is between %s and %s. %s cannot score point.", players.get(0), players.get(1), scorer)
            );
        }

        increasePoints(scorerIdx);
        return this;
    }

    public Optional<Player> winner() {
        int winnerIdx = winnerIdx();
        if (winnerIdx != -1) {
            return Optional.of(players.get(winnerIdx));
        }
        return Optional.empty();
    }

    public boolean started() {
        return !(points[0] == 0 && points[1] == 0);
    }

    public abstract String printScore();

    @Override
    public String toString() {
        return printScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!players.equals(game.players)) return false;
        return Arrays.equals(points, game.points);
    }

    @Override
    public int hashCode() {
        int result = players.hashCode();
        result = 31 * result + Arrays.hashCode(points);
        return result;
    }

    ///////////////////////////////
    // PRIVATE/PROTECTED METHODS //
    ///////////////////////////////

    /**
     * @return 0: player1 wins, 1: player2 wins, -1: no winner yet
     */
    protected int winnerIdx() {
        if (isWinner(0)) return 0;
        if (isWinner(1)) return 1;
        return -1;
    }

    /**
     * @return true if playerIdx won this game
     */
    protected boolean isWinner(int playerIdx) {
        return haveEnoughPoint(playerIdx) && has2PointsAdvantage(playerIdx);
    }

    /**
     * @return true if player has more points than other player
     */
    protected boolean hasMorePoints(int playerIdx) {
        return points[playerIdx] > points[otherPlayerIdx(playerIdx)];
    }

    /**
     * @return true if player has enought point to win the game or being in deuce situation
     */
    protected boolean haveEnoughPoint(int playerIdx) {
        return points[playerIdx] >= nbPointToWin();
    }

    protected abstract int nbPointToWin();

    /**
     * @return true if player has 2 more points than other player
     */
    protected boolean has2PointsAdvantage(int playerIdx) {
        return points[playerIdx] - points[otherPlayerIdx(playerIdx)] > 1;
    }

    /**
     * @return the index of the other player
     */
    private int otherPlayerIdx(int playerIdx) {
        return (playerIdx == 0) ? 1 : 0;
    }

    private void increasePoints(int playerIdx) {
        points[playerIdx]++;
    }

    private boolean hasInvalidState() {
        return (points[0] > nbPointToWin() || points[1] > nbPointToWin()) && Math.abs(points[0] - points[1]) > 2;
    };

}
