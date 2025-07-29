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
import com.example.main.models.App;

public class GDXNetworkLobby implements Screen {
    private Stage stage;
    private Skin skin;
    private NetworkLobbyController controller;
    
    private Label statusLabel;
    private Label lobbyLabel;
    private Table onlineUsersTable;
    private Table lobbyPlayersTable;
    private TextButton inviteButton;
    private TextButton startGameButton;
    private TextButton backButton;
    private TextButton refreshButton;
    
    private List<String> onlineUsers = new ArrayList<>();
    private List<String> lobbyPlayers = new ArrayList<>();
    private String selectedUser = null;
    
    public GDXNetworkLobby() {
        controller = new NetworkLobbyController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        
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
        
        // Online users section
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
        inviteButton = new TextButton("Invite Selected User", skin);
        inviteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedUser != null) {
                    inviteUser(selectedUser);
                }
            }
        });
        
        startGameButton = new TextButton("Start Game (4 Players Required)", skin);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (lobbyPlayers.size() >= 4) {
                    startGame();
                } else {
                    statusLabel.setText("Need 4 players to start game!");
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
                Main.getInstance().setScreen(new GDXMainMenu());
            }
        });
        
        buttonTable.add(inviteButton).pad(5);
        buttonTable.add(startGameButton).pad(5);
        buttonTable.add(refreshButton).pad(5);
        buttonTable.add(backButton).pad(5);
        
        mainTable.add(buttonTable).colspan(2).pad(10).center();
    }
    
    private void updateOnlineUsers() {
        // Clear current online users
        onlineUsersTable.clear();
        onlineUsers.clear();
        
        // Get online users from controller
        List<String> users = controller.getOnlineUsers();
        onlineUsers.addAll(users);
        
        // Add users to table
        String currentUsername = App.getInstance().getCurrentUser() != null ? App.getInstance().getCurrentUser().getUsername() : "Guest";
        for (String username : onlineUsers) {
            if (!username.equals(currentUsername)) {
                TextButton userButton = new TextButton(username, skin);
                userButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedUser = username;
                        statusLabel.setText("Selected: " + username);
                    }
                });
                onlineUsersTable.add(userButton).width(280).pad(2);
                onlineUsersTable.row();
            }
        }
        
        statusLabel.setText("Connected to server. Online users: " + onlineUsers.size());
    }
    
    private void updateLobbyPlayers() {
        lobbyPlayersTable.clear();
        lobbyPlayers.clear();
        
        // Get lobby players from controller
        List<String> players = controller.getLobbyPlayers();
        lobbyPlayers.addAll(players);
        
        // Add players to table
        for (String username : lobbyPlayers) {
            Label playerLabel = new Label(username, skin);
            lobbyPlayersTable.add(playerLabel).width(280).pad(2);
            lobbyPlayersTable.row();
        }
        
        lobbyLabel.setText("Lobby Players (" + lobbyPlayers.size() + "/4)");
        
        // Enable/disable start button
        startGameButton.setDisabled(lobbyPlayers.size() < 4);
    }
    
    private void inviteUser(String username) {
        boolean success = controller.inviteUser(username);
        if (success) {
            statusLabel.setText("Invitation sent to " + username);
        } else {
            statusLabel.setText("Failed to invite " + username);
        }
    }
    
    private void startGame() {
        boolean success = controller.startGame();
        if (success) {
            statusLabel.setText("Starting game...");
            // Transition to game screen
            Main.getInstance().setScreen(new GDXGameScreen());
        } else {
            statusLabel.setText("Failed to start game");
        }
    }
    
    @Override
    public void show() {
        // Connect to server when screen is shown
        if (Main.isNetworkMode()) {
            boolean connected = controller.connectToServer(Main.getServerIp(), Main.getServerPort());
            if (connected) {
                String currentUsername = App.getInstance().getCurrentUser() != null ? App.getInstance().getCurrentUser().getUsername() : "Guest";
                boolean authenticated = controller.authenticate(
                    currentUsername,
                    "password" // You'll need to implement proper password handling
                );
                if (authenticated) {
                    statusLabel.setText("Connected and authenticated!");
                    updateOnlineUsers();
                } else {
                    statusLabel.setText("Authentication failed!");
                }
            } else {
                statusLabel.setText("Failed to connect to server!");
            }
        }
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update lobby players periodically
        updateLobbyPlayers();
        
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