package view.animations;

import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import model.Meteor;

public class ExplosionAnimation extends Transition {
    private final Meteor meteor;
    private final Group meteors;
    private final Pane pane;

    public ExplosionAnimation (Meteor meteor, Pane pane, Group meteors) {
        this.meteor = meteor;
        this.pane = pane;
        this.meteors = meteors;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double v) {
        int number = 1;
        if (0 <= v && v <= 0.33) number = 1;
        else if (0.33 < v && v <= 0.66) number = 2;
        else if (0.66 < v && v <= 1) number = 3;

        meteor.setFill(new ImagePattern(new Image(
                MeteorAnimation.class.getResource("/Images/meteorCollapse" + number + ".png").toExternalForm())));

        this.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                meteors.getChildren().remove(meteor);
            }
        });

    }
}
