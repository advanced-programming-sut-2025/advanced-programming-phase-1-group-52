package model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import view.animations.MeteorAnimation;

public class Meteor extends Rectangle {
    public final double WIDTH = 50;
    public final double HEIGHT = 50;
    public boolean isHit = false;
    
    private MeteorAnimation meteorAnimation;
    
    public MeteorAnimation getMeteorAnimation () {
        return meteorAnimation;
    }
    
    public void setMeteorAnimation (MeteorAnimation meteorAnimation) {
        this.meteorAnimation = meteorAnimation;
    }
    
    public Meteor (double x, double y) {
        super(x, y, 50, 50);
        
        
        this.setFill(new ImagePattern(new Image
                (Meteor.class.getResource("/Images/meteor.png").toExternalForm())));
    }
    
    public void stopTheAnimation() {
        meteorAnimation.stop();
    }
}
