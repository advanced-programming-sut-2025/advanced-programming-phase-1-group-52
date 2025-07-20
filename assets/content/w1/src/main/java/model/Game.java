package model;

import javafx.animation.Transition;
import javafx.scene.Group;
import view.GameLauncher;

import java.util.ArrayList;

public class Game {
    public final double WIDTH = 500;
    public final double HEIGHT = 800;
    public String username;
    public int score = 0;
    public GameLauncher gameLauncher;
    public final Group meteors = new Group();
    public final ArrayList<Transition> animations = new ArrayList<>();
    public Game(String username, GameLauncher gameLauncher) {
        this.username = username;
        this.gameLauncher = gameLauncher;
    }
}
