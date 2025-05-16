package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignUpMenuCommands{
    Register("register -u (?<username>[\\S\\s]+) " +
            "-p (?<password>[\\S\\s]+) (?<passwordConfirm>[\\S\\s]+) " +
            "-n (?<nickname>[\\S\\s]+) -e (?<email>[\\S\\s]+) -g (?<gender>\\S+)"),
    PickQuestion("pick question -q (?<questionNumber>[\\S\\s]+) " +
            "-a (?<answer>[\\S\\s]+) -c (?<answerConfirm>\\S+)"),
    ShowCurrentMenu("show current menu"),
    MenuExit("menu exit"),
    ValidUsername("[A-Za-z0-9_]+"),
    ValidDigit("(?=.*[0-9])"),
    ValidLower( "(?=.*[a-z])"),
    ValidUpper("(?=.*[A-Z])"),
    ValidSpecial("(?=.*[^0-9A-Za-z])"),
    ValidEmail("[a-zA-Z0-9_\\\\.]+@[a-zA-Z0-9_\\\\.]+\\\\.[a-zA-Z0-9_\\\\.]+"),
    ;


    private final String pattern;

    SignUpMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
