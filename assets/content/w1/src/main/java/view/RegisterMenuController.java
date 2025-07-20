package view;

import controller.ApplicationController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterMenuController {
    
    public Label welcomeText;
    public TextField username;
    @FXML
    public void initialize() {
        changeLabel();
    }
    public void changeLabel() {
        username.textProperty().addListener((observableValue, s, t1) -> {
            welcomeText.setText("Welcome " + username.getText());
        });
    }
    
    public void register (MouseEvent mouseEvent) {
        try {
            new GameLauncher(username.getText()).start(ApplicationController.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
