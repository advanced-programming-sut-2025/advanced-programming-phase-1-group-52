package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.example.main.Main;
import com.example.main.controller.LoginMenuController;
import com.example.main.models.Result;

public class GDXLoginMenu implements Screen {
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
        Result result = controller.login(usernameField.getText(), passwordField.getText());
        messageLabel.setText(result.Message());
        if (result.isSuccessful()) {
            Main.getInstance().setScreen(new GDXMainMenu());
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
