package com.example.main.controller.action;

import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.kiwi.util.gdx.GdxUtilities;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.example.main.configuration.Configuration;

/** Since this class implements ActionContainer and is annotated with ViewActionContainer, its methods will be reflected
 * and available in all LML templates. Note that this class is a component like any other, so it can inject any fields,
 * use Initiate-annotated methods, etc. */
@ViewActionContainer("global")
public class Global implements ActionContainer {
    /** This is a mock-up method that does nothing. It will be available in LML templates through "close" (annotation
     * argument) and "noOp" (method name) IDs. */
    @LmlAction("close")
    public void noOp() {
    }

    /** @return true if the game is currently running on a mobile platform. */
    @LmlAction("isMobile")
    public boolean isOnMobilePlatform() {
        return GdxUtilities.isMobile();
    }

    /** @return total amount of playable characters. */
    @LmlAction("playersAmount")
    public int getPlayersAmount() {
        return Configuration.PLAYERS_AMOUNT;
    }
}