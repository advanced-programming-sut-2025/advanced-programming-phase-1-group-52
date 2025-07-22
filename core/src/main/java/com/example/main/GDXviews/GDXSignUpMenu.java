package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.SignUpMenuController;
import com.example.main.enums.regex.SecurityQuestion;
import com.example.main.models.Result;

public class GDXSignUpMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private TextField usernameField, passwordField, passwordConfirmField, nicknameField, emailField;
    private SelectBox<String> genderSelectBox, questionSelectBox;
    private TextField answerField, answerConfirmField;
    private Label messageLabel;
    private TextButton registerButton, loginButton, generatePasswordButton;
    private CheckBox showPasswordCheckBox;
    private SignUpMenuController controller;

    public GDXSignUpMenu() {
        controller = new SignUpMenuController();
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

        generatePasswordButton = new TextButton("Gen", skin);
        generatePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Result result = controller.generatePassword();
                if (result.isSuccessful()) {
                    String generated = result.Message();
                    passwordField.setText(generated);
                    passwordConfirmField.setText(generated);
                }
            }
        });

        passwordConfirmField = new TextField("", skin);
        passwordConfirmField.setMessageText("Confirm Password");
        passwordConfirmField.setPasswordMode(true);
        passwordConfirmField.setPasswordCharacter('*');

        showPasswordCheckBox = new CheckBox(" Show Password", skin);
        showPasswordCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean show = showPasswordCheckBox.isChecked();
                passwordField.setPasswordMode(!show);
                passwordConfirmField.setPasswordMode(!show);
            }
        });

        nicknameField = new TextField("", skin);
        nicknameField.setMessageText("Nickname");

        emailField = new TextField("", skin);
        emailField.setMessageText("Email");

        genderSelectBox = new SelectBox<>(skin);
        genderSelectBox.setItems("Male", "Female");

        questionSelectBox = new SelectBox<>(skin);
        String[] questions = new String[SecurityQuestion.values().length];
        for (int i = 0; i < SecurityQuestion.values().length; i++) {
            questions[i] = SecurityQuestion.values()[i].getQuestionText();
        }
        questionSelectBox.setItems(questions);

        answerField = new TextField("", skin);
        answerField.setMessageText("Answer");

        answerConfirmField = new TextField("", skin);
        answerConfirmField.setMessageText("Confirm Answer");

        registerButton = new TextButton("Register", skin);
        loginButton = new TextButton("Login", skin);

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                handleRegister();
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXLoginMenu());
            }
        });

        table.add(messageLabel).colspan(2).pad(10);
        table.row();
        table.add(usernameField).width(300).pad(5).colspan(2);
        table.row();

        Table passwordRow = new Table();
        passwordRow.add(passwordField).width(180).padRight(5);
        passwordRow.add(generatePasswordButton).width(50).padRight(5);
        passwordRow.add(showPasswordCheckBox);
        table.add(passwordRow).pad(5).colspan(2);
        table.row();

        table.add(passwordConfirmField).width(300).pad(5).colspan(2);
        table.row();
        table.add(nicknameField).width(300).pad(5).colspan(2);
        table.row();
        table.add(emailField).width(300).pad(5).colspan(2);
        table.row();
        table.add(genderSelectBox).width(300).pad(5).colspan(2);
        table.row();
        table.add(new Label("Security Question:", skin)).colspan(2).pad(5);
        table.row();
        table.add(questionSelectBox).width(300).pad(5).colspan(2);
        table.row();
        table.add(answerField).width(300).pad(5).colspan(2);
        table.row();
        table.add(answerConfirmField).width(300).pad(5).colspan(2);
        table.row();
        table.add(registerButton).width(150).pad(10).colspan(2);
        table.row();
        table.add(loginButton).width(150).pad(10).colspan(2);
    }

    private void handleRegister() {
        Result result = controller.register(
            usernameField.getText(),
            passwordField.getText(),
            passwordConfirmField.getText(),
            nicknameField.getText(),
            emailField.getText(),
            genderSelectBox.getSelected(),
            SecurityQuestion.values()[questionSelectBox.getSelectedIndex()],
            answerField.getText(),
            answerConfirmField.getText()
        );
        messageLabel.setText(result.Message());
        if (result.isSuccessful()) {
            Main.getInstance().setScreen(new GDXLoginMenu());
        }
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
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
