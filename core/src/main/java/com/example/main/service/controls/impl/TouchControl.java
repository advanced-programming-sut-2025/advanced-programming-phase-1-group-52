package com.example.main.service.controls.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.main.configuration.preferences.ControlsData;
import com.example.main.service.controls.AbstractControl;
import com.example.main.service.controls.ControlType;

/** Allows to control entity with touch events. */
public class TouchControl extends AbstractControl {
    private final Vector2 entityPosition = new Vector2();
    private boolean isMoving;
    private float x;
    private float y;

    @Override
    public void attachInputListener(final InputMultiplexer inputMultiplexer) {
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
                updateDirection(screenX, Gdx.graphics.getHeight() - screenY);
                isMoving = true;
                getListener().jump();
                return false;
            }

            @Override
            public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
                updateDirection(screenX, Gdx.graphics.getHeight() - screenY);
                return false;
            }

            @Override
            public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
                stop();
                isMoving = false;
                return false;
            }
        });
    }

    private void updateDirection(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(final Viewport gameViewport, final float gameX, final float gameY) {
        gameViewport.project(entityPosition.set(gameX, gameY));
        if (isMoving) {
            updateMovementWithAngle(MathUtils.atan2(y - entityPosition.y, x - entityPosition.x));
        }
    }

    @Override
    public ControlsData toData() {
        return new ControlsData(getType()); // Touch controls require no shortcuts.
    }

    @Override
    public void copy(final ControlsData data) {
        // Touch controls require no shortcuts.
    }

    @Override
    public ControlType getType() {
        return ControlType.TOUCH;
    }
}