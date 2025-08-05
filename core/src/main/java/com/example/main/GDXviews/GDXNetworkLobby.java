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
import com.example.main.controller.NetworkLobbyController;
import com.example.main.models.App;
import com.example.main.service.NetworkService; // Assuming you have this import

import java.util.ArrayList;
import java.util.List;

public class GDXNetworkLobby implements Screen {
    private Stage stage;
    private Skin skin;
    private NetworkLobbyController controller;

    private Label statusLabel;
    private Label lobbyLabel;
    private Table onlineUsersTable;
    private Table lobbyPlayersTable;
    private TextButton leaveLobbyButton;
    private TextButton startGameButton;
    private TextButton backButton;
    private TextButton refreshButton;

    private List<String> onlineUsers = new ArrayList<>();
    private List<String> lobbyPlayers = new ArrayList<>();
    private boolean isAdmin = false;

    public GDXNetworkLobby() {
        // Use the existing, connected NetworkService from the App singleton
        controller = new NetworkLobbyController(App.getInstance().getNetworkService());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Set up callback for online users updates
        controller.setOnlineUsersUpdateCallback(users -> Gdx.app.postRunnable(() -> updateOnlineUsersFromServer(users)));

        createUI();
        updateOnlineUsers();
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Status label
        statusLabel = new Label("Connecting to server...", skin);
        mainTable.add(statusLabel).colspan(2).pad(10).center();
        mainTable.row();

        // Lobby label
        lobbyLabel = new Label("Network Lobby", skin);
        mainTable.add(lobbyLabel).colspan(2).pad(10).center();
        mainTable.row();

        // Section Labels
        mainTable.add(new Label("Online Users:", skin)).left().pad(5);
        mainTable.add(new Label("Lobby Players:", skin)).left().pad(5);
        mainTable.row();

        // Online users table
        onlineUsersTable = new Table();
        onlineUsersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));
        mainTable.add(onlineUsersTable).width(300).height(200).pad(5);

        // Lobby players table
        lobbyPlayersTable = new Table();
        lobbyPlayersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));
        mainTable.add(lobbyPlayersTable).width(300).height(200).pad(5);
        mainTable.row();

        // Buttons
        Table buttonTable = new Table();
        leaveLobbyButton = new TextButton("Leave Lobby", skin);
        leaveLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaveLobby();
            }
        });

        startGameButton = new TextButton("Start Game", skin);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (lobbyPlayers.size() >= 2 && isAdmin) { // Changed to 2 for easier testing
                    startGame();
                } else if (!isAdmin) {
                    statusLabel.setText("Only the admin can start the game!");
                } else {
                    statusLabel.setText("Need at least 2 players to start game!");
                }
            }
        });

        refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateOnlineUsers();
            }
        });

        backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.disconnect();
                Main.getInstance().setScreen(new GDXMainMenu(App.getInstance().getNetworkService()));
            }
        });

        buttonTable.add(leaveLobbyButton).pad(5);
        buttonTable.add(startGameButton).pad(5);
        buttonTable.add(refreshButton).pad(5);
        buttonTable.add(backButton).pad(5);

        mainTable.add(buttonTable).colspan(2).pad(10).center();
    }

    private void updateOnlineUsers() {
        statusLabel.setText("Requesting online users from server...");
        controller.getOnlineUsers();
    }

    public void updateOnlineUsersFromServer(List<String> users) {
        onlineUsersTable.clear();
        onlineUsers.clear();
        onlineUsers.addAll(users);

        String currentUsername = App.getInstance().getCurrentUser() != null ? App.getInstance().getCurrentUser().getUsername() : "";
        for (String username : onlineUsers) {
            // Only show users who are not the current player
            if (!username.equals(currentUsername)) {
                Label userLabel = new Label(username, skin);
                onlineUsersTable.add(userLabel).width(280).pad(2);
                onlineUsersTable.row();
            }
        }
        statusLabel.setText("Online users refreshed. Total online: " + onlineUsers.size());
    }

    private void updateLobbyPlayers() {
        lobbyPlayersTable.clear();
        lobbyPlayers.clear();

        List<String> players = controller.getLobbyPlayers();
        lobbyPlayers.addAll(players);

        for (String username : lobbyPlayers) {
            Label playerLabel = new Label(username, skin);
            lobbyPlayersTable.add(playerLabel).width(280).pad(2);
            lobbyPlayersTable.row();
        }

        lobbyLabel.setText("Lobby Players (" + lobbyPlayers.size() + "/4)");
        startGameButton.setDisabled(lobbyPlayers.size() < 2 || !isAdmin); // Update start button state
    }

    private void startGame() {
        boolean success = controller.startGame();
        if (success) {
            statusLabel.setText("Starting game...");
            Main.getInstance().setScreen(new GDXGameScreen());
        } else {
            statusLabel.setText("Failed to start game. Check conditions.");
        }
    }

    private void leaveLobby() {
        boolean success = controller.leaveLobby();
        if (success) {
            statusLabel.setText("Left lobby successfully!");
            // You might want to navigate back to the online lobbies menu
            Main.getInstance().setScreen(new GDXOnlineLobbiesMenu());
        } else {
            statusLabel.setText("Failed to leave lobby!");
        }
    }

    @Override
    public void show() {
        if (controller.isConnected()) {
            statusLabel.setText("Connected to server!");
            updateOnlineUsers();
            updateLobbyPlayers(); // Also update lobby players when screen is shown
        } else {
            statusLabel.setText("Error: Not connected to server!");
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
        skin.dispose();
    }
}
