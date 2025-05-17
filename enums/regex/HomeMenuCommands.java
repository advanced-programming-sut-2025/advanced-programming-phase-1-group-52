package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HomeMenuCommands {
    CraftingShowRecipes("crafting show recipes"),
    Crafting("crafting craft (?<itemName>\\S+)"),
    CookingRefrigerator("^(\\s*)cooking(\\s+)refrigerator(\\s+)(put|pick)(\\s+)(?<item>\\S+)(\\s*)$"),
    CookingShowRecipes("^(\\s*)cooking(\\s+)show(\\s+)recipes(\\s*)$"),
    CookingPrepare("^(\\s*)cooking(\\s+)prepare(\\s+)(?<recipeName>\\S+)(\\s*)$"),
    Back("^(\\s*)back");

    private final String pattern;

    HomeMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
