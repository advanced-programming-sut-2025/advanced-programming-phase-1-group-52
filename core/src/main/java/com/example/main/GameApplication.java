package com.example.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.main.views.AppView;

public class GameApplication extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    
    // Terminal functionality
    private AppView appView;
    private Thread terminalThread;
    private boolean showTerminalMode = false;
    
    // Graphics state
    private String displayText = "Press T for Terminal Mode, ESC to exit";
    private String terminalStatus = "Graphics Mode";
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        
        // Set up camera and viewport for proper scaling
        camera = new OrthographicCamera();
        viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
        
        // Initialize terminal functionality
        appView = new AppView();
        
        // Set up font scaling
        font.getData().setScale(1.5f);
        
        Gdx.app.log("GameApplication", "Application created successfully");
    }
    
    @Override
    public void render() {
        handleInput();
        
        // Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1f);
        
        // Update camera
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        // Draw UI
        batch.begin();
        
        // Draw title
        font.draw(batch, "Stardew Valley - Hybrid Mode", 50, Main.HEIGHT - 50);
        
        // Draw current mode
        font.draw(batch, "Mode: " + terminalStatus, 50, Main.HEIGHT - 100);
        
        // Draw instructions
        font.draw(batch, displayText, 50, Main.HEIGHT - 150);
        
        if (showTerminalMode) {
            font.draw(batch, "Terminal mode is running in background", 50, Main.HEIGHT - 200);
            font.draw(batch, "Check your console for terminal interaction", 50, Main.HEIGHT - 230);
            font.draw(batch, "Press G to return to Graphics Mode", 50, Main.HEIGHT - 260);
        } else {
            font.draw(batch, "Graphics rendering active", 50, Main.HEIGHT - 200);
            font.draw(batch, "Press T to start Terminal Mode", 50, Main.HEIGHT - 230);
        }
        
        batch.end();
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (!showTerminalMode) {
                startTerminalMode();
            }
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            if (showTerminalMode) {
                stopTerminalMode();
            }
        }
    }
    
    private void startTerminalMode() {
        showTerminalMode = true;
        terminalStatus = "Terminal Mode";
        displayText = "Terminal is running - check console";
        
        // Start terminal in a separate thread so it doesn't block the graphics
        terminalThread = new Thread(() -> {
            try {
                Gdx.app.log("GameApplication", "Starting terminal mode...");
                appView.runProgram();
            } catch (Exception e) {
                Gdx.app.log("GameApplication", "Terminal mode ended: " + e.getMessage());
            }
        });
        terminalThread.setDaemon(true);
        terminalThread.start();
    }
    
    private void stopTerminalMode() {
        showTerminalMode = false;
        terminalStatus = "Graphics Mode";
        displayText = "Press T for Terminal Mode, ESC to exit";
        
        if (terminalThread != null && terminalThread.isAlive()) {
            terminalThread.interrupt();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        
        if (terminalThread != null && terminalThread.isAlive()) {
            terminalThread.interrupt();
        }
    }
} 