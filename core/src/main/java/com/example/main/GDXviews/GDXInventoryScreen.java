package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;

/**
 * Displays the main player menu, including inventory, skills, social, and map tabs.
 */
public class GDXInventoryScreen implements Screen {

    private final GDXGameScreen gameScreen;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Texture backgroundTexture;
    private final Skin skin;

    public GDXInventoryScreen(GDXGameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load the background texture
        backgroundTexture = new Texture("content/Cut/menu_background.png");

        // Main table to hold UI elements
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Table for the top navigation buttons
        Table navTable = new Table();
        mainTable.top().add(navTable).pad(20);

        // Create navigation buttons
        TextButton inventoryButton = new TextButton("Inventory", skin);
        TextButton skillsButton = new TextButton("Skills", skin);
        TextButton socialButton = new TextButton("Social", skin);
        TextButton mapButton = new TextButton("Map", skin);
        TextButton backButton = new TextButton("Back", skin);

        // Add listeners to buttons (for now, they just print a message)
        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Inventory tab clicked");
                // Logic to show inventory items will go here
            }
        });

        skillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Skills tab clicked");
                // Logic to show skills will go here
            }
        });

        socialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Social tab clicked");
                // Logic to show social info will go here
            }
        });

        mapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Map tab clicked");
                // Logic to show map will go here
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch back to the game screen
                Main.getInstance().setScreen(gameScreen);
            }
        });

        // Add buttons to the navigation table
        float buttonPad = 10f;
        navTable.add(inventoryButton).pad(buttonPad);
        navTable.add(skillsButton).pad(buttonPad);
        navTable.add(socialButton).pad(buttonPad);
        navTable.add(mapButton).pad(buttonPad);
        navTable.add(backButton).pad(buttonPad);
    }

    @Override
    public void show() {
        // Set the input processor to the stage when this screen is shown
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw the UI stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        backgroundTexture.dispose();
        batch.dispose();
    }
}
