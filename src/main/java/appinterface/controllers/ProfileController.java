package appinterface.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;

public class ProfileController {
    private static final int SIZE = 13;

    @FXML
    private Label profileLabel;

    @FXML
    private BorderPane borderPaneProfile;

    @FXML
    private Label gamesAmountLabel;

    @FXML
    private Label moneyWinlossLabel;

    @FXML
    private Label playersAssignedAmountLabel;

    private double heroWinLoss;

    private int gamesAmount;

    @FXML
    void initialize() {

//        VBox box = new VBox();
//        box.setSpacing(1);
//        for (int i = 0; i < SIZE; ++i) {
//            HBox row = new HBox();
//            row.setId("row_" + i);
//            row.setSpacing(1);
//            box.getChildren().add(row);
//        }
//
//        borderPaneProfile.setRight(box);
//
//        for (int i = 0; i < SIZE; ++i) {
//            for (int y = 0; y < SIZE; ++y) {
//                Rectangle r = new Rectangle(40, 40);
//                r.setFill(Color.WHITE);
//                ((HBox) box.getChildren().get(i)).getChildren().add(r);
//            }
//        }
    }

    public void setInfo(double heroWinLoss, int gamesAmount) {
        this.heroWinLoss = heroWinLoss;
        this.gamesAmount = gamesAmount;

        String balanceStr = new DecimalFormat("#0.00").format(heroWinLoss);
        balanceStr = balanceStr.replace(',', '.');

        moneyWinlossLabel.setText(moneyWinlossLabel.getText() + balanceStr + "$");
        gamesAmountLabel.setText(gamesAmountLabel.getText() + gamesAmount);
    }


}
