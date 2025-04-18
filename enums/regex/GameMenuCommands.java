package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    GameNew(""),
    GameMap(""),
    ShowCurrentMenu(""),
    MenuExit("");

    private final String pattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
