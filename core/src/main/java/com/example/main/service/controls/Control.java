package com.example.main.service.controls;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.main.configuration.preferences.ControlsData;


public interface Control {

    void attachInputListener(InputMultiplexer inputMultiplexer);


    void update(Viewport gameViewport, float gameX, float gameY);


    Vector2 getMovementDirection();


    void setControlListener(ControlListener listener);


    ControlsData toData();


    void copy(ControlsData data);


    boolean isActive();


    ControlType getType();


    void reset();
}
