package view.animations;

import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Game;
import model.Meteor;
import model.Missile;

public class ShootingAnimation extends Transition {
    private final Game game;
    private final Pane pane;
    private final Missile missile;
    private final double speed = 4;
    private final int duration = 100;
    
    public ShootingAnimation (Pane pane, Game game, Missile missile) {
        this.pane = pane;
        this.game = game;
        this.missile = missile;
        this.setCycleDuration(Duration.millis(duration));
        this.setCycleCount(-1);
    }
    
    @Override
    protected void interpolate (double v) {
        double y = missile.getY() - speed;
    
        for (Node child : game.meteors.getChildren()) {
            Meteor meteor = (Meteor) child;
            if (meteor.getBoundsInParent().intersects(missile.getBoundsInParent())) {
    
                Media media = new Media(getClass().getResource("/Media/explosion.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                
                if (meteor.isHit) continue;
                meteor.isHit = true;
                
                game.score += 100;
                game.gameLauncher.scoreBoard.setText(game.username + "'s score: " + game.score);
                meteor.getMeteorAnimation().stop();
                
                ExplosionAnimation explosionAnimation = new ExplosionAnimation(meteor, pane, game.meteors);
                game.animations.add(explosionAnimation);
                explosionAnimation.play();
    
                pane.getChildren().remove(missile);
                this.stop();
                break;
            }
        }
    
        if (y <= 0) {
            pane.getChildren().remove(missile);
            this.stop();
        }
    
        missile.setY(y);
    }
}
