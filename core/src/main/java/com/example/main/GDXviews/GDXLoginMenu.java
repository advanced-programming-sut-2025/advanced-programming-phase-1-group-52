package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.LoginMenuController;
import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.models.User;

public class GDXLoginMenu implements Screen {
    private com.example.main.service.NetworkService networkService;
    private Stage stage;
    private Skin skin;
    private TextField usernameField, passwordField;
    private Label messageLabel;
    private TextButton loginButton, backToSignupButton, forgotPasswordButton;
    private LoginMenuController controller;

    public GDXLoginMenu() {
        controller = new LoginMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        messageLabel = new Label("", skin);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        loginButton = new TextButton("Login", skin);
        backToSignupButton = new TextButton("Back to Sign Up", skin);
        forgotPasswordButton = new TextButton("Forgot Password?", skin);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                handleLogin();
            }
        });

        backToSignupButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXSignUpMenu());
            }
        });

        forgotPasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXForgotPasswordMenu());
            }
        });

        table.add(messageLabel).colspan(2).pad(10);
        table.row();
        table.add(usernameField).width(300).pad(5);
        table.row();
        table.add(passwordField).width(300).pad(5);
        table.row();
        table.add(loginButton).width(150).pad(10);
        table.row();
        table.add(forgotPasswordButton).width(150).pad(5);
        table.row();
        table.add(backToSignupButton).width(150).pad(10);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (Main.isNetworkMode()) {
            // Network mode - authenticate with server
            handleNetworkLogin(username, password);
        } else {
            // Single player mode - use local authentication
            Result result = controller.login(username, password);
            messageLabel.setText(result.Message());
            if (result.isSuccessful()) {
                System.out.println("Login successful, transitioning to main menu.");
                Main.getInstance().setScreen(new GDXMainMenu(null)); // No network service in single player
            }
        }
    }
    
    private void handleNetworkLogin(String username, String password) {
        // In network mode, authenticate directly with server
        messageLabel.setText("Connecting to server...");

        // Create network service and authenticate
        this.networkService = new com.example.main.service.NetworkService();
        com.example.main.models.App.getInstance().setNetworkService(this.networkService);

        // Connect to server
        boolean connected = networkService.connectToServer(Main.getServerIp(), Main.getServerPort());
        if (!connected) {
            messageLabel.setText("Failed to connect to server.");
            return;
        }

        // Authenticate with server. The response will be handled by ClientMessageHandler.
        if (networkService.authenticate(username, password)) {
            messageLabel.setText("Login request sent. Waiting for server response...");
        } else {
            messageLabel.setText("Failed to send login request.");
        }
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
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
