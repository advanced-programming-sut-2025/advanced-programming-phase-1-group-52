package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.PreGameMenuController;
import com.example.main.models.App;
import com.example.main.models.Result;

public class GDXPreGameMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private PreGameMenuController controller;

    private TextField username2Field, username3Field, username4Field;
    private SelectBox<String> map1Select, map2Select, map3Select, map4Select;
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

        username2Field = new TextField("", skin);
        username2Field.setMessageText("Player 2 Username");

        username3Field = new TextField("", skin);
        username3Field.setMessageText("Player 3 Username");

        username4Field = new TextField("", skin);
        username4Field.setMessageText("Player 4 Username");

        String[] mapOptions = {"Neutral", "Miner", "Fisher"};
        map1Select = new SelectBox<>(skin);
        map1Select.setItems(mapOptions);
        map2Select = new SelectBox<>(skin);
        map2Select.setItems(mapOptions);
        map3Select = new SelectBox<>(skin);
        map3Select.setItems(mapOptions);
        map4Select = new SelectBox<>(skin);
        map4Select.setItems(mapOptions);

        newGameButton = new TextButton("Start New Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        backButton = new TextButton("Back to Main Menu", skin);

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String user2 = username2Field.getText();
                String user3 = username3Field.getText();
                String user4 = username4Field.getText();
                int map1 = map1Select.getSelectedIndex();
                int map2 = map2Select.getSelectedIndex();
                int map3 = map3Select.getSelectedIndex();
                int map4 = map4Select.getSelectedIndex();
                Result result = controller.startNewGame(user2, user3, user4, map1, map2, map3, map4);
                messageLabel.setText(result.Message());
                if (result.isSuccessful()) {
                    Main.getInstance().setScreen(new GDXGameScreen());
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
        table.add(messageLabel).colspan(3).pad(10).center();
        table.row();
        table.add(new Label("Player 1 (You):", skin)).left();
        table.add(new Label(App.getInstance().getCurrentUser().getUsername(), skin)).pad(5);
        table.add(map1Select).width(150).pad(5);
        table.row();
        table.add(new Label("Player 2:", skin)).left();
        table.add(username2Field).width(250).pad(5);
        table.add(map2Select).width(150).pad(5);
        table.row();
        table.add(new Label("Player 3:", skin)).left();
        table.add(username3Field).width(250).pad(5);
        table.add(map3Select).width(150).pad(5);
        table.row();
        table.add(new Label("Player 4:", skin)).left();
        table.add(username4Field).width(250).pad(5);
        table.add(map4Select).width(150).pad(5);
        table.row().padTop(20);
        table.add(newGameButton).width(200).pad(10).colspan(3).center();
        table.row();
        table.add(loadGameButton).width(200).pad(10).colspan(3).center();
        table.row();
        table.add(backButton).colspan(3).center().pad(10);
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
