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
import java.util.ArrayList;
import java.util.List;

public class GDXLobbyScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Label statusLabel;
    
    private String lobbyId;
    private String lobbyName;
    private boolean isAdmin;
    
    private Table playersTable;
    private Table onlinePlayersTable;
    private List<String> lobbyPlayers = new ArrayList<>();
    private List<String> onlinePlayers = new ArrayList<>();
    private String selectedOnlinePlayer = null;
    
    private NetworkLobbyController controller;

    public GDXLobbyScreen(String lobbyId, String lobbyName, boolean isAdmin) {
        this.lobbyId = lobbyId;
        this.lobbyName = lobbyName;
        this.isAdmin = isAdmin;
        
        controller = new NetworkLobbyController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        titleLabel = new Label("Lobby: " + lobbyName, skin);
        titleLabel.setFontScale(1.2f);
        
        statusLabel = new Label("", skin);

        // Players in lobby section
        Label lobbyPlayersLabel = new Label("Players in Lobby:", skin);
        playersTable = new Table();
        playersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));

        // Online players section
        Label onlinePlayersLabel = new Label("Online Players:", skin);
        onlinePlayersTable = new Table();
        onlinePlayersTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));

        // Buttons
        TextButton inviteButton = new TextButton("Invite Selected", skin);
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton leaveButton = new TextButton("Leave Lobby", skin);
        TextButton backButton = new TextButton("Back", skin);

        inviteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inviteSelectedPlayer();
            }
        });

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

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineMenu(com.example.main.models.App.getInstance().getNetworkService()));
            }
        });

        // Set up invitation callback
        controller.setInvitationCallback(new NetworkLobbyController.InvitationCallback() {
            @Override
            public void onInvitationReceived(String lobbyId, String inviterUsername) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        showInvitationDialog(lobbyId, inviterUsername);
                    }
                });
            }
        });

        // Layout
        table.add(titleLabel).padBottom(10).row();
        table.add(statusLabel).padBottom(10).row();
        
        // Two columns layout
        Table contentTable = new Table();
        
        // Left column - Lobby players
        Table leftColumn = new Table();
        leftColumn.add(lobbyPlayersLabel).pad(5).row();
        leftColumn.add(playersTable).width(300).height(200).pad(5).row();
        
        // Right column - Online players
        Table rightColumn = new Table();
        rightColumn.add(onlinePlayersLabel).pad(5).row();
        rightColumn.add(onlinePlayersTable).width(300).height(200).pad(5).row();
        
        contentTable.add(leftColumn).pad(10);
        contentTable.add(rightColumn).pad(10);
        
        table.add(contentTable).row();
        
        // Buttons
        Table buttonTable = new Table();
        buttonTable.add(inviteButton).width(120).height(40).pad(5);
        buttonTable.add(refreshButton).width(120).height(40).pad(5);
        buttonTable.add(startGameButton).width(120).height(40).pad(5);
        buttonTable.add(leaveButton).width(120).height(40).pad(5);
        buttonTable.add(backButton).width(120).height(40).pad(5);
        
        table.add(buttonTable).pad(10).row();
        
        // Initialize with current user
        String currentUsername = App.getInstance().getCurrentUser() != null ? 
            App.getInstance().getCurrentUser().getUsername() : "Unknown";
        lobbyPlayers.add(currentUsername);
        updatePlayersTable();
        
        // Update admin status based on controller
        updateAdminStatus();
    }

    private void inviteSelectedPlayer() {
        if (selectedOnlinePlayer == null) {
            statusLabel.setText("Please select a player to invite!");
            return;
        }
        
        // Don't invite players who are already in the lobby
        if (lobbyPlayers.contains(selectedOnlinePlayer)) {
            statusLabel.setText("Player is already in the lobby!");
            return;
        }
        
        boolean success = controller.invitePlayer(selectedOnlinePlayer);
        if (success) {
            statusLabel.setText("Invitation sent to " + selectedOnlinePlayer + "!");
        } else {
            statusLabel.setText("Failed to send invitation!");
        }
    }

    private void startGame() {
        if (!isAdmin) {
            statusLabel.setText("Only the admin can start the game!");
            return;
        }
        
        if (lobbyPlayers.size() < 2) {
            statusLabel.setText("Need at least 2 players to start!");
            return;
        }
        
        boolean success = controller.startGame();
        if (success) {
            statusLabel.setText("Starting game...");
            // TODO: Transition to game screen
            Main.getInstance().setScreen(new GDXGameScreen());
        } else {
            statusLabel.setText("Failed to start game!");
        }
    }

    private void leaveLobby() {
        boolean success = controller.leaveLobby();
        if (success) {
            statusLabel.setText("Left lobby successfully!");
            Main.getInstance().setScreen(new GDXOnlineMenu(com.example.main.models.App.getInstance().getNetworkService()));
        } else {
            statusLabel.setText("Failed to leave lobby!");
        }
    }

    private void showInvitationDialog(String lobbyId, String inviterUsername) {
        GDXInvitationDialog dialog = new GDXInvitationDialog(
            "Lobby Invitation", 
            lobbyId, 
            inviterUsername,
            new GDXInvitationDialog.InvitationCallback() {
                @Override
                public void onAccept(String lobbyId, String inviterUsername) {
                    controller.acceptInvitation(lobbyId, inviterUsername);
                    statusLabel.setText("Accepted invitation to lobby!");
                }
                
                @Override
                public void onReject(String lobbyId, String inviterUsername) {
                    controller.rejectInvitation(lobbyId, inviterUsername);
                    statusLabel.setText("Declined invitation to lobby.");
                }
            }
        );
        
        dialog.showDialog(stage);
    }

    private void updatePlayersTable() {
        playersTable.clear();
        
        for (String player : lobbyPlayers) {
            Label playerLabel = new Label(player, skin);
            if (player.equals(App.getInstance().getCurrentUser() != null ? 
                App.getInstance().getCurrentUser().getUsername() : "Unknown")) {
                playerLabel.setText(player + " (You)");
            }
            playersTable.add(playerLabel).width(280).pad(5);
            playersTable.row();
        }
    }

    private void updateOnlinePlayersTable() {
        onlinePlayersTable.clear();
        
        // Add header
        onlinePlayersTable.add(new Label("Username", skin)).width(200).pad(5);
        onlinePlayersTable.add(new Label("Action", skin)).width(80).pad(5);
        onlinePlayersTable.row();
        
        String currentUsername = App.getInstance().getCurrentUser() != null ? 
            App.getInstance().getCurrentUser().getUsername() : "Unknown";
        
        // Filter available players
        List<String> availablePlayers = new ArrayList<>();
        for (String player : onlinePlayers) {
            // Don't show current user in online list
            if (player.equals(currentUsername)) {
                continue;
            }
            
            // Don't show players already in lobby
            if (lobbyPlayers.contains(player)) {
                continue;
            }
            
            availablePlayers.add(player);
        }
        
        // Add online players or show message if none available
        if (availablePlayers.isEmpty()) {
            Label noPlayersLabel = new Label("No online players available", skin);
            noPlayersLabel.setColor(0.7f, 0.7f, 0.7f, 1f); // Gray color
            onlinePlayersTable.add(noPlayersLabel).colspan(2).center().pad(20);
        } else {
            for (String player : availablePlayers) {
                Label playerLabel = new Label(player, skin);
                TextButton inviteButton = new TextButton("Invite", skin);
                
                inviteButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedOnlinePlayer = player;
                        inviteSelectedPlayer();
                    }
                });
                
                onlinePlayersTable.add(playerLabel).width(200).pad(5);
                onlinePlayersTable.add(inviteButton).width(80).pad(5);
                onlinePlayersTable.row();
            }
        }
    }

    @Override
    public void show() {
        // Connect to server when screen is shown
        if (Main.isNetworkMode()) {
            boolean connected = controller.connectToServer(Main.getServerIp(), Main.getServerPort());
            if (connected) {
                statusLabel.setText("Connected to server!");
                updateOnlinePlayers();
            } else {
                statusLabel.setText("Failed to connect to server!");
            }
        }
    }
    
    private void updateOnlinePlayers() {
        // Get online players from controller
        List<String> onlineUsers = controller.getOnlineUsers();
        onlinePlayers.clear();
        onlinePlayers.addAll(onlineUsers);
        updateOnlinePlayersTable();
    }
    
    private void updateAdminStatus() {
        // Update admin status based on controller
        isAdmin = controller.isHost();
    }
    
    private void updateUIForAdminStatus() {
        // Update UI elements based on admin status
        // For example, enable/disable invite button, start game button, etc.
        // This would be implemented based on the specific UI requirements
    }
    
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
    }
} 