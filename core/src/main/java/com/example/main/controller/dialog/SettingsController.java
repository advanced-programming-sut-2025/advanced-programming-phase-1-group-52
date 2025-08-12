package com.example.main.controller.dialog;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxSets;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.util.LmlUtilities;
import com.example.main.service.FullscreenService;


@ViewDialog(id = "settings", value = "ui/templates/dialogs/settings.lml", cacheInstance = true)
public class SettingsController implements ActionContainer {
    @Inject private FullscreenService fullscreenService;


    @LmlAction("displayModes")
    public Array<String> getDisplayModes() {
        final ObjectSet<String> alreadyAdded = GdxSets.newSet();
        final Array<String> displayModes = GdxArrays.newArray();
        for (final DisplayMode mode : fullscreenService.getDisplayModes()) {
            final String modeName = fullscreenService.serialize(mode);
            if (alreadyAdded.contains(modeName)) {
                continue;
            }
            displayModes.add(modeName);
            alreadyAdded.add(modeName);
        }
        return displayModes;
    }


    @LmlAction("setFullscreen")
    public void setFullscreenMode(final Actor actor) {
        final String modeName = LmlUtilities.getActorId(actor);
        final DisplayMode mode = fullscreenService.deserialize(modeName);
        fullscreenService.setFullscreen(mode);
    }


    @LmlAction("resetFullscreen")
    public void setWindowedMode() {
        fullscreenService.resetFullscreen();
    }
}
