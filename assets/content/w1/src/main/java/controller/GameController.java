package controller;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import model.Cannon;
import model.Game;
import model.Missile;
import view.animations.ShootingAnimation;

public class GameController {
    public static void moveLeft (Cannon cannon, double width) {
        cannon.setX(Math.max(cannon.getX() - 20, 10));
    }
    
    public static void moveRight (Cannon cannon, double width) {
        cannon.setX(Math.min(cannon.getX() + 20, width - cannon.WIDTH - 10));
    }
    
    public static void shoot (Pane pane, Cannon cannon, Game game) {
        Missile missile = new Missile(cannon);
        int cannonIndex = pane.getChildren().indexOf(cannon);
        pane.getChildren().add(cannonIndex, missile);
        ShootingAnimation shootingAnimation = new ShootingAnimation(pane, game, missile);
        game.animations.add(shootingAnimation);
        shootingAnimation.play();
    }
    
    public static void stopAnimations (Game game) {
        for (Transition animation : game.animations) {
            animation.stop();
        }
    }
}
