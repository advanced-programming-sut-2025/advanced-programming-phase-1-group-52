package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands{
    Login("(\\s*)login(\\s+)-u(\\s+)(?<username>[\\S\\s]+)(\\s+)" +
            "-p(?<password>[\\S\\s]+)(\\s+)–stay(\\s+)-logged(\\s+)-in(\\s*)"),
    ForgetPassword("(\\s*)forget(\\s+)password(\\s+)-u(?<username>\\S+)(\\s*)"),
    Answer("(\\s*)answer(\\s+)-a(\\s+)(?<answer>\\S+)(\\s*)"),
    ResetPassword("(\\s*)-p(\\s+)(?<resetPassword>\\S+)(\\s*)"),
    ShowCurrentMenu("(\\s*)show(\\s+)current(\\s+)menu(\\s*)"),
    MenuExit("(\\s*)menu(\\s+)exit(\\s*)");

    private final String pattern;

    LoginMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }

}
