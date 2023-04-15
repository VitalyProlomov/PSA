package appinterface;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Game;
import models.GamesSet;
import parsers.gg.GGPokerokRushNCashParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class PSAApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PSAApplication.class.getResource("views/gamesListView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 660, 440);
        stage.setScene(scene);

        stage.show();

//        ObjectMapper ob = new ObjectMapper();
//        GGPokerokRushNCashParser parser = new GGPokerokRushNCashParser();
//        try {
//            ArrayList<Game> games = parser.parseFile("D://pokerSession.txt");
//            GamesSet gSet = new GamesSet();
//            gSet.setGames(new HashSet<>(games));
//            ob.writeValue(new File("D://ser.txt"), gSet);
//
//            GamesSet deserSet = ob.readValue(new File("D://ser.txt"), GamesSet.class);
//            for (Game g : deserSet.getGames()) {
//                System.out.println(g + "\n");
//            }
//        } catch (IncorrectHandException e) {
//            e.printStackTrace();
//        } catch (IncorrectBoardException e) {
//            e.printStackTrace();
//        } catch (IncorrectCardException e) {
//            e.printStackTrace();
//        }


    }

    public static void main(String[] args) {
        launch();
    }
}