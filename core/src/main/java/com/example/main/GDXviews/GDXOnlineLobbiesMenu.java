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
import com.example.main.service.NetworkService;
import java.util.List;
import java.util.Map;

public class GDXOnlineLobbiesMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final NetworkLobbyController controller;
    private final Table lobbyListTable;

    /**
     * **FIX 1**: The constructor now accepts the Main game instance and the existing NetworkService.
     */
    public GDXOnlineLobbiesMenu() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // **FIX 2**: Use the passed-in, connected NetworkService, not a new one.
        this.controller = App.getInstance().getNetworkService().getLobbyController();

        // --- Layout Setup ---
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label titleLabel = new Label("Online Lobbies", skin);
        titleLabel.setFontScale(1.5f);
        rootTable.add(titleLabel).padBottom(20).row();

        lobbyListTable = new Table();
        lobbyListTable.setBackground(skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 0.8f));
        rootTable.add(lobbyListTable).width(600).height(400).pad(10).row();

        // --- Button Setup ---
        TextButton refreshButton = new TextButton("Refresh", skin);
        TextButton backButton = new TextButton("Back", skin);

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.isConnected()) {
                    controller.requestAvailableLobbies();
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXOnlineMenu(App.getInstance().getNetworkService()));
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(refreshButton).width(150).height(40).pad(5);
        buttonTable.add(backButton).width(150).height(40).pad(5);
        rootTable.add(buttonTable).pad(10).row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // Request the list of lobbies when the screen is shown.
        if (controller.isConnected()) {
            System.out.println("[UI LOG] Requesting lobby list from the controller.");
            controller.requestAvailableLobbies();
        } else {
            System.err.println("[UI LOG] Cannot request lobbies: controller is not connected.");
        }
    }

    public void updateLobbyList(List<Map<String, Object>> lobbies) {
        System.out.println("[UI LOG] GDXOnlineLobbiesMenu.updateLobbyList called with " + (lobbies != null ? lobbies.size() : "null") + " lobbies.");
        lobbyListTable.clear();

        if (lobbies == null || lobbies.isEmpty()) {
            lobbyListTable.add(new Label("No available lobbies found.", skin)).center();
            return;
        }

        for (Map<String, Object> lobbyInfo : lobbies) {
            String lobbyId = (String) lobbyInfo.get("lobbyId");
            String name = (String) lobbyInfo.get("name"); // The lobby name is here.
            int playerCount = ((Number) lobbyInfo.get("playerCount")).intValue();
            int maxPlayers = ((Number) lobbyInfo.get("maxPlayers")).intValue();
            String host = (String) lobbyInfo.get("host");

            String lobbyText = String.format("%s (%d/%d) - Host: %s", name, playerCount, maxPlayers, host);
            TextButton joinButton = new TextButton("Join", skin);
            joinButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("UI: Join button clicked for lobby: " + lobbyId);
                    // **THE FIX**: The button's only job is to send the request.
                    // The screen change will be handled by the message handler when the server replies.
                    controller.joinLobby(lobbyId);
                }
            });

            lobbyListTable.add(new Label(lobbyText, skin)).expandX().left().pad(5);
            lobbyListTable.add(joinButton).right().pad(5);
            lobbyListTable.row();
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
