package org.example.tennis.domain.game;

import org.example.tennis.domain.Player;

public class RegularGame extends Game {

    private static final String[] pointsLabel = {"0", "15", "30", "40"};
    private static final int NB_POINT_TO_WIN = 4;

    ////////////////////////
    // FIELD, CONSTRUCTOR //
    ////////////////////////

    public RegularGame(Player player1, Player player2) {
        super(player1, player2);
    }

    ////////////////
    // PUBLIC API //
    ////////////////

    @Override
    public String printScore() {
        int winnerIdx = winnerIdx();
        if (winnerIdx != -1) {
            return "Game: " + players.get(winnerIdx);
        }

        if (isDeuce()) {
            return "Deuce";
        }

        int advantageIdx = advantageIdx();
        if (advantageIdx != -1) {
            return "Advantage: " + players.get(advantageIdx);
        }

        return pointsLabel[points[0]] + "-" + pointsLabel[points[1]];
    }

    ///////////////////////////////
    // PRIVATE/PROTECTED METHODS //
    ///////////////////////////////

    @Override
    protected int nbPointToWin() {
        return NB_POINT_TO_WIN;
    }

    private boolean isDeuce() {
        return points[0] >= 3 && points[0] == points[1];
    }

    /**
     * @return 0: player1 has advantage, 1: player2 has advantage, -1: no advantage at the moment
     */
    private int advantageIdx() {
        if (hasAdvantage(0)) return 0;
        if (hasAdvantage(1)) return 1;
        return -1;
    }

    /**
     * @return true if player has advantage on other player
     */
    private boolean hasAdvantage(int playerIdx) {
        return haveEnoughPoint(playerIdx)
                && !has2PointsAdvantage(playerIdx)
                && hasMorePoints(playerIdx);
    }
}
