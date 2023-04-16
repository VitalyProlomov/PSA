package appinterface.controllers;


import appinterface.PSAApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.IncorrectBoardException;
import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Game;
import models.GamesSet;

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

    @FXML
    private Button searchFiltersButton;

    @FXML
    private Button profileButton;

    private
    GamesSet gamesSet;


    @FXML
    void initialize() throws IncorrectHandException, IncorrectCardException, IncorrectBoardException, IOException {
        uploadButton.setOnAction(actionEvent -> onUploadButtonClick());

        initializeTable();
        initializeSerializedSavedGames();
        searchFiltersButton.setOnMouseClicked(actionEvent -> onSearchFiltersButtonClick());

        profileButton.setOnMouseClicked(action -> {
            onProfileButtonClicked();
        });

    }

    @FXML
    void onProfileButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(PSAApplication.class.getResource("views/profileView.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            double heroWinloss = 0;
            for (Game g : gamesSet.getGames()) {
                heroWinloss += g.getHeroWinloss();
            }
            int gamesAmount = gamesSet.getGames().size();

            ProfileController controller = loader.getController();
            controller.setInfo(heroWinloss, gamesAmount);
            stage.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not open Profile. Try reopening the app.");
            alert.show();
        }

    }

    @FXML
    void onSearchFiltersButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(PSAApplication.class.getResource("views/filterSearchView.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            FilterSearchController controller = loader.getController();
            stage.setOnHiding(event -> {
                if (controller.getGamesAfterFilter() != null) {
                    updateTable(controller.getGamesAfterFilter());
                }
            });
            controller.setUnfilteredGames(gamesSet.getGames());
            stage.show();
//        filterSearchController.searchFilteredGames()
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not open filter search");

        }

    }


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
                gamesSet.addGames(new HashSet<>(addedGames));

                updateTable(gamesSet.getGames());

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


    @FXML
    private void initializeTable() {
        gamesTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        gamesTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("gameId"));
        gamesTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("BigBlindSize$"));
        gamesTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("finalPot"));

        gamesTableView.setRowFactory(tv -> {
            TableRow<Game> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        Game rowData = row.getItem();

                        FXMLLoader loader = new FXMLLoader(PSAApplication.class.getResource("views/gameDisplayView.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load()));
                        GameDisplayController controller = loader.getController();
                        controller.setGame(rowData);
                        stage.show();
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Could not properly load game page.");
                        alert.show();
                    }
                }
            });
            return row;
        });

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

    private void updateTable(Set<Game> gamesToShow) {
        gamesTableView.getItems().clear();
        if (gamesToShow == null) {
            return;
        }

        gamesTableView.getItems().addAll(gamesToShow);
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
