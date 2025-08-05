package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.example.main.enums.design.FarmThemes;
import com.example.main.service.NetworkService;

public class GDXPreGameMenu implements Screen {

    private final Main game;
    private final Stage stage;
    private final Skin skin;
    private final NetworkLobbyController controller;
    private final Label statusLabel;
    private final TextButton confirmButton;
    private final Table choicesTable;

    private FarmThemes selectedTheme = null;

    public GDXPreGameMenu(Main game, NetworkService networkService) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.controller = networkService.getLobbyController();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label titleLabel = new Label("Choose Your Farm", skin);
        titleLabel.setFontScale(1.5f);
        rootTable.add(titleLabel).padBottom(20).row();

        statusLabel = new Label("Select a farm type. This choice is permanent.", skin);
        rootTable.add(statusLabel).padBottom(20).row();

        choicesTable = new Table();
        rootTable.add(choicesTable).padBottom(20).row();

        // --- Farm Choice Buttons ---
        TextButton neutralButton = new TextButton("Neutral Farm", skin);
        TextButton minerButton = new TextButton("Miner's Farm", skin);
        TextButton fisherButton = new TextButton("Fisher's Farm", skin);

        choicesTable.add(neutralButton).width(200).height(50).pad(10);
        choicesTable.add(minerButton).width(200).height(50).pad(10);
        choicesTable.add(fisherButton).width(200).height(50).pad(10);

        neutralButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectTheme(FarmThemes.Neutral, "Neutral Farm");
            }
        });

        minerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectTheme(FarmThemes.Miner, "Miner's Farm");
            }
        });

        fisherButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectTheme(FarmThemes.Fisher, "Fisher's Farm");
            }
        });

        // --- Confirm Button ---
        confirmButton = new TextButton("Confirm Selection", skin);
        confirmButton.setDisabled(true); // Disabled until a choice is made
        rootTable.add(confirmButton).width(250).height(50).padTop(20).row();

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedTheme != null) {
                    // This method will need to be created in the controller
                    controller.submitFarmChoice(selectedTheme);
                    confirmButton.setDisabled(true);
                    statusLabel.setText("Choice submitted! Waiting for other players...");
                }
            }
        });
    }

    private void selectTheme(FarmThemes theme, String themeName) {
        this.selectedTheme = theme;
        statusLabel.setText("You have selected: " + themeName);
        confirmButton.setDisabled(false);
        // Optionally disable choice buttons after one is clicked
        for (Actor button : choicesTable.getChildren()) {
            ((TextButton) button).setDisabled(true);
        }
    }

    /**
     * This method will be called by the ClientMessageHandler when the game is ready to start.
     */
    public void onGameSetupComplete() {
        // All players have made their choice, transition to the main game screen
        game.setScreen(new GDXGameScreen());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
