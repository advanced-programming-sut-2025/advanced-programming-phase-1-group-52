package com.example.main.GDXviews;

import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.NetworkLobbyController;

public class GDXCreateLobbyMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Label statusLabel;
    
    private TextField lobbyNameField;
    private CheckBox privateLobbyCheckBox;
    private TextField passwordField;
    private CheckBox visibleLobbyCheckBox;
    
    private NetworkLobbyController controller;

    public GDXCreateLobbyMenu() {
        controller = new NetworkLobbyController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Create Lobby", skin);
        titleLabel.setFontScale(1.5f);
        
        statusLabel = new Label("", skin);

        // Lobby name input
        Label nameLabel = new Label("Lobby Name:", skin);
        lobbyNameField = new TextField("", skin);
        lobbyNameField.setMessageText("Enter lobby name...");

        // Privacy settings
        Label privacyLabel = new Label("Privacy Settings:", skin);
        privateLobbyCheckBox = new CheckBox("Private Lobby", skin);
        
        Label passwordLabel = new Label("Password (if private):", skin);
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password...");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // Visibility settings
        Label visibilityLabel = new Label("Visibility Settings:", skin);
        visibleLobbyCheckBox = new CheckBox("Visible to others", skin);
        visibleLobbyCheckBox.setChecked(true);

        // Buttons
        TextButton createButton = new TextButton("Create Lobby", skin);
        TextButton backButton = new TextButton("Back", skin);

        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobby();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineMenu());
            }
        });

        // Layout
        table.add(titleLabel).padBottom(20).row();
        table.add(statusLabel).padBottom(20).row();
        
        // Lobby name section
        table.add(nameLabel).left().pad(5).row();
        table.add(lobbyNameField).width(300).pad(5).row();
        
        // Privacy section
        table.add(privacyLabel).left().pad(5).row();
        table.add(privateLobbyCheckBox).left().pad(5).row();
        table.add(passwordLabel).left().pad(5).row();
        table.add(passwordField).width(300).pad(5).row();
        
        // Visibility section
        table.add(visibilityLabel).left().pad(5).row();
        table.add(visibleLobbyCheckBox).left().pad(5).row();
        
        // Buttons
        table.add(createButton).width(200).height(40).pad(10).row();
        table.add(backButton).width(200).height(40).pad(10).row();
    }

    private void createLobby() {
        String lobbyName = lobbyNameField.getText().trim();
        if (lobbyName.isEmpty()) {
            statusLabel.setText("Please enter a lobby name!");
            return;
        }

        boolean isPrivate = privateLobbyCheckBox.isChecked();
        String password = passwordField.getText().trim();
        
        if (isPrivate && password.isEmpty()) {
            statusLabel.setText("Please enter a password for private lobby!");
            return;
        }

        boolean isVisible = visibleLobbyCheckBox.isChecked();
        
        // Generate unique lobby ID
        String lobbyId = UUID.randomUUID().toString();
        
        // Create lobby with settings
        boolean success = controller.createLobbyWithSettings(lobbyId, lobbyName, isPrivate, password, isVisible);
        
        if (success) {
            statusLabel.setText("Lobby created successfully!");
            // Navigate to the lobby screen
            Main.getInstance().setScreen(new GDXLobbyScreen(lobbyId, lobbyName, true));
        } else {
            statusLabel.setText("Failed to create lobby!");
        }
    }

    @Override
    public void show() {
        // Connect to server when screen is shown
        if (Main.isNetworkMode()) {
            boolean connected = controller.connectToServer(Main.getServerIp(), Main.getServerPort());
            if (connected) {
                statusLabel.setText("Connected to server!");
            } else {
                statusLabel.setText("Failed to connect to server!");
            }
        }
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