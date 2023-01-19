package Parsers;

import Exceptions.IncorrectBoardException;
import Exceptions.IncorrectCardException;
import Exceptions.IncorrectHandException;
import Models.Game;

import java.util.ArrayList;

public interface Parser {
    public Game parseGame(String gameText) throws IncorrectCardException, IncorrectHandException, IncorrectBoardException;

    public ArrayList<Game> parseFile(String path);
}
