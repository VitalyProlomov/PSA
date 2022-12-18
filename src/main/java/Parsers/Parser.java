package Parsers;

import Models.Game;

import java.util.ArrayList;

public interface Parser {
    public Game parseGame(String gameText);

    public ArrayList<Game> parseFile(String path);
}
