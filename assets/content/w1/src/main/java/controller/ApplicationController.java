package controller;

import javafx.stage.Stage;

import java.util.Random;

public class ApplicationController {
    private static Stage stage;
    public static final Random random = new Random();
    
    public static void setStage (Stage stage) {
        ApplicationController.stage = stage;
    }
    
    public static Stage getStage () {
        return stage;
    }
}
