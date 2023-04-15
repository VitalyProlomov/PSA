package appinterface.controllers;


import appinterface.PSAApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import parsers.gg.GGPokerokRushNCashParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GamesListController {
    private final static String UPLOADED_GAMES_LIST_NAME = "/serializedGames.txt";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane gamesListBorderPane;

    @FXML
    private Button uploadButton;

    @FXML
    private TableView<Game> gamesTableView;

    @FXML
    private Scene scene;

    @FXML
    private Stage stage;

    private
    GamesSet gamesSet;

    @FXML
    void onUploadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Files");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TextFiles", "*.txt"));
//            fileChooser.setSelectedExtensionFilter();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadButton.getParent().getScene().getWindow());

        if (selectedFiles != null) {
            try {
                UploadController uploadController = new UploadController();
                ArrayList<Game> addedGames = uploadController.uploadFiles(selectedFiles);

                updateTable(addedGames);
                gamesSet.addGames(new HashSet<>(addedGames));
                serializeGames();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Something went wrong while uploading files..");
                System.out.println(ex.getMessage());
                // System.out.println(Arrays.toString(ex.getCause().getStackTrace()));
                alert.show();
            }
        }
    }

    @FXML private void initializeTable() throws IncorrectHandException, IncorrectBoardException, IOException, IncorrectCardException {
        gamesTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        gamesTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("gameId"));
        gamesTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("BigBlindSize$"));
        gamesTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("finalPot"));

    }

    private void initializeSerializedSavedGames() throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            URL url = PSAApplication.class.getResource("/serializedGames.txt");

            String path = url.getFile().replace("%20", " ");

            if (new File(path).length() != 0) {
                gamesSet = objectMapper.readValue(new File(path), GamesSet.class);
            }
            if (this.gamesSet == null) {
                this.gamesSet = new GamesSet();
            }

            if (gamesSet.getGames().size() != 0) {
                gamesTableView.getItems().addAll(gamesSet.getGames());
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Something went wring while parsing the file with saved games." +
                    "Make sure not to change anything in them or to close the file if it is opened");
            alert.show();
        }
    }


    @FXML
    void initialize() throws IncorrectHandException, IncorrectCardException, IncorrectBoardException, IOException {
        uploadButton.setOnAction(actionEvent -> {
            onUploadButtonClick();
        });

        initializeTable();
        initializeSerializedSavedGames();
    }

    private void updateTable(ArrayList<Game> gamesToAdd) {
        if (gamesToAdd == null) {
            return;
        }
        for (Game g : gamesToAdd) {
            if (!gamesSet.getGames().contains(g)) {
                gamesTableView.getItems().add(g);
            }
        }
    }

    private void serializeGames() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            URL url = PSAApplication.class.getResource(UPLOADED_GAMES_LIST_NAME);
            String path = url.getFile().replace("%20", " ");
            File file = new File(path);

            objectMapper.writeValue(file, gamesSet);

        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("File with saved games was not able to upload, please close it if it is opened");
        }
    }
}
