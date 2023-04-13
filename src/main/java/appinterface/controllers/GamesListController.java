package appinterface.controllers;


import exceptions.IncorrectCardException;
import exceptions.IncorrectHandException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;

import java.io.File;
import java.net.URL;
import java.util.*;

public class GamesListController {

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
    void onUploadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Files");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TextFiles", "*.txt"));
//            fileChooser.setSelectedExtensionFilter();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadButton.getParent().getScene().getWindow());
        if (selectedFiles != null) {
            UploadController uploadController = new UploadController();
            uploadController.uploadFiles(selectedFiles);

            gamesTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
            gamesTableView.getItems().addAll();
        }
    }

    @FXML private void initializeTable() {
//        Game example = new Game("R", 12);

        gamesTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        gamesTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("gameId"));
        gamesTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("BigBlindSize$"));
        gamesTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("finalPot"));

//        gamesTableView.getItems().addAll(example);
    }

    @FXML
    void initialize() throws IncorrectHandException, IncorrectCardException {
        uploadButton.setOnAction(actionEvent -> {
            onUploadButtonClick();
        });

        initializeTable();
//        Game game = new Game("", 0);

//        TableView table = new TableView<Game>();
//        TableColumn firstNameColumn = new TableColumn<Game, Date>("firstName");
//        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Game, Date>("firstName"));
//
//        TableColumn lastNameColumn = new TableColumn<Person, String>("lastName");
//        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
//
//        table.getColumns().addAll(firstNameColumn, lastNameColumn);

//        ((BorderPane)gamesTableView.getParent()).setCenter(table);



//        TableColumn<Game, Date> tb = gamesTableView.getColumns().get(1).set;
//        tb.setCellFactory(new PropertyValueFactory<Game, String>("gameId"));

//        gamesTableView.getColumns().get(0).setCellFactory(new PropertyValueFactory("date"));
//        ((TableColumn)(gamesTableView.getColumns().get(1))).setCellFactory(new PropertyValueFactory<Game, Date>("date"));
//        ((TableColumn)(gamesTableView.getColumns().get(1))).setCellFactory(new PropertyValueFactory<Game, String>("gameId"));
//        ((TableColumn)(gamesTableView.getColumns().get(2))).setCellFactory(new PropertyValueFactory<Game, Double>("BigBlindSize$"));
//        ((TableColumn)(gamesTableView.getColumns().get(3))).setCellFactory(new PropertyValueFactory<Game, Double>("finalPot"));
//            gamesTableView.getItems().add(new Game("game1", 15));

    }

}
