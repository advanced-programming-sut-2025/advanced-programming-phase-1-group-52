package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.PreGameMenuController;
import com.example.main.models.Result;

public class GDXPreGameMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private PreGameMenuController controller;

    private TextField username1Field, username2Field, username3Field;
    private Label messageLabel;
    private TextButton newGameButton, loadGameButton, backButton;

    public GDXPreGameMenu() {
        controller = new PreGameMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        messageLabel = new Label("", skin);

        username1Field = new TextField("", skin);
        username1Field.setMessageText("Player 2 Username");

        username2Field = new TextField("", skin);
        username2Field.setMessageText("Player 3 Username");

        username3Field = new TextField("", skin);
        username3Field.setMessageText("Player 4 Username");

        newGameButton = new TextButton("Start New Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        backButton = new TextButton("Back to Main Menu", skin);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String user1 = username1Field.getText();
                String user2 = username2Field.getText();
                String user3 = username3Field.getText();
                Result result = controller.startNewGame(user1, user2, user3);
                messageLabel.setText(result.Message());
                if (result.isSuccessful()) {
                    // On success, you might navigate to a map selection screen or the game itself.
                    // For now, it shows a success message.
                }
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                messageLabel.setText("Load Game feature is not implemented yet.");
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXMainMenu());
            }
        });

        // Layout
        table.add(messageLabel).colspan(2).pad(10);
        table.row();
        table.add(new Label("Player 2:", skin)).left();
        table.add(username1Field).width(250).pad(5);
        table.row();
        table.add(new Label("Player 3:", skin)).left();
        table.add(username2Field).width(250).pad(5);
        table.row();
        table.add(new Label("Player 4:", skin)).left();
        table.add(username3Field).width(250).pad(5);
        table.row().padTop(20);
        table.add(newGameButton).width(200).pad(10);
        table.add(loadGameButton).width(200).pad(10);
        table.row();
        table.add(backButton).colspan(2).center().pad(10);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
