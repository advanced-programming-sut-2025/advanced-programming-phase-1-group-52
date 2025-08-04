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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.Main;
import com.example.main.controller.ProfileMenuController;
import com.example.main.models.App;
import com.example.main.models.Result;

public class GDXProfileMenu implements Screen {
    private Stage stage;
    private Skin skin;
    private ProfileMenuController controller;

    private TextField usernameField, nicknameField, emailField, newPasswordField, oldPasswordField;
    private Label messageLabel, userInfoLabel;
    private TextButton changeUsernameButton, changeNicknameButton, changeEmailButton, changePasswordButton, userInfoButton, backButton;

    public GDXProfileMenu() {
        controller = new ProfileMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        messageLabel = new Label("", skin);
        userInfoLabel = new Label("", skin);
        userInfoLabel.setWrap(true);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("New Username");

        nicknameField = new TextField("", skin);
        nicknameField.setMessageText("New Nickname");

        emailField = new TextField("", skin);
        emailField.setMessageText("New Email");

        oldPasswordField = new TextField("", skin);
        oldPasswordField.setMessageText("Old Password");
        oldPasswordField.setPasswordMode(true);
        oldPasswordField.setPasswordCharacter('*');

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New Password");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        changeUsernameButton = new TextButton("Change Username", skin);
        changeNicknameButton = new TextButton("Change Nickname", skin);
        changeEmailButton = new TextButton("Change Email", skin);
        changePasswordButton = new TextButton("Change Password", skin);
        userInfoButton = new TextButton("Show User Info", skin);
        backButton = new TextButton("Back to Main Menu", skin);

        // Add listeners to buttons
        changeUsernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.changeUsername(usernameField.getText());
                messageLabel.setText(result.Message());
            }
        });

        changeNicknameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.changeNickname(nicknameField.getText());
                messageLabel.setText(result.Message());
            }
        });

        changeEmailButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.changeEmail(emailField.getText());
                messageLabel.setText(result.Message());
            }
        });

        changePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.changePassword(newPasswordField.getText(), oldPasswordField.getText());
                messageLabel.setText(result.Message());
            }
        });

        userInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.userInfo();
                userInfoLabel.setText(result.Message());
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new GDXMainMenu(com.example.main.models.App.getInstance().getNetworkService()));
            }
        });


        // Layout the table
        table.add(messageLabel).colspan(3).pad(10).center();
        table.row();
        table.add(new Label("Change Username:", skin)).left().padRight(10);
        table.add(usernameField).width(200).pad(5);
        table.add(changeUsernameButton).pad(5);
        table.row();
        table.add(new Label("Change Nickname:", skin)).left().padRight(10);
        table.add(nicknameField).width(200).pad(5);
        table.add(changeNicknameButton).pad(5);
        table.row();
        table.add(new Label("Change Email:", skin)).left().padRight(10);
        table.add(emailField).width(200).pad(5);
        table.add(changeEmailButton).pad(5);
        table.row();
        table.add(new Label("Old Password:", skin)).left().padRight(10);
        table.add(oldPasswordField).width(200).pad(5);
        table.row();
        table.add(new Label("New Password:", skin)).left().padRight(10);
        table.add(newPasswordField).width(200).pad(5);
        table.add(changePasswordButton).pad(5);
        table.row().padTop(20);
        table.add(userInfoButton).colspan(3).center().pad(10);
        table.row();
        table.add(userInfoLabel).width(400).colspan(3).center().pad(10);
        table.row().padTop(20);
        table.add(backButton).colspan(3).center().pad(10);
    }

    @Override
    public void show() {
        // You can refresh user info here if needed when the screen is shown
        String username = App.getInstance().getCurrentUser() != null ? App.getInstance().getCurrentUser().getUsername() : "Guest";
        userInfoLabel.setText("User: " + username);
        messageLabel.setText("");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
