package view;

import controller.ApplicationController;
import controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cannon;
import model.Game;
import model.Meteor;
import view.animations.MeteorAnimation;

public class GameLauncher extends Application {
    public final double WIDTH = 500;
    public final double HEIGHT = 800;
    public Cannon cannon;
    public final Game game;
    public Pane pane;
    public Timeline createMeteors;
    public Text scoreBoard;
    
    public GameLauncher(String username) {
        game = new Game(username, this);
    }
    @Override
    public void start (Stage stage) throws Exception {
        pane = new Pane();
        setSize(pane);
    
//        setBackground(pane);
    
        createCannon();
        pane.getChildren().add(cannon);
        pane.setBackground(new Background(createBackgroundImage()));
        pane.getChildren().add(game.meteors);
        createMeteors = new Timeline(new KeyFrame(Duration.seconds(2), actionEvent -> createMeteor()));
        createMeteors.setCycleCount(-1);
        createMeteors.play();
    
        pane.getChildren().add(createScoreHbox());
        
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        cannon.requestFocus();
    }
    
    private void createMeteor() {
        Meteor meteor = new Meteor(ApplicationController.random.nextDouble(50, game.WIDTH - 100), 30);
        meteor.setMeteorAnimation(new MeteorAnimation(pane, game, cannon, meteor));
        game.animations.add(meteor.getMeteorAnimation());
        meteor.getMeteorAnimation().play();
        game.meteors.getChildren().add(meteor);
    }
    
    private void createCannon () {
        cannon = new Cannon(game);
        cannon.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                GameController.moveRight(cannon, WIDTH);
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                GameController.moveLeft(cannon, WIDTH);
            } else if (keyEvent.getCode() == KeyCode.SPACE) {
                GameController.shoot(pane, cannon, game);
            }
        });
    }
    
    private void setSize (Pane pane) {
        pane.setMinHeight(HEIGHT);
        pane.setMaxHeight(HEIGHT);
        pane.setMinWidth(WIDTH);
        pane.setMaxWidth(WIDTH);
    }
    
    private HBox createScoreHbox() {
        HBox hBox = new HBox();
        scoreBoard = new Text(360, 50,
                game.username + "'s score: 0");
        scoreBoard.setFill(Color.OLIVE);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(scoreBoard);
        hBox.setLayoutX(10);
        return hBox;
    }
    
    private void setBackground (Pane pane) {
        Image backgroundImage = new Image(GameLauncher.class.getResource("/Images/envirenement.png").toExternalForm());
        ImageView background = new ImageView(backgroundImage);
        background.setX((game.WIDTH / 2) - (backgroundImage.getWidth() / 2));
        pane.getChildren().add(background);
    }
    
    private BackgroundImage createBackgroundImage () {
        Image image = new Image(Game.class.getResource("/Images/envirenement.png").toExternalForm(), WIDTH ,HEIGHT, false, false);
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        return backgroundImage;
    }
}
