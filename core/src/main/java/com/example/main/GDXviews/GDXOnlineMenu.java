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
import com.example.main.service.NetworkService;

import java.util.List;
import java.util.Map;

public class GDXOnlineMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private NetworkService networkService;
    private NetworkLobbyController networkLobbyController;
    private Table lobbyListTable;

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
        Gdx.input.setInputProcessor(stage);
        // Update title with current user
        String username = com.example.main.models.App.getInstance().getCurrentUser() != null ?
            com.example.main.models.App.getInstance().getCurrentUser().getUsername() : "Guest";
        titleLabel.setText("Online Multiplayer - " + username);
        System.out.println("[UI LOG] GDXOnlineMenu is now visible.");
        if (networkLobbyController != null && networkLobbyController.isConnected()) {
            System.out.println("[UI LOG] Requesting lobby list from the controller.");
            networkLobbyController.requestAvailableLobbies();
        } else {
            System.err.println("[UI LOG] Cannot request lobbies: network controller is null or not connected.");
        }
    }

    public void updateLobbyList(List<Map<String, Object>> lobbies) {
        System.out.println("[UI LOG] updateLobbyList called with " + (lobbies != null ? lobbies.size() : "null") + " lobbies.");
        lobbyListTable.clear(); // Clear the old list from the UI

        if (lobbies == null || lobbies.isEmpty()) {
            System.out.println("[UI LOG] No lobbies to display. Showing 'No available lobbies' message.");
            lobbyListTable.add(new Label("No available lobbies found.", skin)).center();
            return;
        }

        System.out.println("[UI LOG] Populating lobby list table...");
        for (Map<String, Object> lobbyInfo : lobbies) {
            String lobbyId = (String) lobbyInfo.get("lobbyId");
            String name = (String) lobbyInfo.get("name");
            // Ensure numbers are handled correctly after JSON deserialization
            int playerCount = ((Number) lobbyInfo.get("playerCount")).intValue();
            int maxPlayers = ((Number) lobbyInfo.get("maxPlayers")).intValue();
            String host = (String) lobbyInfo.get("host");

            String lobbyText = String.format("%s (%d/%d) - Host: %s", name, playerCount, maxPlayers, host);

            TextButton joinButton = new TextButton("Join", skin);
            joinButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Attempting to join lobby: " + lobbyId);
                    networkLobbyController.joinLobby(lobbyId);
                }
            });

            lobbyListTable.add(new Label(lobbyText, skin)).expandX().left().pad(5);
            lobbyListTable.add(joinButton).right().pad(5);
            lobbyListTable.row();
        }
        System.out.println("[UI LOG] Finished populating lobby list table.");
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
