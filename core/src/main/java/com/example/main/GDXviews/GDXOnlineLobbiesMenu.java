package com.example.main.GDXviews;

import java.util.ArrayList;
import java.util.List;

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
import com.example.main.controller.NetworkLobbyController;

public class GDXOnlineLobbiesMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Label statusLabel;
    
    private Table lobbiesTable;
    private List<String> availableLobbies = new ArrayList<>();
    private String selectedLobby = null;
    
    private NetworkLobbyController controller;

    public GDXOnlineLobbiesMenu() {
        controller = new NetworkLobbyController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Online Lobbies", skin);
        titleLabel.setFontScale(1.5f);
        
        statusLabel = new Label("Loading lobbies...", skin);

        // Lobbies table
        lobbiesTable = new Table();
        lobbiesTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));

        // Buttons
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton joinButton = new TextButton("Join Selected", skin);
        TextButton backButton = new TextButton("Back", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshLobbies();
            }
        });

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinSelectedLobby();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineMenu(com.example.main.models.App.getInstance().getNetworkService()));
            }
        });

        // Layout
        table.add(titleLabel).padBottom(20).row();
        table.add(statusLabel).padBottom(10).row();
        table.add(lobbiesTable).width(600).height(400).pad(10).row();
        
        Table buttonTable = new Table();
        buttonTable.add(refreshButton).width(150).height(40).pad(5);
        buttonTable.add(joinButton).width(150).height(40).pad(5);
        buttonTable.add(backButton).width(150).height(40).pad(5);
        
        table.add(buttonTable).pad(10).row();
    }

    private void refreshLobbies() {
        statusLabel.setText("Refreshing lobbies...");
        // Request available lobbies from server
        controller.requestAvailableLobbies();
        
        // Clear existing lobbies - will be populated by server response
        availableLobbies.clear();
        updateLobbiesTable();
        statusLabel.setText("No lobbies available");
    }

    private void updateLobbiesTable() {
        lobbiesTable.clear();
        
        // Add header
        lobbiesTable.add(new Label("Lobby Name", skin)).width(200).pad(5);
        lobbiesTable.add(new Label("Players", skin)).width(100).pad(5);
        lobbiesTable.add(new Label("Status", skin)).width(100).pad(5);
        lobbiesTable.row();
        
        // Add lobbies or show message if none available
        if (availableLobbies.isEmpty()) {
            Label noLobbiesLabel = new Label("No lobbies available", skin);
            noLobbiesLabel.setColor(0.7f, 0.7f, 0.7f, 1f); // Gray color
            lobbiesTable.add(noLobbiesLabel).colspan(3).center().pad(20);
        } else {
            for (String lobby : availableLobbies) {
                TextButton lobbyButton = new TextButton(lobby, skin);
                lobbyButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedLobby = lobby;
                        statusLabel.setText("Selected: " + lobby);
                    }
                });
                
                lobbiesTable.add(lobbyButton).width(200).pad(5);
                lobbiesTable.add(new Label("2/4", skin)).width(100).pad(5);
                lobbiesTable.add(new Label("Public", skin)).width(100).pad(5);
                lobbiesTable.row();
            }
        }
    }

    private void joinSelectedLobby() {
        if (selectedLobby == null) {
            statusLabel.setText("Please select a lobby first!");
            return;
        }
        
        // Extract lobby ID from selected lobby (for now, use a simple approach)
        String lobbyId = "sample-lobby-id"; // This would come from the server
        String lobbyName = selectedLobby.split(" ")[0] + " " + selectedLobby.split(" ")[1];
        
        statusLabel.setText("Joining " + selectedLobby + "...");
        
        // Navigate to lobby screen
        Main.getInstance().setScreen(new GDXLobbyScreen(lobbyId, lobbyName, false));
    }

    @Override
    public void show() {
        // Connect to server when screen is shown
        if (Main.isNetworkMode()) {
            boolean connected = controller.connectToServer(Main.getServerIp(), Main.getServerPort());
            if (connected) {
                statusLabel.setText("Connected to server!");
                refreshLobbies();
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