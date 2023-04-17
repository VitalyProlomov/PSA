package appinterface.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import appinterface.PSAApplication;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Game;
import models.GamesSet;
import models.UserProfile;
import models.UserProfileSet;
import org.controlsfx.control.textfield.CustomTextField;

public class ExaminePlayersController {
    @FXML
    private CustomTextField searchIdCustomTextField;

    @FXML
    private Button assignNewPlayerButton;


    @FXML
    private TableView<UserProfile> userProfilesTableView;


    private GamesSet gamesSet;

    public void setGameSet(GamesSet set) {
        this.gamesSet = set;
    }

    private UserProfileSet userProfileSet;

    private void onAssignNewPlayerMouseClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(PSAApplication.class.getResource("views/assignNewPlayerView.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);
            AssignNewPlayerController controller = loader.getController();
            HashSet<UserProfile> newUserProfiles;
            stage.getScene().getWindow().setOnHiding(event -> assignNewUsers(controller));
            controller.setGamesSet(gamesSet);
            stage.show();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not open filter search");
            alert.show();
        }
    }

    private void assignNewUsers(AssignNewPlayerController controller) {
        for (UserProfile user : controller.getNewlyAssignedUsers()) {
            userProfileSet.addUser(user);
            System.out.println(user.getUserName() + ": " + user.getAllGamesIds());
        }

        updateTable();

    }

    private void updateTable() {
        userProfilesTableView.getItems().clear();
        userProfilesTableView.getItems().addAll(userProfileSet.getIdUserMap().values());
    }

    private void initializeTable() {
        userProfilesTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        userProfilesTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("gamesAmount"));

        // Setting clinking the row to open gameDisplayView.
       userProfilesTableView.setRowFactory(tv -> {
            TableRow<UserProfile> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        UserProfile rowData = row.getItem();

//                        FXMLLoader loader = new FXMLLoader(PSAApplication.class.getResource("views/gameDisplayView.fxml"));
//                        Stage stage = new Stage();
//                        stage.setScene(new Scene(loader.load()));
//                        GameDisplayController controller = loader.getController();
//                        controller.setGame(rowData);
//                        stage.setResizable(false);
//                        stage.show();
                    } catch (Exception ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Could not properly load User profile window.");
                        alert.show();
                    }
                }
            });
            return row;
        });

    }

    @FXML
    void initialize() {
        userProfileSet = new UserProfileSet();
        initializeTable();

        searchIdCustomTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    searchIdCustomTextField.textProperty().set("gggggggg");
                }
            }
        });
        assignNewPlayerButton.setOnMouseClicked(action -> onAssignNewPlayerMouseClicked());

    }

}
