package com.example.main.service;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.config.AutumnActionPriority;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.example.main.configuration.Configuration;
import com.example.main.configuration.preferences.ControlsData;
import com.example.main.configuration.preferences.ControlsPreference;
import com.example.main.service.controls.Control;

/** Manages players' controls. */
@Component
public class ControlsService {
    @Inject private ControlsPreference preference;
    private final Array<Control> controls = new Array<Control>();

    @Initiate
    public void readControlsFromPreferences() {
        final Array<ControlsData> controlsPreferences = preference.get();
        for (final ControlsData data : controlsPreferences) {
            controls.add(data.type.create(data));
        }
    }

    @Destroy(priority = AutumnActionPriority.TOP_PRIORITY)
    public void saveControlsInPreferences() {
        final Array<ControlsData> controlsData = GdxArrays.newArray(Configuration.PLAYERS_AMOUNT);
        for (final Control control : controls) {
            controlsData.add(control.toData());
        }
        controlsData.size = Configuration.PLAYERS_AMOUNT;
        preference.set(controlsData);
    }

    /** @param playerId ID of the player to check.
     * @return true if the player ID is valid and the player has an active controller attached. */
    public boolean isActive(final int playerId) {
        return GdxArrays.isIndexValid(controls, playerId) && controls.get(playerId).isActive();
    }

    /** @param playerId ID of the player.
     * @return controller assigned to the player. */
    public Control getControl(final int playerId) {
        return controls.get(playerId);
    }

    /** @param playerId ID of the player.
     * @param control will be assigned to the selected player. */
    public void setControl(final int playerId, final Control control) {
        controls.set(playerId, control);
    }

    /** @return controllers assigned to all players. Order matches players' IDs. */
    public Array<Control> getControls() {
        return controls;
    }
}