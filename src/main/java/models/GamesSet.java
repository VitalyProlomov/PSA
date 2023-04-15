package models;

import java.util.HashSet;
import java.util.Set;

public class GamesSet {
    public GamesSet() {
        this.games = new HashSet<>();
    }
    private HashSet<Game> games;

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = new HashSet<>(games);
    }

    public void addGames(Set<Game> addendumGames) {
        games.addAll(addendumGames);
    }
}
