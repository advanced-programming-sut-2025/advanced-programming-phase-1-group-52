package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Cannon extends Rectangle {
    public final double WIDTH = 100;
    public final double HEIGHT = 100;
    public final Game game;
    
    public Cannon(Game game) {
        super(100, 100);
        this.game = game;
        setX((game.WIDTH - WIDTH) / 2);
        setY(game.HEIGHT - WIDTH - 50);
        setFill(new ImagePattern(new Image(Cannon.class.getResource("/Images/cannon.png").toExternalForm())));
    }
}
