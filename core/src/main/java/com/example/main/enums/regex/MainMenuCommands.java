package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands {
    MenuEnter("menu enter (?<menuName>\\S+)"),
    MenuExit("menu exit"),
    ShowCurrentMenu("show current menu"),
    UserLogout("user logout"),;

    private final String pattern;

    MainMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
