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
import java.util.List;
import java.util.Map;

public class GDXOnlineLobbiesMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final NetworkLobbyController controller;
    private final Table lobbyListTable;
    private final Label statusLabel;

    public GDXOnlineLobbiesMenu() {
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.controller = App.getInstance().getNetworkService().getLobbyController();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // --- Search Bar ---
        rootTable.add(new Label("Find Lobby by ID:", skin)).padTop(20);
        TextField searchField = new TextField("", skin);
        searchField.setMessageText("Enter Lobby ID...");
        rootTable.add(searchField).width(300).pad(5);
        TextButton searchButton = new TextButton("Search", skin);
        rootTable.add(searchButton).pad(5);
        rootTable.row();

        searchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                statusLabel.setText("Searching for lobby...");
                controller.findLobbyById(searchField.getText());
            }
        });

        // --- Main UI ---
        Label titleLabel = new Label("Public Lobbies", skin);
        titleLabel.setFontScale(1.5f);
        rootTable.add(titleLabel).padTop(20).colspan(3).row();

        statusLabel = new Label("Fetching lobbies...", skin);
        rootTable.add(statusLabel).padBottom(10).colspan(3).row();

        lobbyListTable = new Table();
        rootTable.add(lobbyListTable).expand().fill().minWidth(600).minHeight(300).colspan(3).row();

        TextButton refreshButton = new TextButton("Refresh List", skin);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.isConnected()) {
                    controller.requestAvailableLobbies();
                }
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
        buttonTable.add(refreshButton).width(150).height(40).pad(5);
        buttonTable.add(backButton).width(150).height(40).pad(5);
        rootTable.add(buttonTable).pad(10).colspan(3);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if (controller.isConnected()) {
            controller.requestAvailableLobbies();
        }
    }

    public void updateLobbyList(List<Map<String, Object>> lobbies) {
        lobbyListTable.clear();
        if (lobbies == null || lobbies.isEmpty()) {
            lobbyListTable.add(new Label("No public lobbies found.", skin)).center();
            return;
        }
        for (Map<String, Object> lobbyInfo : lobbies) {
            String lobbyId = (String) lobbyInfo.get("lobbyId");
            String name = (String) lobbyInfo.get("name");
            boolean isPrivate = (Boolean) lobbyInfo.getOrDefault("isPrivate", false);
            String lobbyText = String.format("%s %s", name, (isPrivate ? "(Private)" : ""));
            TextButton joinButton = new TextButton("Join", skin);
            joinButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleFoundLobby(lobbyId, isPrivate);
                }
            });
            lobbyListTable.add(new Label(lobbyText, skin)).expandX().left().pad(5);
            lobbyListTable.add(joinButton).right().pad(5);
            lobbyListTable.row();
        }
    }

    public void handleFoundLobby(String lobbyId, boolean isPrivate) {
        if (isPrivate) {
            showPasswordDialog(lobbyId);
        } else {
            controller.joinLobby(lobbyId, null);
        }
    }

    private void showPasswordDialog(String lobbyId) {
        Dialog dialog = new Dialog("Enter Password", skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password...");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        dialog.text("This lobby is private. Please enter the password:");
        dialog.getContentTable().row();
        dialog.getContentTable().add(passwordField).width(250).pad(10);
        TextButton okButton = new TextButton("Join", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.joinLobby(lobbyId, passwordField.getText());
                dialog.hide();
            }
        });
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        dialog.getButtonTable().add(okButton).pad(10);
        dialog.getButtonTable().add(cancelButton).pad(10);
        dialog.show(stage);
    }

    public void showStatus(String message) {
        statusLabel.setText(message);
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
