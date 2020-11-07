package org.example.tennis.domain;

public class Player {
    public final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
