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
import com.example.main.controller.MainMenuController;
import com.example.main.models.Result;

public class GDXMainMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private MainMenuController controller;
    private Label welcomeLabel;

    public GDXMainMenu() {
        controller = new MainMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        welcomeLabel = new Label("Welcome, Guest", skin);

        TextButton preGameMenuButton = new TextButton("Go to PreGame Menu", skin);
        TextButton networkLobbyButton = new TextButton("Play Online", skin);
        TextButton profileMenuButton = new TextButton("Go to Profile Menu", skin);
        TextButton logoutButton = new TextButton("Logout", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        preGameMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.menuEnter("PreGameMenu");
                System.out.println(result.Message());
                Main.getInstance().setScreen(new GDXPreGameMenu());
            }
        });

        networkLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.menuEnter("NetworkLobby");
                System.out.println(result.Message());
                Main.getInstance().setScreen(new GDXOnlineMenu());
            }
        });

        profileMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.menuEnter("ProfileMenu");
                System.out.println(result.Message());
                Main.getInstance().setScreen(new GDXProfileMenu());
            }
        });

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.userLogout();
                System.out.println(result.Message());
                Main.getInstance().setScreen(new GDXLoginMenu());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.menuExit();
                Gdx.app.exit();
            }
        });

        table.add(welcomeLabel).padBottom(20).row();
        table.add(preGameMenuButton).width(200).pad(10).row();
        table.add(networkLobbyButton).width(200).pad(10).row();
        table.add(profileMenuButton).width(200).pad(10).row();
        table.add(logoutButton).width(200).pad(10).row();
        table.add(exitButton).width(200).pad(10).row();
    }

    @Override
    public void show() {
        // Update welcome message with current user
        String username = com.example.main.models.App.getInstance().getCurrentUser() != null ? 
            com.example.main.models.App.getInstance().getCurrentUser().getUsername() : "Guest";
        welcomeLabel.setText("Welcome, " + username);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
