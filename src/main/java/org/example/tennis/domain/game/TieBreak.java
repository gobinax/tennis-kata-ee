package org.example.tennis.domain.game;


import org.example.tennis.domain.Player;

public class TieBreak extends Game {

    private static final int NB_POINT_TO_WIN = 6;

    ////////////////////////
    // FIELD, CONSTRUCTOR //
    ////////////////////////

    public TieBreak(Player player1, Player player2) {
        super(player1, player2);
    }


    @Override
    public String printScore() {
        int winnerIdx = winnerIdx();
        if (winnerIdx != -1) {
            return "Game: " + players.get(winnerIdx);
        }

        return points[0] + "-" + points[1];
    }

    @Override
    protected int nbPointToWin() {
        return NB_POINT_TO_WIN;
    }
}
