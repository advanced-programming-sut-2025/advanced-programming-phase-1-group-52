package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignUpMenuCommands{
    Register("register -u (?<username>[\\S\\s]+) " +
            "-p (?<password>[\\S\\s]+) -rp (?<passwordConfirm>[\\S\\s]+) " +
            "-n (?<nickname>[\\S\\s]+) -e (?<email>[\\S\\s]+) -g (?<gender>\\S+)"),
    PickQuestion("pick question -q (?<questionNumber>[\\S\\s]+) " +
            "-a (?<answer>[\\S\\s]+) -c (?<answerConfirm>\\S+)"),
    ShowCurrentMenu("(\\s*)show(\\s+)current(\\s+)menu(\\s*)"),
    MenuExit("(\\s*)menu(\\s+)exit(\\s*)"),
    ValidUsername("[A-Za-z0-9_]+"),
    ValidDigit(".*\\d.*"),
    ValidLower( ".*[a-z].*"),
    ValidUpper(".*[A-Z].*"),
    ValidSpecial(".*[!@#$%^&*()].*"),
    ValidEmail("[a-zA-Z0-9_\\.]+@[a-zA-Z0-9_\\.]+\\.[a-zA-Z0-9_\\.]+"),
    GoToLoginMenu("go to login menu"),
    GeneratePassword("generate password"),;


    private final String pattern;

    SignUpMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
