package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.service.NetworkService;

public class GDXOnlineMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private NetworkService networkService;

    public GDXOnlineMenu(NetworkService networkService) {
        this.networkService = networkService;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Online Multiplayer", skin);
        titleLabel.setFontScale(1.5f);

        TextButton createLobbyButton = new TextButton("Create Lobby", skin);
        TextButton onlineLobbiesButton = new TextButton("Online Lobbies", skin);
        TextButton backButton = new TextButton("Back", skin);

        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXCreateLobbyMenu(networkService));
            }
        });

        onlineLobbiesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineLobbiesMenu());
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXMainMenu(com.example.main.models.App.getInstance().getNetworkService()));
            }
        });

        table.add(titleLabel).padBottom(30).row();
        table.add(createLobbyButton).width(250).height(50).pad(10).row();
        table.add(onlineLobbiesButton).width(250).height(50).pad(10).row();
        table.add(backButton).width(250).height(50).pad(10).row();
    }

    @Override
    public void show() {
        // Update title with current user
        String username = com.example.main.models.App.getInstance().getCurrentUser() != null ? 
            com.example.main.models.App.getInstance().getCurrentUser().getUsername() : "Guest";
        titleLabel.setText("Online Multiplayer - " + username);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
} 