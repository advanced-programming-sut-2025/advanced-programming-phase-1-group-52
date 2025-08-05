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
import com.example.main.controller.NetworkLobbyController;
import com.example.main.models.App;
import com.example.main.service.NetworkService;
import java.util.ArrayList;
import java.util.List;

public class GDXLobbyScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final Label titleLabel;
    private final Label statusLabel;
    private final Table playersTable;
    private final List<String> lobbyPlayers = new ArrayList<>();
    private final NetworkLobbyController controller;

    public GDXLobbyScreen(String lobbyId, String lobbyName) {
        this.controller = App.getInstance().getNetworkService().getLobbyController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Lobby: " + lobbyName, skin);
        titleLabel.setFontScale(1.2f);
        table.add(titleLabel).padBottom(5).row();

        // **THE FIX**: Add a label to display the Lobby ID.
        Label idLabel = new Label("Lobby ID: " + lobbyId, skin);
        table.add(idLabel).padBottom(10).row();

        statusLabel = new Label("Waiting for players...", skin);
        table.add(statusLabel).padBottom(10).row();

        Label lobbyPlayersLabel = new Label("Players in Lobby:", skin);
        table.add(lobbyPlayersLabel).pad(5).row();
        playersTable = new Table();
        playersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));
        table.add(playersTable).width(400).height(200).pad(5).row();

        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton leaveButton = new TextButton("Leave Lobby", skin);

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });

        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaveLobby();
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(startGameButton).width(150).height(40).pad(5);
        buttonTable.add(leaveButton).width(150).height(40).pad(5);
        table.add(buttonTable).pad(10).row();
    }

    private void startGame() {
        if (!controller.startGame()) {
            statusLabel.setText("Failed to start game! Only the host can start with 2+ players.");
        } else {
            statusLabel.setText("Starting game...");
            // The server will send a message to all clients to start the game
        }
    }

    private void leaveLobby() {
        controller.leaveLobby();
        Main.getInstance().setScreen(new GDXOnlineMenu(App.getInstance().getNetworkService()));
    }

    public void updatePlayerList(List<String> newPlayerNames) {
        this.lobbyPlayers.clear();
        this.lobbyPlayers.addAll(newPlayerNames);

        playersTable.clear();
        String currentUsername = App.getInstance().getCurrentUser() != null ?
            App.getInstance().getCurrentUser().getUsername() : "Unknown";

        for (String playerName : lobbyPlayers) {
            String labelText = playerName;
            if (playerName.equals(currentUsername)) {
                labelText += " (You)";
            }
            Label playerLabel = new Label(labelText, skin);
            playersTable.add(playerLabel).expandX().left().pad(5);
            playersTable.row();
        }
    }

    @Override
    public void show() { Gdx.input.setInputProcessor(stage); }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
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
