package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignUpMenuCommands{
    Register("(\\s*)register(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)" +
            "-p(?<password>[\\S\\s]+)(\\s+)(?<password_confirm>[\\S\\s]+)(\\s+)" +
            "-n(?<nickname>[\\S\\s]+)(\\s+)-e(\\s+)(?<email>[\\S\\s]+)(\\s+)-g(?<gender>\\S+)(\\s*)"),
    PickQuestion("(\\s*)pick(\\s+)question(\\s+)-q(?<question_number>[\\S\\s]+)(\\s+)" +
            "-a(?<answer>[\\S\\s]+)(\\s+)-c(?<answer_confirm>\\S+)(\\s*)"),
    ShowCurrentMenu("(\\s*)show(\\s+)current(\\s+)menu(\\s*)"),
    MenuExit("(\\s*)menu(\\s+)exit(\\s*)"),
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
