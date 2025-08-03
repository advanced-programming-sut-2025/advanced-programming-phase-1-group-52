package com.example.main.GDXviews;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GDXInvitationDialog {
    private Dialog dialog;
    private String lobbyId;
    private String inviterUsername;
    private InvitationCallback callback;
    
    public interface InvitationCallback {
        void onAccept(String lobbyId, String inviterUsername);
        void onReject(String lobbyId, String inviterUsername);
    }
    
    public GDXInvitationDialog(String title, String lobbyId, String inviterUsername, InvitationCallback callback) {
        this.lobbyId = lobbyId;
        this.inviterUsername = inviterUsername;
        this.callback = callback;
        
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        dialog = new Dialog(title, skin);
        
        dialog.text(inviterUsername + " has invited you to join their lobby!");
        dialog.row();
        
        TextButton acceptButton = new TextButton("Accept", skin);
        TextButton rejectButton = new TextButton("Reject", skin);
        
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (callback != null) {
                    callback.onAccept(lobbyId, inviterUsername);
                }
                dialog.hide();
            }
        });
        
        rejectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (callback != null) {
                    callback.onReject(lobbyId, inviterUsername);
                }
                dialog.hide();
            }
        });
        
        dialog.button(acceptButton);
        dialog.button(rejectButton);
    }
    
    public void showDialog(Stage stage) {
        dialog.show(stage);
    }
} 