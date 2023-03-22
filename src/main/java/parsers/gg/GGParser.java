package parsers.gg;

import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import models.Game;
import parsers.Parser;

import java.io.*;
import java.util.ArrayList;

public interface GGParser extends Parser {
    public default ArrayList<Game> parseDirectoryFiles(String path) throws IOException, IncorrectHandException, IncorrectBoardException, IncorrectCardException {
        File dir = new File(path);
        if (!dir.exists()) {
            throw new FileNotFoundException("Given path " + path + "could not have been found");
        }
        ArrayList<Game> allGames = new ArrayList<>();
        if (dir.isDirectory()) {
            if (dir.listFiles() == null) {
                return new ArrayList<>();
            }
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    addSubDirectoryGames(allGames, f);
                }
                if (f.isFile()) {
                    allGames.addAll(parseFile(f.getPath()));
                }
            }
        }
        return allGames;
    }

    private void addSubDirectoryGames(ArrayList<Game> allGames, File dir) throws IncorrectHandException, IncorrectBoardException, IOException, IncorrectCardException {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                addSubDirectoryGames(allGames, f);
            }
            if (f.isFile()) {
                allGames.addAll(parseFile(f.getPath()));
            }
        }
    }

    public default ArrayList<Game> parseFile(String path) throws IOException, IncorrectHandException, IncorrectBoardException, IncorrectCardException {
        ArrayList<Game> parsedGames = new ArrayList<>();

//        Files.lines(path);
        // try with resources
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader bfr = new BufferedReader(fr);

        String line = bfr.readLine();
        while (line != null) {
            StringBuilder gameText = new StringBuilder();
            // Getting to the first line of the game text.
            while (line != null && (line.equals("") || !line.substring(0, 5).equals("Poker"))) {
                line = bfr.readLine();
            }

            while (line != null && !line.equals("")) {
                gameText.append(line).append("\n");
                line = bfr.readLine();
            }
//            System.out.println(gameText.substring(0, 25));
            if (line != null) {
                parsedGames.add(parseGame(gameText.toString()));
            }
        }
        return parsedGames;
    }
}
