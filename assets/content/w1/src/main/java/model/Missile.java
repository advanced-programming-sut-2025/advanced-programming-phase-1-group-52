package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Missile extends Rectangle{
    public final double WIDTH = 8;
    public final double HEIGHT = 10;
    
    public Missile(Cannon cannon) {
        super(8, 10);
        setX(cannon.getX() + cannon.WIDTH / 2 - WIDTH / 2);
        setY(cannon.getY() + 2);
        this.setFill(new ImagePattern(
                new Image(Missile.class.getResource("/Images/missile.png").toExternalForm())));
    }
}
