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

    private final String lobbyName;

    private final Table playersTable;
    private final Table onlinePlayersTable;
    private final List<String> lobbyPlayers = new ArrayList<>();
    private List<String> onlinePlayers = new ArrayList<>();

    private final NetworkLobbyController controller;

    public GDXLobbyScreen(String lobbyId, String lobbyName) {
        this.lobbyName = lobbyName;

        // **FIX 1**: Use the existing, connected NetworkService from the App singleton
        this.controller = App.getInstance().getNetworkService().getLobbyController();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Lobby: " + this.lobbyName, skin);
        titleLabel.setFontScale(1.2f);

        statusLabel = new Label("Welcome! Waiting for players...", skin);

        // --- UI Layout (Largely the same) ---
        Label lobbyPlayersLabel = new Label("Players in Lobby:", skin);
        playersTable = new Table();
        playersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));

        Label onlinePlayersLabel = new Label("Online Players:", skin);
        onlinePlayersTable = new Table();
        onlinePlayersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));

        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton leaveButton = new TextButton("Leave Lobby", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshOnlinePlayers();
            }
        });

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

        table.add(titleLabel).padBottom(10).row();
        table.add(statusLabel).padBottom(10).row();

        Table contentTable = new Table();
        Table leftColumn = new Table();
        leftColumn.add(lobbyPlayersLabel).pad(5).row();
        leftColumn.add(playersTable).width(300).height(200).pad(5).row();

        Table rightColumn = new Table();
        rightColumn.add(onlinePlayersLabel).pad(5).row();
        rightColumn.add(onlinePlayersTable).width(300).height(200).pad(5).row();

        contentTable.add(leftColumn).pad(10);
        contentTable.add(rightColumn).pad(10);
        table.add(contentTable).row();

        Table buttonTable = new Table();
        buttonTable.add(refreshButton).width(120).height(40).pad(5);
        buttonTable.add(startGameButton).width(120).height(40).pad(5);
        buttonTable.add(leaveButton).width(120).height(40).pad(5);
        table.add(buttonTable).pad(10).row();

        // **FIX 2**: The faulty updateAdminStatus() call is removed.
    }

    private void startGame() {
        // **FIX 4**: The screen's method now directly calls the controller.
        // The controller itself handles the logic and provides the status message.
        boolean success = controller.startGame();
        if (success) {
            statusLabel.setText("Starting game...");
            Main.getInstance().setScreen(new GDXGameScreen());
        } else {
            statusLabel.setText("Failed to start game! Only the host can start with 2+ players.");
        }
    }

    private void leaveLobby() {
        boolean success = controller.leaveLobby();
        if (success) {
            statusLabel.setText("Left lobby successfully!");
            Main.getInstance().setScreen(new GDXOnlineMenu(App.getInstance().getNetworkService()));
        } else {
            statusLabel.setText("Failed to leave lobby!");
        }
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

    private void updateOnlinePlayersTable() {
        onlinePlayersTable.clear();
        onlinePlayersTable.add(new Label("Username", skin)).expandX().left().pad(5);
        onlinePlayersTable.row();

        String currentUsername = App.getInstance().getCurrentUser() != null ?
            App.getInstance().getCurrentUser().getUsername() : "";

        boolean playersFound = false;
        for (String player : onlinePlayers) {
            if (!player.equals(currentUsername) && !lobbyPlayers.contains(player)) {
                onlinePlayersTable.add(new Label(player, skin)).expandX().left().pad(5);
                onlinePlayersTable.row();
                playersFound = true;
            }
        }

        if (!playersFound) {
            onlinePlayersTable.add(new Label("No other players online", skin)).center().pad(20);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (controller.isConnected()) {
            statusLabel.setText("Connected to server!");
            refreshOnlinePlayers();
        } else {
            statusLabel.setText("Error: Not connected to server!");
        }
    }

    private void updateOnlinePlayers() {
        onlinePlayers = controller.getOnlineUsers();
        updateOnlinePlayersTable();
    }

    // **FIX 3**: The updateAdminStatus method is now deleted.
    // private void updateAdminStatus() { ... }

    private void refreshOnlinePlayers() {
        statusLabel.setText("Refreshing online players...");
        updateOnlinePlayers();
        statusLabel.setText("Online players refreshed!");
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
        skin.dispose();
    }
}
