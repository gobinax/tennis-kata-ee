package org.example.tennis.domain;

public class Player {
    public final String name;

    public Player(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("player must have valid name");
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
