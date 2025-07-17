package view.animations;

import controller.ApplicationController;
import controller.GameController;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Cannon;
import model.Game;
import model.Meteor;

public class MeteorAnimation extends Transition {

    private final Game game;
    private final Cannon cannon;
    private final Pane pane;
    private final Meteor meteor;
    private final double acceleration = 0.05;
    private double vSpeed = 0;
    private double hSpeed;
    private final int duration = 100;

    public MeteorAnimation (Pane pane, Game game, Cannon cannon, Meteor meteor) {
        this.cannon = cannon;
        this.game = game;
        this.pane = pane;
        this.meteor = meteor;
        hSpeed = ApplicationController.random.nextDouble(-1, 1);
        
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(duration));
    }

    @Override
    protected void interpolate(double v) {
        double y = meteor.getY() + vSpeed;
        double x = meteor.getX() + hSpeed;
        vSpeed += acceleration; // vSpeed + acceleration

        if (meteor.getBoundsInParent().intersects(cannon.getBoundsInParent())) {
           collision(meteor, cannon);
        }

        if (y >= pane.getHeight() - meteor.HEIGHT - 80) {
            vSpeed = 2 * acceleration - vSpeed; // (-vSpeed' = -vSpeed - acceleration) + 2acceleration
            y = meteor.getY();
        }
        if (x <= 10 || x >= pane.getWidth() - meteor.WIDTH - 10) {
            hSpeed = -hSpeed;
            x = meteor.getX();
        }
        meteor.setY(y);
        meteor.setX(x);
    }

    private void collision(Meteor meteor, Cannon cannon) {
        game.meteors.getChildren().remove(meteor);
        this.stop();
        
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(cannon);
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.01);
        fadeTransition.play();

        try {
            game.gameLauncher.createMeteors.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cannon.setOnKeyPressed(keyEvent -> {});

        GameController.stopAnimations(game);

        System.out.println(game.username + "'s score: " + game.score);
    }
}
