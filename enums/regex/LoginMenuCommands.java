package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands{
    Login("login -u (?<username>[\\S\\s]+) " +
            "-p (?<password>[\\S\\s]+) –stay -logged -in"),
    ForgetPassword("forget password -u (?<username>\\S+)"),
    Answer("answer -a (?<answer>\\S+)"),
    ResetPassword("-p (?<resetPassword>\\S+)"),
    ShowCurrentMenu("show current menu"),
    MenuExit("menu exit");

    private final String pattern;

    LoginMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }

}
