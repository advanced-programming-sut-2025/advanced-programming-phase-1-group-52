package com.example.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.example.main.GDXmodels.DatabaseManager;
import com.example.main.GDXviews.GDXSignUpMenu;
import com.example.main.views.AppView;

public class Main extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static Main main;
    private static SpriteBatch batch;
    private static DatabaseManager databaseManager;

    public static void main(String[] args) {
        (new AppView()).runProgram();
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        databaseManager = new DatabaseManager();

        this.setScreen(new GDXSignUpMenu());
    }

    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        super.dispose();
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static Main getInstance() {
        return main;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
