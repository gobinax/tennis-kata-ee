package org.example.tennis.application;

import org.example.tennis.domain.Match;
import org.example.tennis.domain.Player;

public class TennisDemo {

    private static final Player A = new Player("Nadal");
    private static final Player B = new Player("Djokovic");

    public static void main(String[] args) {
        Match match = new Match(A, B);

        int countPoints = 0;
        while (match.winner().isEmpty()) {
            match.point(randomPlayer());
            if (++countPoints % 10 == 0) {
                System.out.println(match.printScore());
            }
        }

        System.out.println(match.printScore());
    }

    private static Player randomPlayer() {
        return (Math.random() <= 0.5) ? A : B;
    }
}
