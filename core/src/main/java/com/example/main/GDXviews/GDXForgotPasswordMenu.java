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
import com.example.main.models.Result;

public class GDXForgotPasswordMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private TextField usernameField, answerField, newPasswordField;
    private Label messageLabel;
    private TextButton submitAnswerButton, backButton;
    private LoginMenuController controller;

    public GDXForgotPasswordMenu() {
        controller = new LoginMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        messageLabel = new Label("", skin);
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter Username");

        answerField = new TextField("", skin);
        answerField.setMessageText("Answer");

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New Password");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        submitAnswerButton = new TextButton("Change Password", skin);
        backButton = new TextButton("Back", skin);

        submitAnswerButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                String answer = answerField.getText().trim();
                String newPassword = newPasswordField.getText();

                Result result = controller.resetPasswordProcess(username, answer, newPassword);
                messageLabel.setText(result.Message());

                if (result.isSuccessful()) {
                    Main.getInstance().setScreen(new GDXLoginMenu());
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXLoginMenu());
            }
        });

        table.add(messageLabel).colspan(2).pad(10);
        table.row();
        table.add(usernameField).width(300).pad(5).colspan(2);
        table.row();
        table.add(answerField).width(300).pad(5).colspan(2);
        table.row();
        table.add(newPasswordField).width(300).pad(5).colspan(2);
        table.row();
        table.add(submitAnswerButton).width(200).pad(10).colspan(2);
        table.row();
        table.add(backButton).width(150).pad(10).colspan(2);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
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
