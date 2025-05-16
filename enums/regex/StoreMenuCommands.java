package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StoreMenuCommands {
    BuildBarnOrCoop("^build -a (?<building_name>.+?)" +
            "-l(?<x>\\d+),(?<y>\\d+)$"),
    BuyAnimal("^buy animal -a (?<animal>[\\S\\s]+) -n (?<name>\\S+)$"),
    ShowAllProducts("show all products"),
    ShowAvailableProducts("show all available products"),
    Purchase("purchase (?<productName>\\S+) -n (?<amount>\\d+)"),
    ;
    private final String pattern;

    StoreMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
