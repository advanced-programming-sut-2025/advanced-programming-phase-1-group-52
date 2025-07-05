package com.example.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.example.main.views.AppView;

public class Main extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    
    public static void main(String[] args) {
        (new AppView()).runProgram();
    }

    @Override
    public void create() {
        // initial set screen
        setScreen(new Screen() {
            private Stage stage;
            
            @Override
            public void show() {
                stage = new Stage(new FitViewport(WIDTH, HEIGHT));
                Gdx.input.setInputProcessor(stage);
                Table table = new Table();
                table.setFillParent(true);
                stage.addActor(table);

                Label label = new Label("Hello, World! This is libGDX Graphics Mode!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
                table.add(label).expand().center();
            }

            @Override
            public void render(float delta) {
                // Clear the screen
                ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1f);
                
                // Update and draw the stage
                stage.act(delta);
                stage.draw();
            }

            @Override
            public void dispose() {
                if (stage != null) {
                    stage.dispose();
                }
            }

            @Override
            public void hide() {
                // Called when this screen is no longer the current screen
            }

            @Override
            public void pause() {
                // Called when the application is paused
            }

            @Override
            public void resume() {
                // Called when the application is resumed
            }

            @Override
            public void resize(int width, int height) {
                stage.getViewport().update(width, height, true);
            }
        });
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}