package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands{
    ChangeUsername("(\\s*)change(\\s+)username(\\s+)-u(?<username>\\S+)(\\s*)"),
    ChangeNickname("(\\s*)change(\\s+)nickname(\\s+)-u(?<nickname>\\S+)(\\s*)"),
    ChangeEmail("(\\s*)change(\\s+)email(\\s+)-e(?<email>\\S+)(\\s*)"),
    ChangePassword("(\\s*)change(\\s+)password(\\s+)-p(?<new_password>[\\S\\s]+)(\\s+)" +
            "-o(?<old_password>\\S+)(\\s*)"),
    UserInfo("(\\s*)uesr(\\s+)info(\\s*)"),
    ShowCurrentMenu("(\\s*)show(\\s+)current(\\s+)menu(\\s*)"),
    MenuExit("(\\s*)menu(\\s+)exit(\\s*)");


    private final String pattern;

    ProfileMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
