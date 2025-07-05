package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands{
    ChangeUsername("change username -u (?<username>\\S+)"),
    ChangeNickname("change nickname -u (?<nickname>\\S+)"),
    ChangeEmail("change email -e (?<email>\\S+)"),
    ChangePassword("change password -p (?<newPassword>[\\S\\s]+) " +
            "-o (?<oldPassword>\\S+)"),
    UserInfo("user info"),
    ShowCurrentMenu("show current menu"),
    MenuExit("menu exit"),
    GoToMainMenu("go to main menu"),;


    private final String pattern;

    ProfileMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
