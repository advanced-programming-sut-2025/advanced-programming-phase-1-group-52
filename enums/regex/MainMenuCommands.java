package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands {
    MenuEnter("(\\s*)menu(\\s+)enter(?<menu_name>\\S+)(\\s*)"),
    MenuExit("(\\s*)menu(\\s+)exit(\\s*)"),
    ShowCurrentMenu("(\\s*)show(\\s+)current(\\s+)menu(\\s*)"),
    UserLogout("(\\s*)user(\\s+)logout(\\s*)");

    private final String pattern;

    MainMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
