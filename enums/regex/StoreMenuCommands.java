package enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StoreMenuCommands {
    BuildBarnOrCoop("^(\\s*)build(\\s+)-a(\\s+)(?<building_name>.+?)(\\s+)" +
            "-l(\\s*)(?<x>\\d+),(\\s*)(?<y>\\d+)(\\s*)$"),
    BuyAnimal("^(\\s*)buy(\\s+)animal(\\s+)-a(?<animal>[\\S\\s]+)(\\s+)-n(\\s+)(?<name>\\S+)(\\s*)$"),
    ShowAllProducts("show all products"),
    ShowAvailableProducts("show all available products"),
    Purchase("purchase (?<product_name>\\S+) -n (?<amount>\\d+)"),
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
