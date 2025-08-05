package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.NetworkLobbyController;
import com.example.main.models.App;
import com.example.main.service.NetworkService;

public class GDXCreateLobbyMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final NetworkLobbyController controller;
    private final Label statusLabel;
    private final TextField lobbyNameField;
    private final CheckBox privateCheckBox;
    private final TextField passwordField;
    private final CheckBox visibleCheckBox;

    public GDXCreateLobbyMenu(NetworkService networkService) {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.controller = networkService.getLobbyController();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // ** THE FIX IS HERE **
        // Create the label using the default style, which is guaranteed to exist.
        Label titleLabel = new Label("Create a New Lobby", skin);
        // Then, scale it up to make it look like a title.
        titleLabel.setFontScale(1.5f);
        rootTable.add(titleLabel).padBottom(20).colspan(2).row();

        rootTable.add(new Label("Lobby Name:", skin)).left();
        lobbyNameField = new TextField("", skin);
        rootTable.add(lobbyNameField).width(300).pad(5).row();

        privateCheckBox = new CheckBox(" Private Lobby", skin);
        rootTable.add(privateCheckBox).left().padTop(10).colspan(2).row();

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password...");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setVisible(false);
        rootTable.add(new Label("Password:", skin)).left();
        rootTable.add(passwordField).width(300).pad(5).row();

        visibleCheckBox = new CheckBox(" Visible in Public List", skin);
        visibleCheckBox.setChecked(true);
        rootTable.add(visibleCheckBox).left().padTop(10).colspan(2).row();

        privateCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                passwordField.setVisible(privateCheckBox.isChecked());
            }
        });

        statusLabel = new Label("", skin);
        rootTable.add(statusLabel).padTop(10).colspan(2).row();

        TextButton createButton = new TextButton("Create Lobby", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobby();
            }
        });

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineMenu(App.getInstance().getNetworkService()));
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(createButton).width(200).height(50).pad(10);
        buttonTable.add(backButton).width(200).height(50).pad(10);
        rootTable.add(buttonTable).padTop(20).colspan(2);
    }

    private void createLobby() {
        String lobbyName = lobbyNameField.getText().trim();
        boolean isPrivate = privateCheckBox.isChecked();
        String password = passwordField.getText();
        boolean isVisible = visibleCheckBox.isChecked();

        if (lobbyName.isEmpty()) {
            statusLabel.setText("Lobby name cannot be empty.");
            return;
        }

        if (isPrivate && password.isEmpty()) {
            statusLabel.setText("A private lobby must have a password.");
            return;
        }

        statusLabel.setText("Creating lobby...");
        controller.createLobby(lobbyName, isPrivate, password, isVisible);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
